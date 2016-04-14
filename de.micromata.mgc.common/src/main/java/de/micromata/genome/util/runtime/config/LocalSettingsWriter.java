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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.collections.PropertiesReadWriter;
import de.micromata.genome.util.runtime.RuntimeIOException;

/**
 * Writes local-settings.properties.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LocalSettingsWriter
{

  public static class LocalSetingsEntry
  {
    String comment;
    String key;
    String value;

    public LocalSetingsEntry(String key, String value, String comment)
    {
      this.key = key;
      this.value = value;
      this.comment = comment;
    }
  }

  public static class LocalSettingsSection
  {
    String comment;
    List<LocalSetingsEntry> entries = new ArrayList<>();

    public void put(String key, String value, String comment)
    {
      entries.add(new LocalSetingsEntry(key, value, comment));
    }
  }

  protected Charset charset = Charsets.ISO_8859_1;
  protected String headerComment;
  protected Map<String, String> allEntries = new HashMap<>();

  protected List<LocalSettingsSection> sections = new ArrayList<>();

  private LocalSettingsSection currentSection()
  {
    if (sections.isEmpty() == true) {
      sections.add(new LocalSettingsSection());
    }
    return sections.get(sections.size() - 1);
  }

  public LocalSettingsWriter setComment(String headerComment)
  {
    this.headerComment = headerComment;
    return this;
  }

  public LocalSettingsWriter put(String key, String value)
  {
    return put(key, value, "");
  }

  public LocalSettingsWriter put(String key, String value, String comment)
  {
    addcheckDuplicated(key, value);
    currentSection().put(key, value, comment);
    return this;
  }

  public LocalSettingsWriter newSection(String comment)
  {
    LocalSettingsSection lsection = new LocalSettingsSection();
    LocalSettingsWriter parent = this;
    lsection.comment = comment;
    sections.add(lsection);
    return new LocalSettingsWriter()
    {
      @Override
      public LocalSettingsWriter put(String key, String value, String comment)
      {
        parent.put(key, value, comment);
        //        lsection.put(key, value, comment);
        return this;
      }
    };
  }

  protected void addcheckDuplicated(String key, String value)
  {
    if (allEntries.containsKey(key) == true) {
      throw new IllegalArgumentException("Key is dublicated: " + key);
    }
    allEntries.put(key, value);
  }

  public void store(File file) throws RuntimeIOException
  {
    try {
      store(new FileOutputStream(file));

    } catch (FileNotFoundException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  public void store(OutputStream out) throws RuntimeIOException
  {
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, charset))) {
      store(writer);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  public void store(BufferedWriter writer) throws RuntimeIOException
  {
    try {
      storeFileComments(writer);
      storeFileSections(writer);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }

  }

  protected void storeFileComments(BufferedWriter writer) throws IOException
  {
    if (StringUtils.isNotBlank(headerComment) == true) {
      PropertiesReadWriter.writeComments(writer, headerComment);
      writer.newLine();
      writer.newLine();
    }
  }

  protected void storeFileSections(BufferedWriter writer) throws IOException
  {
    for (LocalSettingsSection section : sections) {
      storeSection(writer, section);
    }
  }

  protected void storeSection(BufferedWriter writer, LocalSettingsSection section) throws IOException
  {
    if (StringUtils.isNotBlank(section.comment) == true) {
      writer.newLine();
      writer.newLine();
      PropertiesReadWriter.writeComments(writer, section.comment);
    }
    for (LocalSetingsEntry entry : section.entries) {
      storeLocalSettingsEntry(writer, entry);
    }
  }

  protected void storeLocalSettingsEntry(BufferedWriter writer, LocalSetingsEntry entry) throws IOException
  {
    if (StringUtils.isNotBlank(entry.comment) == true) {
      writer.newLine();
      PropertiesReadWriter.writeComments(writer, entry.comment);
    }
    writeEntryKeyValue(writer, entry.key, entry.value);
  }

  protected void writeEntryKeyValue(BufferedWriter writer, String key, String value) throws IOException
  {
    String enkey = PropertiesReadWriter.saveConvert(key, true, true, true);
    String envalue;
    if (value == null) {
      envalue = "";
    } else {
      envalue = PropertiesReadWriter.saveConvert(value, false, true, false);
    }
    writer.write(enkey + "=" + envalue);
    writer.newLine();
  }

}
