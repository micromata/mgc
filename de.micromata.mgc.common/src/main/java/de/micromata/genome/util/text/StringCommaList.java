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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility to encode a List of string. If the one part of the list contains , it will be escpaped by backslash
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class StringCommaList
{

  /**
   * Encode.
   * 
   * @param parts the parts
   * @return the string
   */
  public static String encodeStringArray(String[] parts)
  {
    return join(parts, ',');
  }

  /**
   * Decode.
   * 
   * @param merged the merged
   * @return the string[]
   */
  public static String[] decodeStringArray(String merged)
  {
    return split(merged, ',');
  }

  /**
   * Encode basic array.
   * 
   * @param <T> the generic type
   * @param parts the parts
   * @return the string
   */
  public static <T> String encodeBasicArray(T[] parts)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < parts.length; ++i) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append(parts[i].toString());
    }
    return sb.toString();
  }

  /**
   * Encode long array.
   * 
   * @param parts the parts none must be null
   * @return the string
   */
  public static String encodeLongArray(Long[] parts)
  {
    return encodeBasicArray(parts);
  }

  /**
   * Decode long array.
   * 
   * @param merged the merged
   * @return the long[]
   */
  public static Long[] decodeLongArray(String merged)
  {
    String[] splitted = StringUtils.split(merged, ',');
    Long[] ret = new Long[splitted.length];
    for (int i = 0; i < splitted.length; ++i) {
      ret[i] = Long.parseLong(splitted[i]);
    }
    return ret;
  }

  /**
   * Escape.
   * 
   * @param s the s
   * @param sep the sep
   * @return the string
   */
  public static String escape(String s, char sep)
  {
    if (s.indexOf(sep) == -1 && s.indexOf('\\') == -1) {
      return s;
    }
    StringBuilder ret = new StringBuilder();
    for (int i = 0; i < s.length(); ++i) {
      char c = s.charAt(i);
      if (c == sep || c == '\\') {
        ret.append("\\");
      }
      ret.append(c);
    }
    return ret.toString();
  }

  /**
   * Unescape.
   * 
   * @param s the s
   * @param sep the sep
   * @return the string
   */
  public static String unescape(String s, char sep)
  {
    if (s.indexOf(sep) == -1) {
      return s;
    }
    StringBuilder ret = new StringBuilder();
    for (int i = 0; i < s.length(); ++i) {
      char c = s.charAt(i);
      if (c == '\\') {
        ++i;
        if (i < s.length()) {
          ret.append(s.charAt(i));
        }
        continue;
      }
      ret.append(c);
    }
    return ret.toString();
  }

  /**
   * Join.
   * 
   * @param parts the parts
   * @param sep the sep
   * @return the string
   */
  public static String join(String[] parts, char sep)
  {
    StringBuilder sb = new StringBuilder();
    for (String p : parts) {
      if (sb.length() > 0) {
        sb.append(sep);
      }
      sb.append(escape(p, sep));
    }
    return sb.toString();
  }

  /**
   * Split.
   * 
   * @param merged the merged
   * @param sep the sep
   * @return the string[]
   */
  public static String[] split(String merged, char sep)
  {
    if (merged.indexOf(sep) == -1) {
      if (merged.isEmpty() == true) {
        return new String[] {};
      }
      return new String[] { merged};
    }
    List<String> ret = new ArrayList<String>();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < merged.length(); ++i) {
      char c = merged.charAt(i);
      if (c == '\\') {
        ++i;
        if (i < merged.length()) {
          sb.append(merged.charAt(i));
        }
        continue;
      }
      if (c == sep) {
        ret.add(sb.toString());
        sb = new StringBuilder();
        continue;
      }
      sb.append(c);
    }
    if (sb.length() > 0) {
      ret.add(sb.toString());
    }
    return ret.toArray(new String[] {});
  }
}
