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

import com.github._1c_syntax.bsl.languageserver.utils.Ranges;
import com.github._1c_syntax.bsl.parser.BSLParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticRelatedInformation;
import org.eclipse.lsp4j.Range;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticStorage {

  private BSLDiagnostic diagnostic;
  private List<Diagnostic> diagnosticList = new ArrayList<>();

  DiagnosticStorage(BSLDiagnostic diagnostic) {
    this.diagnostic = diagnostic;
  }

  public List<Diagnostic> getDiagnostics() {
    return new ArrayList<>(diagnosticList);
  }

  public void clearDiagnostics() {
    diagnosticList.clear();
  }

  protected void addDiagnostic(BSLParserRuleContext node) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(diagnostic, node));
  }

  protected void addDiagnostic(BSLParserRuleContext node, String diagnosticMessage) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(diagnostic, diagnosticMessage, node));
  }

  protected void addDiagnostic(int startLine, int startChar, int endLine, int endChar) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(diagnostic, startLine, startChar, endLine, endChar));
  }

  protected void addDiagnostic(Range range) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(
      diagnostic,
      range,
      diagnostic.getDiagnosticMessage()));
  }

  protected void addDiagnostic(Range range, String diagnosticMessage) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(
      diagnostic,
      range,
      diagnosticMessage));
  }

  protected void addDiagnostic(Token token) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(
      diagnostic,
      Ranges.create(token),
      diagnostic.getDiagnosticMessage()
    ));
  }

  protected void addDiagnostic(Token startToken, Token endToken) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(
      diagnostic,
      Ranges.create(startToken, endToken),
      diagnostic.getDiagnosticMessage()
    ));
  }

  protected void addDiagnostic(Token token, String diagnosticMessage) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(
      diagnostic,
      Ranges.create(token),
      diagnosticMessage
    ));
  }

  protected void addDiagnostic(TerminalNode terminalNode) {
    addDiagnostic(terminalNode.getSymbol());
  }

  protected void addDiagnostic(TerminalNode startTerminalNode, TerminalNode stopTerminalNode) {
    addDiagnostic(startTerminalNode.getSymbol(), stopTerminalNode.getSymbol());
  }

  protected void addDiagnostic(BSLParserRuleContext node, List<DiagnosticRelatedInformation> relatedInformation) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(diagnostic, node, relatedInformation));
  }

  public void addDiagnostic(Token token, List<DiagnosticRelatedInformation> relatedInformation) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(diagnostic, token, relatedInformation));
  }

  public void addDiagnostic(
    BSLParserRuleContext node,
    String diagnosticMessage,
    List<DiagnosticRelatedInformation> relatedInformation
  ) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(
      diagnostic,
      Ranges.create(node),
      diagnosticMessage,
      relatedInformation
    ));
  }

  public void addDiagnostic(
    Token token,
    String diagnosticMessage,
    List<DiagnosticRelatedInformation> relatedInformation
  ) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(
      diagnostic,
      Ranges.create(token),
      diagnosticMessage,
      relatedInformation
    ));
  }

  public void addDiagnostic(
    Range range,
    String diagnosticMessage,
    List<DiagnosticRelatedInformation> relatedInformation
  ) {
    diagnosticList.add(BSLDiagnostic.createDiagnostic(
      diagnostic,
      range,
      diagnosticMessage,
      relatedInformation
    ));
  }
}


