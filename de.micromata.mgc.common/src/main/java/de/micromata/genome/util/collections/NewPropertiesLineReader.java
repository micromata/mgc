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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

import de.micromata.genome.util.runtime.RuntimeIOException;

/**
 * Parses property file and uses callback.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NewPropertiesLineReader
{
  public static interface LineReaderCallback
  {
    void onEmptyLine();

    void onComment(String comment);

    void onKeyValue(String key, String value);

    default void onEndOfFile()
    {
    }

    /**
     * Neither empty, nor comment, keyvalue.
     * 
     * @param line
     */
    default void onGarbage(String line)
    {
    }

  }

  protected LineReaderCallback callBack;
  protected int curPos = 0;
  protected char[] data;

  public NewPropertiesLineReader(LineReaderCallback callBack)
  {
    this.callBack = callBack;
  }

  public void read(InputStream is) throws RuntimeIOException
  {
    read(is, StandardCharsets.ISO_8859_1);
  }

  public void read(InputStream is, Charset charset) throws RuntimeIOException
  {
    try {
      byte[] cdata = IOUtils.toByteArray(is);
      String text = new String(cdata, charset);
      data = text.toCharArray();
      parse();
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  char peek(int lookup)
  {
    int newPos = curPos + lookup;
    if (newPos < 0) {
      return 0;
    }
    if (newPos >= data.length) {
      return 0;
    }
    return data[newPos];
  }

  boolean isNextPartOfNewLine()
  {
    if ((data[curPos] == '\n' && peek(1) == '\r') || (data[curPos] == '\r' && peek(1) == '\n')) {
      return true;
    }
    return false;
  }

  void consumeNl()
  {
    if (isNextPartOfNewLine() == true) {
      ++curPos;
    }
    ++curPos;
  }

  protected void parse()
  {
    while (curPos < data.length) {
      char c = data[curPos];
      switch (c) {
        case '\r':
        case '\n':
          consumeNl();
          callBack.onEmptyLine();
          break;
        case '#':
        case '!':
          ++curPos;
          consumeCommentLine();
          break;
        default:
          consumeKeyValueLine();
          break;
      }
    }
    callBack.onEndOfFile();
  }

  private void consumeKeyValueLine()
  {
    String key = parseKey();
    if (key == null) {
      return;
    }
    String value = parseValue();
    callBack.onKeyValue(key, value);

  }

  private String parseKey()
  {
    StringBuilder value = new StringBuilder();
    while (curPos < data.length) {
      char c = data[curPos];
      switch (c) {
        case '\r':
        case '\n':
          callBack.onGarbage(value.toString());
          consumeNl();
          return null;
        case '\\':
          ++curPos;
          continue;
        case '=':
          ++curPos;
          return value.toString();
        default:
          value.append(c);
          ++curPos;
          break;
      }
    }
    callBack.onGarbage(value.toString());
    return null;
  }

  private String parseValue()
  {
    StringBuilder value = new StringBuilder();
    while (curPos < data.length) {
      char c = data[curPos];
      switch (c) {
        case '\r':
        case '\n':
          consumeNl();
          return value.toString();
        case '\\':
          if (curPos + 1 < data.length) {
            ++curPos;
            char nc = data[curPos];
            switch (nc) {
              case 't':
                value.append('\t');
                break;
              case 'n':
                value.append('\n');
                break;
              case 'r':
                value.append('\r');
                break;
              default:
                value.append('\\');
                value.append(nc);
                break;
            }
          }
          ++curPos;
          continue;
        default:
          value.append(c);
          ++curPos;
          break;
      }
    }

    return value.toString();
  }

  private void consumeCommentLine()
  {
    int startComment = curPos;
    while (curPos < data.length) {
      char c = data[curPos];
      switch (c) {
        case '\r':
        case '\n':
          int endComment = curPos;
          consumeNl();
          callBack.onComment(new String(data, startComment, endComment - startComment));
          return;
        default:
          // nothing
          ++curPos;
      }
    }

    callBack.onComment(new String(data, startComment, curPos - startComment));
  }
}
