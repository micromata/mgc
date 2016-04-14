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

/**
 * Adapter class for StringUtils
 * 
 * @author roger
 * 
 */
@Deprecated
public class StringUtils extends org.apache.commons.lang.StringUtils
{
  /**
   * This was removed in later commons.lang
   * 
   * @param text
   * @param start
   * @return
   */
  public static boolean startsWith(String text, String start)
  {
    if (text == null) {
      return false;
    }
    return text.startsWith(start);
  }
}
