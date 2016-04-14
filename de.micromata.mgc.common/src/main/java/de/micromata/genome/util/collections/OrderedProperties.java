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

package de.micromata.genome.util.collections;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections15.map.ListOrderedMap;

import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.Pair;

/**
 * An ordered properties file.
 * 
 * The keys are ordered in the same order, like it is in the properties file.
 * 
 * This implementation is not synchronized.
 * 
 * With KeyValueReplacer implementation, key/values can be replaces while loading.
 * 
 * in store and load only RuntimeIOException will be thrown.
 * 
 * @author Roger Kommer (roger.kommer.extern@micromata.de)
 * 
 */
public class OrderedProperties extends ListOrderedMap<String, String>
{
  /**
   * Replacer, which works while load/store.
   * 
   * @author Roger Kommer (roger.kommer.extern@micromata.de)
   * 
   */
  public static interface KeyValueReplacer
  {

    /**
     * Replace.
     * 
     * @param keyValue the key value
     * @param properties the properties
     * @return if null, will not transfer to file/map.
     */
    Pair<String, String> replace(Pair<String, String> keyValue, Map<String, String> target);
  }

  /**
   * Instantiates a new ordered properties.
   */
  public OrderedProperties()
  {

  }

  /**
   * Reads a property list (key and element pairs) from the input byte stream. The input stream is in a simple
   * line-oriented format as specified in {@link #load(java.io.Reader) load(Reader)} and is assumed to use the ISO
   * 8859-1 character encoding; that is each byte is one Latin1 character. Characters not in Latin1, and certain special
   * characters, are represented in keys and elements using Unicode escapes as defined in section 3.3 of <cite>The
   * Java&trade; Language Specification</cite>.
   * <p>
   * The specified stream remains open after this method returns.
   *
   * @param inStream the input stream.
   * @throws RuntimeIOException the runtime io exception
   * @throws IllegalArgumentException if the input stream contains a malformed Unicode escape sequence.
   * @since 1.2
   */
  public void load(InputStream inStream) throws RuntimeIOException
  {
    load0(new OldPropertiesLineReader(inStream), null);
  }

  /**
   * Load.
   *
   * @param inStream the in stream
   * @param replacer the replacer
   * @throws RuntimeIOException the runtime io exception
   */
  public void load(InputStream inStream, KeyValueReplacer replacer) throws RuntimeIOException
  {
    load0(new OldPropertiesLineReader(inStream), replacer);
  }

  /**
   * Load0.
   *
   * @param lr the lr
   * @param replacer the replacer
   * @throws RuntimeIOException the runtime io exception
   */
  private void load0(OldPropertiesLineReader lr, KeyValueReplacer replacer) throws RuntimeIOException
  {
    char[] convtBuf = new char[1024];
    int limit;
    int keyLen;
    int valueStart;
    char c;
    boolean hasSep;
    boolean precedingBackslash;
    try {
      while ((limit = lr.readLine()) >= 0) {
        c = 0;
        keyLen = 0;
        valueStart = limit;
        hasSep = false;

        precedingBackslash = false;
        while (keyLen < limit) {
          c = lr.lineBuf[keyLen];
          //need check if escaped.
          if ((c == '=' || c == ':') && !precedingBackslash) {
            valueStart = keyLen + 1;
            hasSep = true;
            break;
          } else if ((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash) {
            valueStart = keyLen + 1;
            break;
          }
          if (c == '\\') {
            precedingBackslash = !precedingBackslash;
          } else {
            precedingBackslash = false;
          }
          keyLen++;
        }
        while (valueStart < limit) {
          c = lr.lineBuf[valueStart];
          if (c != ' ' && c != '\t' && c != '\f') {
            if (!hasSep && (c == '=' || c == ':')) {
              hasSep = true;
            } else {
              break;
            }
          }
          valueStart++;
        }
        String key = loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
        String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);

        Pair<String, String> p = Pair.make(key, value);
        addKeyValue(p, replacer);

      }
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  protected void addKeyValue(Pair<String, String> keyValue, KeyValueReplacer replacer)
  {
    if (replacer != null) {
      keyValue = replacer.replace(keyValue, this);
    }
    if (keyValue != null) {
      addKeyValue(keyValue.getKey(), keyValue.getValue());
    }
  }

  protected void addKeyValue(String key, String value)
  {
    put(key, value);
  }

  /*
   * Converts encoded &#92;uxxxx to unicode chars and changes special saved chars to their original forms
   */
  protected String loadConvert(char[] in, int off, int len, char[] convtBuf)
  {
    return PropertiesReadWriter.loadConvert(in, off, len, convtBuf);
  }

  /**
   * Writes this property list (key and element pairs) in this <code>Properties</code> table to the output character
   * stream in a format suitable for using the {@link #load(java.io.Reader) load(Reader)} method.
   * <p>
   * Properties from the defaults table of this <code>Properties</code> table (if any) are <i>not</i> written out by
   * this method.
   * <p>
   * If the comments argument is not null, then an ASCII <code>#</code> character, the comments string, and a line
   * separator are first written to the output stream. Thus, the <code>comments</code> can serve as an identifying
   * comment. Any one of a line feed ('\n'), a carriage return ('\r'), or a carriage return followed immediately by a
   * line feed in comments is replaced by a line separator generated by the <code>Writer</code> and if the next
   * character in comments is not character <code>#</code> or character <code>!</code> then an ASCII <code>#</code> is
   * written out after that line separator.
   * <p>
   * Next, a comment line is always written, consisting of an ASCII <code>#</code> character, the current date and time
   * (as if produced by the <code>toString</code> method of <code>Date</code> for the current time), and a line
   * separator as generated by the <code>Writer</code>.
   * <p>
   * Then every entry in this <code>Properties</code> table is written out, one per line. For each entry the key string
   * is written, then an ASCII <code>=</code>, then the associated element string. For the key, all space characters are
   * written with a preceding <code>\</code> character. For the element, leading space characters, but not embedded or
   * trailing space characters, are written with a preceding <code>\</code> character. The key and element characters
   * <code>#</code>, <code>!</code>, <code>=</code>, and <code>:</code> are written with a preceding backslash to ensure
   * that they are properly loaded.
   * <p>
   * After the entries have been written, the output stream is flushed. The output stream remains open after this method
   * returns.
   * <p>
   *
   * @param writer an output character stream writer.
   * @param comments a description of the property list.
   * @throws RuntimeIOException the runtime io exception
   * @since 1.6
   * @exception ClassCastException if this <code>Properties</code> object contains any keys or values that are not
   *              <code>Strings</code>.
   * @exception NullPointerException if <code>writer</code> is null.
   */
  public void store(Writer writer, String comments)
      throws RuntimeIOException
  {
    store0((writer instanceof BufferedWriter) ? (BufferedWriter) writer
        : new BufferedWriter(writer),
        comments, false, null);
  }

  /**
   * Store.
   *
   * @param writer the writer
   * @param comments the comments
   * @param replacer the replacer
   * @throws RuntimeIOException the runtime io exception
   */
  public void store(Writer writer, String comments, KeyValueReplacer replacer)
      throws RuntimeIOException
  {
    store0((writer instanceof BufferedWriter) ? (BufferedWriter) writer
        : new BufferedWriter(writer),
        comments, false, replacer);
  }

  /**
   * Writes this property list (key and element pairs) in this <code>Properties</code> table to the output stream in a
   * format suitable for loading into a <code>Properties</code> table using the {@link #load(InputStream)
   * load(InputStream)} method.
   * <p>
   * Properties from the defaults table of this <code>Properties</code> table (if any) are <i>not</i> written out by
   * this method.
   * <p>
   * This method outputs the comments, properties keys and values in the same format as specified in
   * {@link #store(java.io.Writer, java.lang.String) store(Writer)}, with the following differences:
   * <ul>
   * <li>The stream is written using the ISO 8859-1 character encoding.
   * 
   * <li>Characters not in Latin-1 in the comments are written as <code>&#92;u</code><i>xxxx</i> for their appropriate
   * unicode hexadecimal value <i>xxxx</i>.
   * 
   * <li>Characters less than <code>&#92;u0020</code> and characters greater than <code>&#92;u007E</code> in property
   * keys or values are written as <code>&#92;u</code><i>xxxx</i> for the appropriate hexadecimal value <i>xxxx</i>.
   * </ul>
   * <p>
   * After the entries have been written, the output stream is flushed. The output stream remains open after this method
   * returns.
   * <p>
   *
   * @param out an output stream.
   * @param comments a description of the property list.
   * @throws RuntimeIOException the runtime io exception
   * @since 1.2
   * @exception ClassCastException if this <code>Properties</code> object contains any keys or values that are not
   *              <code>Strings</code>.
   * @exception NullPointerException if <code>out</code> is null.
   */
  public void store(OutputStream out, String comments)
      throws RuntimeIOException
  {
    try {
      store0(new BufferedWriter(new OutputStreamWriter(out, "8859_1")),
          comments, true, null);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  /**
   * Store.
   *
   * @param out the out
   * @param comments the comments
   * @param replacer the replacer
   * @throws RuntimeIOException the runtime io exception
   */
  public void store(OutputStream out, String comments, KeyValueReplacer replacer)
      throws RuntimeIOException
  {
    try {
      store0(new BufferedWriter(new OutputStreamWriter(out, "8859_1")),
          comments, true, replacer);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  /**
   * Store0.
   *
   * @param bw the bw
   * @param comments the comments
   * @param escUnicode the esc unicode
   * @param replacer the replacer
   * @throws RuntimeIOException the runtime io exception
   */
  private void store0(BufferedWriter bw, String comments, boolean escUnicode, KeyValueReplacer replacer)
      throws RuntimeIOException
  {
    if (comments != null) {
      PropertiesReadWriter.writeComments(bw, comments);
    }
    try {
      bw.write("#" + new Date().toString());
      bw.newLine();
      for (Map.Entry<String, String> me : entrySet()) {
        String key = me.getKey();
        String val = me.getValue();
        if (replacer != null) {
          Pair<String, String> p = replacer.replace(Pair.make(key, val), this);
          if (p == null) {
            continue;
          }
          key = p.getKey();
          val = p.getValue();
        }
        key = PropertiesReadWriter.saveConvert(key, true, escUnicode, true);
        /*
         * No need to escape embedded and trailing spaces for value, hence pass false to flag.
         */
        val = PropertiesReadWriter.saveConvert(val, false, escUnicode, false);
        bw.write(key + "=" + val);
        bw.newLine();
      }
      bw.flush();
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

}
