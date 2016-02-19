package de.micromata.genome.util.collections;

import java.io.BufferedWriter;
import java.io.IOException;

import de.micromata.genome.util.runtime.RuntimeIOException;

/**
 * Methods to read/write properties
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class PropertiesReadWriter
{
  /**
   * Load convert.
   *
   * @param in the in
   * @param off the off
   * @param len the len
   * @param convtBuf the convt buf
   * @return the string
   */
  /*
   * Converts encoded &#92;uxxxx to unicode chars and changes special saved chars to their original forms
   */
  public static String loadConvert(char[] in, int off, int len, char[] convtBuf)
  {
    if (convtBuf.length < len) {
      int newLen = len * 2;
      if (newLen < 0) {
        newLen = Integer.MAX_VALUE;
      }
      convtBuf = new char[newLen];
    }
    char aChar;
    char[] out = convtBuf;
    int outLen = 0;
    int end = off + len;

    while (off < end) {
      aChar = in[off++];
      if (aChar == '\\') {
        aChar = in[off++];
        if (aChar == 'u') {
          // Read the xxxx
          int value = 0;
          for (int i = 0; i < 4; i++) {
            aChar = in[off++];
            switch (aChar) {
              case '0':
              case '1':
              case '2':
              case '3':
              case '4':
              case '5':
              case '6':
              case '7':
              case '8':
              case '9':
                value = (value << 4) + aChar - '0';
                break;
              case 'a':
              case 'b':
              case 'c':
              case 'd':
              case 'e':
              case 'f':
                value = (value << 4) + 10 + aChar - 'a';
                break;
              case 'A':
              case 'B':
              case 'C':
              case 'D':
              case 'E':
              case 'F':
                value = (value << 4) + 10 + aChar - 'A';
                break;
              default:
                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
            }
          }
          out[outLen++] = (char) value;
        } else {
          if (aChar == 't') {
            aChar = '\t';
          } else if (aChar == 'r') {
            aChar = '\r';
          } else if (aChar == 'n') {
            aChar = '\n';
          } else if (aChar == 'f') {
            aChar = '\f';
          }
          out[outLen++] = aChar;
        }
      } else {
        out[outLen++] = aChar;
      }
    }
    return new String(out, 0, outLen);
  }

  /**
   * Save convert.
   *
   * @param theString the the string
   * @param escapeSpace the escape space
   * @param escapeUnicode the escape unicode
   * @return the string
   */
  /*
   * Converts unicodes to encoded &#92;uxxxx and escapes special characters with a preceding slash
   */
  public static String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode, boolean escapeForKey)
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
        case '#': // Fall through
        case '!':
          if (escapeForKey == true) {
            outBuffer.append('\\');
            outBuffer.append(aChar);
          } else {
            outBuffer.append(aChar);
          }
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
   * Write comments.
   *
   * @param bw the bw
   * @param comments the comments
   * @throws RuntimeIOException the runtime io exception
   */
  public static void writeComments(BufferedWriter bw, String comments)
      throws RuntimeIOException
  {
    try {
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
            if (c == '\r' &&
                current != len - 1 &&
                comments.charAt(current + 1) == '\n') {
              current++;
            }
            if (current == len - 1 ||
                (comments.charAt(current + 1) != '#' &&
                    comments.charAt(current + 1) != '!')) {
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
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
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
  private static final char[] hexDigit = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };

}
