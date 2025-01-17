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
package com.github._1c_syntax.bsl.languageserver.context;

import com.github._1c_syntax.bsl.languageserver.utils.Lazy;
import com.github._1c_syntax.mdclasses.metadata.ConfigurationBuilder;
import com.github._1c_syntax.mdclasses.metadata.configurations.AbstractConfiguration;
import com.github._1c_syntax.mdclasses.metadata.configurations.EmptyConfiguration;
import org.eclipse.lsp4j.TextDocumentItem;

import javax.annotation.CheckForNull;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerContext {
  private final Map<String, DocumentContext> documents = Collections.synchronizedMap(new HashMap<>());
  private final Lazy<AbstractConfiguration> configurationMetadata = new Lazy<>(this::computeConfigurationMetadata);
  @CheckForNull
  private Path configurationRoot;

  public ServerContext() {
    this(null);
  }

  public ServerContext(@CheckForNull Path configurationRoot) {
    this.configurationRoot = configurationRoot;
  }

  public Map<String, DocumentContext> getDocuments() {
    return Collections.unmodifiableMap(documents);
  }

  @CheckForNull
  public DocumentContext getDocument(String uri) {
    return documents.get(uri);
  }

  public DocumentContext addDocument(String uri, String content) {

    DocumentContext documentContext = documents.get(uri);
    if (documentContext == null) {
      documentContext = new DocumentContext(uri, content, this);
      documents.put(uri, documentContext);
    } else {
      documentContext.rebuild(content);
    }

    return documentContext;
  }

  public DocumentContext addDocument(TextDocumentItem textDocumentItem) {
    return addDocument(textDocumentItem.getUri(), textDocumentItem.getText());
  }

  public void clear() {
    documents.clear();
    configurationMetadata.clear();
  }

  public void setConfigurationRoot(@CheckForNull Path configurationRoot) {
    this.configurationRoot = configurationRoot;
  }

  public AbstractConfiguration getConfiguration() {
    return configurationMetadata.getOrCompute();
  }

  private AbstractConfiguration computeConfigurationMetadata() {
    if (configurationRoot == null) {
      return new EmptyConfiguration();
    }

    ConfigurationBuilder configurationBuilder =
      new ConfigurationBuilder(configurationRoot);

    return configurationBuilder.build();
  }


}
