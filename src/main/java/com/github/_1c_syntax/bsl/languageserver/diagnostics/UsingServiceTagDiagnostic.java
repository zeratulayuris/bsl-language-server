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
import com.github._1c_syntax.bsl.languageserver.utils.Ranges;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.Diagnostic;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@DiagnosticMetadata(
  type = DiagnosticType.CODE_SMELL,
  severity = DiagnosticSeverity.INFO,
  tags = {
    DiagnosticTag.BADPRACTICE
  }
)
public class UsingServiceTagDiagnostic extends AbstractVisitorDiagnostic {

  private static final String SERVICE_TAGS_DEFAULT = "todo|fixme|!!|mrg|@|отладка|debug|для\\s*отладки"
    + "|(\\{\\{|\\}\\})КОНСТРУКТОР_|(\\{\\{|\\}\\})MRG";

  @DiagnosticParameter(
    type = String.class,
    defaultValue = "" + SERVICE_TAGS_DEFAULT,
    description = "Служебные теги"
  )
  private String serviceTags = SERVICE_TAGS_DEFAULT;
  private Pattern pattern = getPatternSearch(SERVICE_TAGS_DEFAULT);

  @Override
  public void configure(Map<String, Object> configuration) {
    if (configuration == null) {
      return;
    }
    serviceTags = (String) configuration.getOrDefault("serviceTags", serviceTags);
    pattern = getPatternSearch(serviceTags);
  }

  public Pattern getPatternSearch(String value) {
    return Pattern.compile(
      "//\\s*+(" + value + ")",
      Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);
  }

  @Override
  public List<Diagnostic> getDiagnostics(DocumentContext documentContext) {

    return documentContext.getComments()
      .parallelStream()
      .filter((Token token) -> pattern.matcher(token.getText()).find())
      .map((Token token) -> {
        Matcher matcher = pattern.matcher(token.getText());
        matcher.find();
        return BSLDiagnostic.createDiagnostic(
          this,
          Ranges.create(token),
          getDiagnosticMessage(matcher.group(0)));
      })
      .collect((Collectors.toList()));

  }

}
