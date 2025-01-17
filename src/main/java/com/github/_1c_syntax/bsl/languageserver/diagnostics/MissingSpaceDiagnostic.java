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
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticMetadata;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticParameter;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticSeverity;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticTag;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticType;
import com.github._1c_syntax.bsl.languageserver.providers.CodeActionProvider;
import com.github._1c_syntax.bsl.parser.BSLLexer;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@DiagnosticMetadata(
  type = DiagnosticType.CODE_SMELL,
  severity = DiagnosticSeverity.INFO,
  minutesToFix = 1,
  tags = {
    DiagnosticTag.BADPRACTICE
  }
)
public class MissingSpaceDiagnostic extends AbstractVisitorDiagnostic implements QuickFixProvider {

  // символы, требующие пробелы только слева
  private static final String DEFAULT_LIST_FOR_CHECK_LEFT = "";
  // ... только справа
  private static final String DEFAULT_LIST_FOR_CHECK_RIGHT = ", ;";
  // ... с обеих сторон
  private static final String DEFAULT_LIST_FOR_CHECK_LEFT_AND_RIGHT = "+ - * / = % < > <> <= >=";
  // Проверять пробел справа от унарного знака
  private static final boolean DEFAULT_CHECK_SPACE_TO_RIGHT_OF_UNARY = false;
  // Разрешить несколько запятых подряд
  private static final boolean DEFAULT_ALLOW_MULTIPLE_COMMAS = false;

  private final String[] sampleMessage = new String[3];

  @DiagnosticParameter(
    type = String.class,
    defaultValue = "" + DEFAULT_LIST_FOR_CHECK_LEFT,
    description = "Список символов для проверки слева (разделенные пробелом). Например: ) ="
  )
  private String listForCheckLeft = getRegularString(DEFAULT_LIST_FOR_CHECK_LEFT);

  @DiagnosticParameter(
    type = String.class,
    defaultValue = "" + DEFAULT_LIST_FOR_CHECK_RIGHT,
    description = "Список символов для проверки справа (разделенные пробелом). Например: ( ="
  )
  private String listForCheckRight = getRegularString(DEFAULT_LIST_FOR_CHECK_RIGHT);

  @DiagnosticParameter(
    type = String.class,
    defaultValue = "" + DEFAULT_LIST_FOR_CHECK_LEFT_AND_RIGHT,
    description = "Список символов для проверки с обоих сторон (разделенные пробелом). Например: + - * / = % < >"
  )
  private String listForCheckLeftAndRight = getRegularString(DEFAULT_LIST_FOR_CHECK_LEFT_AND_RIGHT);

  @DiagnosticParameter(
    type = Boolean.class,
    defaultValue = "" + DEFAULT_CHECK_SPACE_TO_RIGHT_OF_UNARY,
    description = "Проверять наличие пробела справа от унарных знаков (+ -)"
  )
  private Boolean checkSpaceToRightOfUnary = DEFAULT_CHECK_SPACE_TO_RIGHT_OF_UNARY;

  @DiagnosticParameter(
    type = Boolean.class,
    defaultValue = "" + DEFAULT_ALLOW_MULTIPLE_COMMAS,
    description = "Разрешать несколько запятых подряд"
  )
  private Boolean allowMultipleCommas = DEFAULT_ALLOW_MULTIPLE_COMMAS;

  private Pattern patternL = compilePattern(listForCheckLeft);
  private Pattern patternR = compilePattern(listForCheckRight);
  private Pattern patternLr = compilePattern(listForCheckLeftAndRight);
  private Pattern patternNotSpace = compilePattern("\\S+");

  @Override
  public List<Diagnostic> getDiagnostics(DocumentContext documentContext) {

    sampleMessage[0] = getResourceString("wordLeft");         // "Слева"
    sampleMessage[1] = getResourceString("wordRight");        // "Справа"
    sampleMessage[2] = getResourceString("wordLeftAndRight"); // "Слева и справа"

    diagnosticStorage.clearDiagnostics();

    List<Token> tokens = documentContext.getTokens();
    List<Token> foundTokens;

    // проверяем слева
    if (patternL != null) {
      foundTokens = findTokensByPattern(tokens, patternL);

      foundTokens.stream()
        .filter((Token t) -> noSpaceLeft(tokens, t))
        .forEach((Token t) ->
          diagnosticStorage.addDiagnostic(t, getErrorMessage(0, t.getText()))
        );
    }

    // проверяем справа
    if (patternR != null) {
      foundTokens = findTokensByPattern(tokens, patternR);

      foundTokens.stream()
        .filter((Token t) -> noSpaceRight(tokens, t))
        .forEach((Token t) ->
          diagnosticStorage.addDiagnostic(t, getErrorMessage(1, t.getText()))
        );
    }

    // проверяем слева и справа
    if (patternLr != null) {
      foundTokens = findTokensByPattern(tokens, patternLr);

      foundTokens.stream()
        .filter((Token t) -> noSpaceLeft(tokens, t) && !noSpaceRight(tokens, t))
        .forEach((Token t) ->
          diagnosticStorage.addDiagnostic(t, getErrorMessage(0, t.getText()))
        );

      foundTokens.stream()
        .filter((Token t) -> !noSpaceLeft(tokens, t) && noSpaceRight(tokens, t))
        .forEach((Token t) ->
          diagnosticStorage.addDiagnostic(t, getErrorMessage(1, t.getText()))
        );

      foundTokens.stream()
        .filter((Token t) -> noSpaceLeft(tokens, t) && noSpaceRight(tokens, t))
        .forEach((Token t) ->
          diagnosticStorage.addDiagnostic(t, getErrorMessage(2, t.getText()))
        );

    }

    return diagnosticStorage.getDiagnostics();
  }

  @Override
  public void configure(Map<String, Object> configuration) {
    if (configuration == null) {
      return;
    }

    String listLParam =
      (String) configuration.getOrDefault("listForCheckLeft", DEFAULT_LIST_FOR_CHECK_LEFT);
    listForCheckLeft = getRegularString(listLParam);
    patternL = compilePattern(listForCheckLeft);

    String listRParam =
      (String) configuration.getOrDefault("listForCheckRight", DEFAULT_LIST_FOR_CHECK_RIGHT);
    listForCheckRight = getRegularString(listRParam);
    patternR = compilePattern(listForCheckRight);

    String listLRParam =
      (String) configuration.getOrDefault("listForCheckLeftAndRight", DEFAULT_LIST_FOR_CHECK_LEFT_AND_RIGHT);
    listForCheckLeftAndRight = getRegularString(listLRParam);
    patternLr = compilePattern(listForCheckLeftAndRight);

    checkSpaceToRightOfUnary =
      (boolean) configuration.getOrDefault("checkSpaceToRightOfUnary", checkSpaceToRightOfUnary);

    allowMultipleCommas =
      (boolean) configuration.getOrDefault("allowMultipleCommas", allowMultipleCommas);
  }

  private static List<Token> findTokensByPattern(List<Token> tokens, Pattern pattern) {
    return tokens
      .parallelStream()
      .filter((Token t) -> pattern.matcher(t.getText()).matches())
      .collect(Collectors.toList());
  }

  private static String getRegularString(String string) {

    if (string.isEmpty()) {
      return "";
    }

    StringBuilder singleChar = new StringBuilder();
    StringBuilder doubleChar = new StringBuilder();

    String[] listOfString = string.trim().split(" ");

    for (String s : listOfString) {
      if (s.length() == 1) {
        singleChar.append(s);
      } else {
        doubleChar.append("|(").append(s).append(")");
      }
    }

    return "[\\Q" + singleChar + "\\E]" + doubleChar;
  }

  private static Pattern compilePattern(String string) {

    if (string.isEmpty()) {
      return null;
    }

    return Pattern.compile(string, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
  }

  private boolean noSpaceLeft(List<Token> tokens, Token t) {

    Token previousToken = tokens.get(t.getTokenIndex() - 1);
    return patternNotSpace.matcher(previousToken.getText()).matches();
  }

  private boolean noSpaceRight(List<Token> tokens, Token t) {

    // Если это унарный + или -, то пробел справа проверяем в соответствии с параметром checkSpaceToRightOfUnary
    // Надо понять, что они унарные
    if ((t.getType() == BSLLexer.PLUS || t.getType() == BSLLexer.MINUS)
      && isUnaryChar(tokens, t) && !Boolean.TRUE.equals(checkSpaceToRightOfUnary)) {
      return false;
    }

    Token nextToken;
    if (tokens.size() > t.getTokenIndex() + 1) {
      nextToken = tokens.get(t.getTokenIndex() + 1);
    } else {
      return false;
    }

    // Если это запятая и включен allowMultipleCommas, то допустимо что бы справа от нее была еще запятая
    if (Boolean.TRUE.equals(allowMultipleCommas)
      && (t.getType() == BSLLexer.COMMA && nextToken.getType() == BSLLexer.COMMA)) {
      return false;
    }
    return patternNotSpace.matcher(nextToken.getText()).matches();
  }

  private boolean isUnaryChar(List<Token> tokens, Token t) {

    // Унарные + и -
    // Унарным считаем, если перед ним (пропуская пробельные символы) находим + - * / = % < > ( [ , Возврат <> <= >=

    Pattern checkChar = compilePattern(getRegularString("+ - * / = % < > ( [ , Возврат <> <= >="));
    if (checkChar == null) {
      return false;
    }

    int currentIndex = t.getTokenIndex() - 1;
    while (currentIndex > 0) {

      if (patternNotSpace.matcher(tokens.get(currentIndex).getText()).matches()) {
        return checkChar.matcher(tokens.get(currentIndex).getText()).matches();
      }

      currentIndex--;
    }
    return true;
  }

  private String getErrorMessage(int errCode, String tokenText) {
    return getDiagnosticMessage(sampleMessage[errCode], tokenText);
  }

  @Override
  public List<CodeAction> getQuickFixes(
    List<Diagnostic> diagnostics,
    CodeActionParams params,
    DocumentContext documentContext
  ) {

    List<TextEdit> textEdits = new ArrayList<>();

    diagnostics.forEach((Diagnostic diagnostic) -> {
      String diagnosticMessage = diagnostic.getMessage().toLowerCase(Locale.ENGLISH);

      // TODO @YanSergey. Переделать после выполнения issue #371 'Доработки ядра. Хранение информации для квикфиксов'
      Boolean missedLeft = diagnosticMessage.contains("слева") || diagnosticMessage.contains("left");
      Boolean missedRight = diagnosticMessage.contains("справа") || diagnosticMessage.contains("right");

      Range range = diagnostic.getRange();

      if (Boolean.TRUE.equals(missedLeft)) {
        TextEdit textEdit = new TextEdit(
          new Range(range.getStart(), range.getStart()),
          " ");
        textEdits.add(textEdit);
      }
      if (Boolean.TRUE.equals(missedRight)) {
        TextEdit textEdit = new TextEdit(
          new Range(range.getEnd(), range.getEnd()),
          " ");
        textEdits.add(textEdit);
      }
    });

    return CodeActionProvider.createCodeActions(
      textEdits,
      getResourceString("quickFixMessage"),
      documentContext.getUri(),
      diagnostics
    );
  }
}
