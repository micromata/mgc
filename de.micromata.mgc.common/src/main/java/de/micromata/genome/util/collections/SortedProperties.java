/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    noodles@micromata.de
// Created   27.07.2007
// Copyright Micromata 27.07.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.collections;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Die <code>store()</code>-Methode speichert die Einträge in alphabetischer Reihenfolge ab. Hat auch die moeglichkeit,
 * dass im VAlue := nicht escaped wird
 * 
 * @author noodles@micromata.de, roger@microamta.de
 */
public class SortedProperties extends Properties
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6788379042847551459L;

  /**
   * Do not escape character, which are really not needed to escape when reading.
   */
  private boolean storeUnescapedUnimportant = true;

  /**
   * Instantiates a new sorted properties.
   */
  public SortedProperties()
  {
    super();
  }

  /**
   * Instantiates a new sorted properties.
   *
   * @param defaults the defaults
   */
  public SortedProperties(Properties defaults)
  {
    super(defaults);

  }

  /**
   * Zwei Objekte anhand toString-Value vergleichen.
   *
   * @author noodles@micromata.de
   * @param <T> the generic type
   */
  private static class ToStringComparator<T extends Object>implements Comparator<T>
  {

    @Override
    public int compare(T l, T r)
    {
      String left = (l != null) ? "" + l : "";
      String right = (r != null) ? "" + r : "";

      final int result = left.compareToIgnoreCase(right);
      return result;
    }

  }

  @Override
  public synchronized Enumeration<Object> keys()
  {
    final Set<Object> keyset = super.keySet();
    final List<Object> list = new ArrayList<Object>(keyset);

    Collections.sort(list, new ToStringComparator<Object>());
    Enumeration<Object> result = Collections.enumeration(list);

    return result;
  }

  @Override
  public void store(OutputStream out, String comments) throws IOException
  {
    if (storeUnescapedUnimportant == false) {
      super.store(out, comments);
    } else {
      storeInternal(new BufferedWriter(new OutputStreamWriter(out, "8859_1")), comments, true);
    }
  }

  /**
   * Store internal.
   *
   * @param bw the bw
   * @param comments the comments
   * @param escUnicode the esc unicode
   * @throws IOException Signals that an I/O exception has occurred.
   */
  protected void storeInternal(BufferedWriter bw, String comments, boolean escUnicode) throws IOException
  {
    if (comments != null) {
      writeComments(bw, comments);
    }
    bw.write("#" + new Date().toString());
    bw.newLine();
    synchronized (this) {
      for (Enumeration e = keys(); e.hasMoreElements();) {
        String key = (String) e.nextElement();
        String val = (String) get(key);
        key = saveConvert(key, true, escUnicode, true);
        /*
         * No need to escape embedded and trailing spaces for value, hence pass false to flag.
         */
        val = saveConvert(val, false, escUnicode, false);
        bw.write(key + "=" + val);
        bw.newLine();
      }
    }
    bw.flush();
  }

  /**
   * Write comments.
   *
   * @param bw the bw
   * @param comments the comments
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static void writeComments(BufferedWriter bw, String comments) throws IOException
  {
    bw.write("#");
    int len = comments.length();
    int current = 0;
    int last = 0;
    char[] uu = new char[6];
    uu[0] = '\\';
    uu[1] = 'u';
    while (current < len) {
      char c = comments.charAt(current);
      if (c > '\u00ff' || c == '\n' || c == '\r') {
        if (last != current) {
          bw.write(comments.substring(last, current));
        }
        if (c > '\u00ff') {
          uu[2] = toHex((c >> 12) & 0xf);
          uu[3] = toHex((c >> 8) & 0xf);
          uu[4] = toHex((c >> 4) & 0xf);
          uu[5] = toHex(c & 0xf);
          bw.write(new String(uu));
        } else {
          bw.newLine();
          if (c == '\r' && current != len - 1 && comments.charAt(current + 1) == '\n') {
            current++;
          }
          if (current == len - 1 || (comments.charAt(current + 1) != '#' && comments.charAt(current + 1) != '!')) {
            bw.write("#");
          }
        }
        last = current + 1;
      }
      current++;
    }
    if (last != current) {
      bw.write(comments.substring(last, current));
    }
    bw.newLine();
  }

  /**
   * Save convert.
   *
   * @param theString the the string
   * @param escapeSpace the escape space
   * @param escapeUnicode the escape unicode
   * @param forKey the for key
   * @return the string
   */
  /*
   * Converts unicodes to encoded &#92;uxxxx and escapes special characters with a preceding slash
   */
  private String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode, boolean forKey)
  {
    int len = theString.length();
    int bufLen = len * 2;
    if (bufLen < 0) {
      bufLen = Integer.MAX_VALUE;
    }
    StringBuffer outBuffer = new StringBuffer(bufLen);

    for (int x = 0; x < len; x++) {
      char aChar = theString.charAt(x);
      // Handle common case first, selecting largest block that
      // avoids the specials below
      if ((aChar > 61) && (aChar < 127)) {
        if (aChar == '\\') {
          outBuffer.append('\\');
          outBuffer.append('\\');
          continue;
        }
        outBuffer.append(aChar);
        continue;
      }
      switch (aChar) {
        case ' ':
          if (x == 0 || escapeSpace) {
            outBuffer.append('\\');
          }
          outBuffer.append(' ');
          break;
        case '\t':
          outBuffer.append('\\');
          outBuffer.append('t');
          break;
        case '\n':
          outBuffer.append('\\');
          outBuffer.append('n');
          break;
        case '\r':
          outBuffer.append('\\');
          outBuffer.append('r');
          break;
        case '\f':
          outBuffer.append('\\');
          outBuffer.append('f');
          break;

        case '=': // Fall through
        case ':': // Fall through
          if (forKey == false) {
            // GEnome specific:
            outBuffer.append(aChar);
            break;
          }
          // otherwise fall through
        case '#': // Fall through
        case '!':
          outBuffer.append('\\');
          outBuffer.append(aChar);
          break;
        default:
          if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
            outBuffer.append('\\');
            outBuffer.append('u');
            outBuffer.append(toHex((aChar >> 12) & 0xF));
            outBuffer.append(toHex((aChar >> 8) & 0xF));
            outBuffer.append(toHex((aChar >> 4) & 0xF));
            outBuffer.append(toHex(aChar & 0xF));
          } else {
            outBuffer.append(aChar);
          }
      }
    }
    return outBuffer.toString();
  }

  /**
   * Convert a nibble to a hex character.
   *
   * @param nibble the nibble to convert.
   * @return the char
   */
  private static char toHex(int nibble)
  {
    return hexDigit[(nibble & 0xF)];
  }

  /**
   * A table of hex digits.
   */
  private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
      'F' };

  public boolean isStoreUnescapedUnimportant()
  {
    return storeUnescapedUnimportant;
  }

  public void setStoreUnescapedUnimportant(boolean storeUnescapedUnimportant)
  {
    this.storeUnescapedUnimportant = storeUnescapedUnimportant;
  }

}
