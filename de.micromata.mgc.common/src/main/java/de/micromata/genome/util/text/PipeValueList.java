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

package de.micromata.genome.util.text;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Die PipeValueList verwendet | für den Trenner von einem Key-Value-Paar. Key und Value werden mittels = getrennt. Wenn
 * | oder = im Wert verwendet werden soll, muss das Zeichen durch ein vorangestelltes \ maskiert. Ein \ im Wert wird
 * ebenfalls mit einem vorangestellten \ maskiert.
 * 
 * @author roger
 * 
 */
public class PipeValueList
{

  /**
   * Escape a key or value.
   *
   * @param k the k
   * @return the string
   */
  public static String escape(String k)
  {
    k = StringUtils.replace(k, "\\", "\\\\");
    k = StringUtils.replace(k, "|", "\\|");
    k = StringUtils.replace(k, "=", "\\=");
    return k;

  }

  /**
   * Encode.
   *
   * @param map the map
   * @return the string
   */
  public static String encode(Map<String, String> map)
  {
    if (map == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    for (Map.Entry<String, String> me : map.entrySet()) {
      if (isFirst == false) {
        sb.append("|");
      }
      isFirst = false;
      String k = me.getKey();
      k = escape(k);
      sb.append(k).append("=");
      String v = me.getValue();
      v = escape(v);
      sb.append(v);
    }
    return sb.toString();
  }

  /**
   * The Enum State.
   */
  private static enum State
  {

    /**
     * The Parse key.
     */
    ParseKey,
    /**
     * The Parse value.
     */
    ParseValue,
  };

  /**
   * Trim.
   *
   * @param text the text
   * @return the string
   */
  protected static String trim(String text)
  {
    if (text.length() < 3) {
      return text;
    }

    String ttext = StringUtils.trim(text);

    if (ttext.length() > 0 && ttext.endsWith("\\")) {
      return text;
    }

    return ttext;
    // }
    // if (text.charAt(text.length() - 2) != '\\') {// falls nicht am Ende escaped wurde, timmen
    // text = StringUtils.trim(text);
    // } else {
    // text = StringUtils.substring(text, 0, text.length() - 2) + StringUtils.substring(text, text.length() - 1, text.length()); // fallls.
    // escaped
    // wurde,
    // escape
    // zeichen
    // verwerfen
    // }

    // return text;
  }

  /**
   * der Text wird getrimmt. Es sei denn, an letzter Stelle ist ein escapetes(mit Backsclash) Zeichen.
   * 
   * aus "a=b|c=d\\\n" wird a=b|c=d\n und aus "a=b|c=d\n" wird a=b|c=d
   *
   * @param text the text
   * @return the map
   */
  public static Map<String, String> decode(String text)
  {
    Map<String, String> map = new HashMap<String, String>();
    if (StringUtils.isEmpty(text) == true) {
      return map;
    }

    text = trim(text);

    List<String> tlist = TextSplitterUtils.parseStringTokens(text, "|\\=", true);
    StringBuilder sb = new StringBuilder();
    String curKey = null;
    String curValue = null;
    State state = State.ParseKey;
    for (int i = 0; i < tlist.size(); ++i) {
      String t = tlist.get(i);
      if ("null".equals(t)) {
        //skip because null
      } else if ("\\".equals(t) == true) {
        ++i;
        t = tlist.get(i);
        sb.append(t);
        continue;
      } else if ("=".equals(t) == true) {
        if (state != State.ParseKey) {
          throw new IllegalStateException("Parsing '=' in state: " + state);
        }
        curKey = sb.toString();
        sb = new StringBuilder();
        state = State.ParseValue;
      } else if ("|".equals(t) == true) {
        if (state != State.ParseValue) {
          throw new IllegalStateException("Parsing '=' in state: " + state);
        }
        curValue = sb.toString();
        map.put(curKey, curValue);
        sb = new StringBuilder();
        curKey = null;
        curValue = null;
        state = State.ParseKey;
      } else {
        sb.append(t);
      }
    }
    if (curKey != null) {
      curValue = sb.toString();
      map.put(curKey, StringUtils.defaultString(curValue));
    }
    return map;
  }

}
