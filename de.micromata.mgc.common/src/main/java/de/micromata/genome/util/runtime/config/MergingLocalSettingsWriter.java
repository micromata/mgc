package de.micromata.genome.util.runtime.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.micromata.genome.util.collections.PropertiesReadWriter;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.runtime.config.OrderedPropertiesWithComments.Line;
import de.micromata.genome.util.runtime.config.OrderedPropertiesWithComments.LineType;

public class MergingLocalSettingsWriter extends LocalSettingsWriter
{
  OrderedPropertiesWithComments origProps;

  /**
   * Used track already written key;
   */
  Map<String, String> written = new HashMap<>();

  public MergingLocalSettingsWriter(OrderedPropertiesWithComments origProps)
  {
    this.origProps = origProps;

  }

  @Override
  public void store(BufferedWriter writer) throws RuntimeIOException
  {
    try {
      storeFileComments(writer);
      storeFileSections(writer);
      storeMissingOrigKeys(writer);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  private void storeMissingOrigKeys(BufferedWriter writer) throws IOException
  {
    for (Map.Entry<String, String> me : origProps.originalMap.entrySet()) {
      if (written.containsKey(me.getKey()) == true) {
        continue;
      }
      writeOrigEntry(writer, me.getKey(), me.getValue());
    }
  }

  private void writeOrigEntry(BufferedWriter writer, String key, String value) throws IOException
  {
    List<String> comments = findOldCommentsForKey(key);
    if (comments.isEmpty() == false) {
      writer.newLine();
      for (String com : comments) {
        PropertiesReadWriter.writeComments(writer, com);
      }
    }
    writeEntryKeyValue(writer, key, value);
  }

  List<String> getStoredFileComments()
  {
    List<String> ret = new ArrayList<>();
    for (Line line : origProps.orginalLines) {
      if (line.lineType != LineType.Comment) {
        break;
      }
      ret.add(line.first);
    }
    return ret;
  }

  @Override
  protected void storeFileComments(BufferedWriter writer) throws IOException
  {
    List<String> orig = getStoredFileComments();
    if (orig.isEmpty() == true) {
      super.storeFileComments(writer);
      return;
    }
    for (String cmd : orig) {
      PropertiesReadWriter.writeComments(writer, cmd);
    }
    writer.newLine();
    writer.newLine();
  }

  @Override
  protected void storeLocalSettingsEntry(BufferedWriter writer, LocalSetingsEntry entry) throws IOException
  {
    List<String> comments = findOldCommentsForKey(entry.key);
    if (comments.isEmpty() == true) {
      super.storeLocalSettingsEntry(writer, entry);
      return;
    }
    writer.newLine();
    for (String com : comments) {
      PropertiesReadWriter.writeComments(writer, com);
    }
    writeEntryKeyValue(writer, entry.key, entry.value);
  }

  @Override
  protected void writeEntryKeyValue(BufferedWriter writer, String key, String value) throws IOException
  {
    super.writeEntryKeyValue(writer, key, value);
    written.put(key, value);
  }

  private int findStoredKeyLinePos(String key)
  {
    for (int i = 0; i < origProps.orginalLines.size(); ++i) {
      Line line = origProps.orginalLines.get(i);
      if (line.lineType != LineType.KeyValue) {
        continue;
      }
      if (line.first.equals(key) == true) {
        return i;
      }
    }
    return -1;
  }

  private List<String> findOldCommentsForKey(String key)
  {
    int pos = findStoredKeyLinePos(key);
    if (pos == -1) {
      return Collections.emptyList();
    }
    List<String> ret = new ArrayList<>();
    for (int i = pos - 1; i >= 0; --i) {
      Line line = origProps.orginalLines.get(i);
      if (line.lineType != LineType.Comment) {
        return ret;
      }
      ret.add(line.first);
    }
    return ret;
  }

}
