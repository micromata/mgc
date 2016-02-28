package de.micromata.genome.util.runtime.config;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.collections.NewPropertiesLineReader;
import de.micromata.genome.util.collections.NewPropertiesLineReader.LineReaderCallback;
import de.micromata.genome.util.collections.OrderedProperties;
import de.micromata.genome.util.collections.PropertiesReadWriter;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.Pair;

/**
 * Read LocalSettings
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class OrderedPropertiesWithComments extends OrderedProperties
{
  public static enum LineType
  {
    KeyValue, EmptyLine, Comment, Garbage
  }

  public static class Line
  {
    public LineType lineType;
    public String first;
    public String second;

    public Line(LineType lineType)
    {
      this.lineType = lineType;
      this.first = "";
      this.second = "";
    }

    public Line(LineType lineType, String first)
    {
      this.lineType = lineType;
      this.first = first;
      this.second = "";
    }

    public Line(LineType lineType, String first, String second)
    {
      this.lineType = lineType;
      this.first = first;
      this.second = second;
    }
  }

  List<Line> orginalLines = new ArrayList<>();
  Map<String, String> originalMap = new HashMap<>();
  private Charset charset = org.apache.commons.io.Charsets.ISO_8859_1;

  @Override
  public void load(InputStream inStream) throws RuntimeIOException
  {
    load(inStream, charset, null);
  }

  @Override
  public void load(InputStream inStream, KeyValueReplacer replacer) throws RuntimeIOException
  {
    load(inStream, charset, replacer);
  }

  public void load(InputStream inStream, Charset charset, KeyValueReplacer replacer) throws RuntimeIOException
  {
    NewPropertiesLineReader npreader = new NewPropertiesLineReader(new LineReaderCallback()
    {

      @Override
      public void onKeyValue(String key, String value)
      {
        addRawKeyValue(key, value, replacer);

      }

      @Override
      public void onGarbage(String line)
      {
        orginalLines.add(new Line(LineType.Garbage, line));
      }

      @Override
      public void onEmptyLine()
      {
        orginalLines.add(new Line(LineType.EmptyLine));
      }

      @Override
      public void onComment(String comment)
      {
        orginalLines.add(new Line(LineType.Comment, comment));

      }
    });
    npreader.read(inStream, charset);
  }

  protected void addRawKeyValue(String key, String value, KeyValueReplacer replacer)
  {
    orginalLines.add(new Line(LineType.KeyValue, key, value));
    key = decodeKey(key);
    value = decodeValue(value);
    originalMap.put(key, value);
    if (replacer != null) {
      Pair<String, String> keyValue = new Pair<>(key, value);
      keyValue = replacer.replace(keyValue, this);
      if (keyValue != null) {
        addKeyValue(keyValue.getKey(), keyValue.getValue());
      }
    } else {
      addKeyValue(key, value);
    }

  }

  protected String decodeKey(String key)
  {

    return decodeValue(key);
  }

  protected String decodeValue(String value)
  {
    char[] convtBuf = new char[value.length() * 2];
    String ret = PropertiesReadWriter.loadConvert(value.toCharArray(), 0, value.length(), convtBuf);
    ret = StringUtils.trim(ret);
    return ret;
  }

  @Override
  public void store(OutputStream out, String comments) throws RuntimeIOException
  {
    store(out, comments, charset, null);
  }

  @Override
  public void store(OutputStream out, String comments, KeyValueReplacer replacer) throws RuntimeIOException
  {
    store(out, comments, charset, replacer);
  }

  public void store(OutputStream out, String comments, Charset charset, KeyValueReplacer replacer)
      throws RuntimeIOException
  {
    OutputStreamWriter writer = new OutputStreamWriter(out, charset);
    store(writer, comments, replacer);
  }

  @Override
  public void store(Writer writer, String comments, KeyValueReplacer replacer) throws RuntimeIOException
  {
    super.store(writer, comments, replacer);
  }

  public Map<String, String> getOriginalMap()
  {
    return originalMap;
  }

  public List<Line> getOrginalLines()
  {
    return orginalLines;
  }

}
