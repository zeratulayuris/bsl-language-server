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

import com.github._1c_syntax.bsl.languageserver.providers.DiagnosticProvider;
import com.github._1c_syntax.bsl.languageserver.utils.Ranges;
import org.eclipse.lsp4j.Diagnostic;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UsingHardcodePathDiagnosticTest extends AbstractDiagnosticTest<UsingHardcodePathDiagnostic> {
	UsingHardcodePathDiagnosticTest() {
		super(UsingHardcodePathDiagnostic.class);
	}

	@Test
	void test() {

		List<Diagnostic> diagnostics = getDiagnostics();

		// when
		assertThat(diagnostics).hasSize(31);

		// then
		assertThat(diagnostics)
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(5, 16, 5, 38)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(6, 16, 6, 50)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(7, 16, 7, 43)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(8, 16, 8, 59)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(9, 16, 9, 38)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(10, 16, 10, 50)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(11, 16, 11, 27)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(12, 16, 12, 27)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(13, 16, 13, 28)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(15, 16, 15, 41)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(16, 16, 16, 44)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(18, 16, 18, 27)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(19, 16, 19, 36)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(20, 16, 20, 37)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(21, 16, 21, 38)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(31, 15, 31, 31)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(35, 23, 35, 39)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(36, 23, 36, 34)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(38, 23, 38, 64)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(39, 23, 39, 64)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(41, 44, 41, 85)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(49, 18, 49, 29)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(52, 10, 52, 27)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(53, 23, 53, 60)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(57, 15, 57, 30)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(59, 22, 59, 48)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(61, 7, 61, 145)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(62, 7, 62, 119)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(63, 7, 63, 39)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(65, 59, 65, 77)))
			.anyMatch(diagnostic -> diagnostic.getRange().equals(Ranges.create(79, 2, 79, 70)));

	}

	@Test
	void testConfigure() {

		List<Diagnostic> diagnostics;
		Map<String, Object> configuration;

		// Проверяем количество срабатываний без изменения параметров
		// when
		configuration = DiagnosticProvider.getDefaultDiagnosticConfiguration(getDiagnosticInstance());
		getDiagnosticInstance().configure(configuration);
		diagnostics = getDiagnostics();

		// then
		assertThat(diagnostics).hasSize(31);

		// Выключаем поиск IP адресов
		// when
		configuration.put("enableSearchNetworkAddresses", false);
		getDiagnosticInstance().configure(configuration);
		diagnostics = getDiagnostics();

		// then
		assertThat(diagnostics).hasSize(19);

		// Изменяем ключевые слова исключения для поиска IP адресов
		// when
		configuration = DiagnosticProvider.getDefaultDiagnosticConfiguration(getDiagnosticInstance());
		configuration.put("searchWordsExclusion", "Version");
		getDiagnosticInstance().configure(configuration);
		diagnostics = getDiagnostics();

		// then
		assertThat(diagnostics).hasSize(36);

		// Изменяем состав ключевых слов поиска стандартных корневых каталогов Unix
		// when
		configuration = DiagnosticProvider.getDefaultDiagnosticConfiguration(getDiagnosticInstance());
		configuration.put("searchWordsStdPathsUnix", "home|lib");
		getDiagnosticInstance().configure(configuration);

		// then
		diagnostics = getDiagnostics();
		assertThat(diagnostics).hasSize(28);

	}
}
