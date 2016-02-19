package de.micromata.genome.util.runtime.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
  String headerComment;
  Map<String, String> allEntries = new HashMap<>();

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

  List<LocalSettingsSection> sections = new ArrayList<>();

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
    checkDuplicated(key, value);
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
        parent.checkDuplicated(key, value);
        lsection.put(key, value, comment);
        return this;
      }
    };
  }

  protected void checkDuplicated(String key, String value)
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
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, Charsets.ISO_8859_1))) {
      if (StringUtils.isNotBlank(headerComment) == true) {
        PropertiesReadWriter.writeComments(writer, headerComment);
        writer.newLine();
        writer.newLine();
      }
      for (LocalSettingsSection section : sections) {
        storeSection(writer, section);
      }
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  private void storeSection(BufferedWriter writer, LocalSettingsSection section) throws IOException
  {
    if (StringUtils.isNotBlank(section.comment) == true) {
      writer.newLine();
      writer.newLine();
      PropertiesReadWriter.writeComments(writer, section.comment);
    }
    for (LocalSetingsEntry entry : section.entries) {
      if (StringUtils.isNotBlank(entry.comment) == true) {
        writer.newLine();
        PropertiesReadWriter.writeComments(writer, entry.comment);
      }
      String enkey = PropertiesReadWriter.saveConvert(entry.key, true, true, true);
      String envalue = PropertiesReadWriter.saveConvert(entry.value, false, true, false);
      writer.write(enkey + "=" + envalue);
      writer.newLine();
    }
  }
}
