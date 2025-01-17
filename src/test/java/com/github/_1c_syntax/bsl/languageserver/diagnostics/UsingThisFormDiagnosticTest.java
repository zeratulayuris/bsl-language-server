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
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.TextEdit;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UsingThisFormDiagnosticTest extends AbstractDiagnosticTest<UsingThisFormDiagnostic> {
  private static final String THIS_OBJECT = "ЭтотОбъект";

  UsingThisFormDiagnosticTest() {
    super(UsingThisFormDiagnostic.class);
  }

  @Test
  void runTest() {
    List<Diagnostic> diagnostics = getDiagnostics();
    assertThat(diagnostics).hasSize(12);
    assertThat(diagnostics.get(0).getRange()).isEqualTo(Ranges.create(3, 20, 3, 28));
    assertThat(diagnostics.get(1).getRange()).isEqualTo(Ranges.create(4, 29, 4, 37));
    assertThat(diagnostics.get(2).getRange()).isEqualTo(Ranges.create(5, 4, 5, 12));
    assertThat(diagnostics.get(3).getRange()).isEqualTo(Ranges.create(6, 12, 6, 20));
    assertThat(diagnostics.get(4).getRange()).isEqualTo(Ranges.create(13, 19, 13, 27));
    assertThat(diagnostics.get(5).getRange()).isEqualTo(Ranges.create(14, 20, 14, 28));
    assertThat(diagnostics.get(6).getRange()).isEqualTo(Ranges.create(15, 33, 15, 41));
    assertThat(diagnostics.get(7).getRange()).isEqualTo(Ranges.create(16, 12, 16, 20));
    assertThat(diagnostics.get(8).getRange()).isEqualTo(Ranges.create(40, 16, 40, 24));
    assertThat(diagnostics.get(9).getRange()).isEqualTo(Ranges.create(41, 25, 41, 33));
    assertThat(diagnostics.get(10).getRange()).isEqualTo(Ranges.create(42, 0, 42, 8));
    assertThat(diagnostics.get(11).getRange()).isEqualTo(Ranges.create(43, 8, 43, 16));

  }

  @Test
  void runQuickFixTest() {
    List<Diagnostic> diagnostics = getDiagnostics();
    List<CodeAction> quickFixes = getQuickFixes(diagnostics.get(0).getRange());

    assertThat(quickFixes).hasSize(1);
    Map<String, List<TextEdit>> changes = quickFixes.get(0).getEdit().getChanges();
    assertThat(changes).hasSize(1);
    assertThat(changes.get("file:///fake-uri.bsl")).allMatch(t -> t.getNewText().equals(THIS_OBJECT));
  }
}