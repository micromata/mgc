//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.util.runtime.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.validation.ValContext;

/**
 * an external text based configuration file.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AbstractTextConfigFileConfigModel extends AbstractLocalSettingsConfigModel
{

  /**
   * The Constant LOG.
   */
  private static final Logger LOG = Logger.getLogger(AbstractTextConfigFileConfigModel.class);

  private String title;
  private boolean editableFileName;
  /**
   * The auto create directory.
   */
  private boolean autoCreateDirectory = false;

  /**
   * The file name.
   */
  private String fileName;

  /**
   * The char set.
   */
  private Charset charSet = Charset.forName("UTF-8");

  /**
   * The content.
   */
  private String content;

  /**
   * Instantiates a new abstract text config file config model.
   *
   * @param fileName the file name
   * @param charSet the char set
   */
  public AbstractTextConfigFileConfigModel(String title, String fileName, Charset charSet)
  {
    this.title = title;
    this.fileName = fileName;
    this.charSet = charSet;
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    File f = new File(fileName);
    try {
      FileUtils.write(f, content, charSet);
    } catch (IOException ex) {
      LOG.error("Cannot write file: " + f.getAbsolutePath() + ": " + ex.getMessage(), ex);
    }
    return writer;
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    File f = new File(fileName);
    if (f.exists() == true && f.isFile() == true) {
      try {
        content = FileUtils.readFileToString(f, charSet);
      } catch (IOException ex) {
        LOG.error("Cannot read file: " + f.getAbsolutePath() + ": " + ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void validate(ValContext ctx)
  {
    // no validation

  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public Charset getCharSet()
  {
    return charSet;
  }

  public void setCharSet(Charset charSet)
  {
    this.charSet = charSet;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  public boolean isAutoCreateDirectory()
  {
    return autoCreateDirectory;
  }

  public void setAutoCreateDirectory(boolean autoCreateDirectory)
  {
    this.autoCreateDirectory = autoCreateDirectory;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public boolean isEditableFileName()
  {
    return editableFileName;
  }

  public void setEditableFileName(boolean editableFileName)
  {
    this.editableFileName = editableFileName;
  }

}
