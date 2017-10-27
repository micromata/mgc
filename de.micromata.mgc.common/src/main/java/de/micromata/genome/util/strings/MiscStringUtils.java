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

package de.micromata.genome.util.strings;

import org.apache.commons.lang3.StringUtils;

/**
 * Collection of String utilities
 * 
 * @author roger
 * 
 */
public class MiscStringUtils
{
  /**
   * Shorten a String from the left side
   * 
   * @param s
   * @param maxLength
   * @return
   */
  public static String cutLeft(String s, int maxLength)
  {
    if (s == null)
      return "";
    if (s.length() <= maxLength)
      return s;
    return s.substring(s.length() - maxLength);
  }

  /**
   * Shorten a String from the right side
   * 
   * @param s
   * @param maxLength
   * @return
   */
  public static String cutRight(String s, int maxLength)
  {
    return StringUtils.substring(s, 0, maxLength);
  }

}
