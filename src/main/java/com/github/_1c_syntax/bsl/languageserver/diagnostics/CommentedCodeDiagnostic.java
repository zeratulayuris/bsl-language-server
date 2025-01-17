/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2019
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package com.github._1c_syntax.bsl.languageserver.diagnostics;

import com.github._1c_syntax.bsl.languageserver.context.DocumentContext;
import com.github._1c_syntax.bsl.languageserver.context.symbol.MethodDescription;
import com.github._1c_syntax.bsl.languageserver.context.symbol.MethodSymbol;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticMetadata;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticParameter;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticSeverity;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticTag;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticType;
import com.github._1c_syntax.bsl.languageserver.recognizer.BSLFootprint;
import com.github._1c_syntax.bsl.languageserver.recognizer.CodeRecognizer;
import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.Tokenizer;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.Diagnostic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@DiagnosticMetadata(
  type = DiagnosticType.CODE_SMELL,
  severity = DiagnosticSeverity.MINOR,
  minutesToFix = 1,
  tags = {
    DiagnosticTag.STANDARD,
    DiagnosticTag.BADPRACTICE
  }
)
public class CommentedCodeDiagnostic extends AbstractVisitorDiagnostic {

  private static final float COMMENTED_CODE_THRESHOLD = 0.9F;

  @DiagnosticParameter(
    type = Float.class,
    defaultValue = "" + COMMENTED_CODE_THRESHOLD,
    description = "Порог чуствительности"
  )
  private float threshold = COMMENTED_CODE_THRESHOLD;

  private List<MethodDescription> methodDescriptions;
  private CodeRecognizer codeRecognizer;

  public CommentedCodeDiagnostic() {
    codeRecognizer = new CodeRecognizer(threshold, new BSLFootprint());
  }

  @Override
  public void configure(Map<String, Object> configuration) {
    if (configuration == null) {
      return;
    }
    threshold = (float) configuration.getOrDefault("threshold", threshold);
    codeRecognizer = new CodeRecognizer(threshold, new BSLFootprint());
  }

  @Override
  public List<Diagnostic> getDiagnostics(DocumentContext documentContext) {
    this.documentContext = documentContext;
    diagnosticStorage.clearDiagnostics();

    methodDescriptions = documentContext.getMethods()
      .stream()
      .map(MethodSymbol::getDescription)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());

    groupComments(documentContext.getComments())
      .stream()
      .filter(this::isCommentGroupNotMethodDescription)
      .forEach(this::checkCommentGroup);

    return diagnosticStorage.getDiagnostics();
  }

  private List<List<Token>> groupComments(List<Token> comments) {
    List<List<Token>> groups = new ArrayList<>();
    List<Token> currentGroup = null;

    for (Token comment : comments) {
      if (currentGroup == null) {
        currentGroup = initNewGroup(comment);
      } else if (isAdjacent(comment, currentGroup)) {
        currentGroup.add(comment);
      } else {
        groups.add(currentGroup);
        currentGroup = initNewGroup(comment);
      }
    }

    if (currentGroup != null) {
      groups.add(currentGroup);
    }

    return groups;
  }

  private static List<Token> initNewGroup(Token comment) {
    List<Token> group = new ArrayList<>();
    group.add(comment);
    return group;
  }

  private boolean isAdjacent(Token comment, List<Token> currentGroup) {

    Token last = currentGroup.get(currentGroup.size() - 1);
    return last.getLine() + 1 == comment.getLine()
      && onlyEmptyDelimiters(last.getTokenIndex(), comment.getTokenIndex());

  }

  private boolean onlyEmptyDelimiters(int firstTokenIndex, int lastTokenIndex) {
    if (firstTokenIndex > lastTokenIndex) {
      return false;
    }

    for (int index = firstTokenIndex + 1; index < lastTokenIndex; index++) {
      int tokenType = documentContext.getTokens().get(index).getType();
      if (tokenType != BSLParser.WHITE_SPACE) {
        return false;
      }
    }

    return true;
  }

  private boolean isCommentGroupNotMethodDescription(List<Token> commentGroup) {
    if (methodDescriptions.isEmpty()) {
      return true;
    }

    final Token first = commentGroup.get(0);
    final Token last = commentGroup.get(commentGroup.size() - 1);

    return methodDescriptions.stream().noneMatch(methodDescription -> methodDescription.contains(first, last));
  }

  private void checkCommentGroup(List<Token> commentGroup) {
    Token firstComment = commentGroup.get(0);
    Token lastComment = commentGroup.get(commentGroup.size() - 1);

    for (Token comment : commentGroup) {
      if (isTextParsedAsCode(comment.getText())) {
        diagnosticStorage.addDiagnostic(firstComment, lastComment);
        return;
      }
    }
  }

  private boolean isTextParsedAsCode(String text) {
    if (!codeRecognizer.meetsCondition(text)) {
      return false;
    }

    Tokenizer tokenizer = new Tokenizer(uncomment(text));
    final List<Token> tokens = tokenizer.getTokens();

    // Если меньше двух токенов нет смысла анализировать - это код
    if (tokens.size() >= 2) {

      List<Integer> tokenTypes = tokens.stream()
        .map(Token::getType)
        .filter(t -> t != BSLParser.WHITE_SPACE)
        .collect(Collectors.toList());

      // Если два идентификатора идут подряд - это не код
      for (int i = 0; i < tokenTypes.size() - 1; i++) {
        if (tokenTypes.get(i) == BSLParser.IDENTIFIER && tokenTypes.get(i + 1) == BSLParser.IDENTIFIER) {
          return false;
        }
      }
    }

    return true;
}

  private static String uncomment(String comment) {
    if (comment.startsWith("//")) {
      return uncomment(comment.substring(2));
    }
    return comment;
  }

}
