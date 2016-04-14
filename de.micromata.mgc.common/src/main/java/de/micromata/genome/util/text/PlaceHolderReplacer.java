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

import java.util.Map;

/**
 * a Replacer. Normally resolveReplaceDollarVars will be used.
 *
 * @author roger
 *
 */
public class PlaceHolderReplacer
{

  /**
   * Resolve replace.
   *
   * @param text the text
   * @param start the start
   * @param end the end
   * @param resolver the resolver
   * @return the string
   */
  public static String resolveReplace(String text, String start, String end, StringResolver resolver)
  {
    if (text == null) {
      return text;
    }
    int idx = text.indexOf(start);
    if (idx == -1) {
      return text;
    }
    int eidx = 0;
    StringBuilder sb = new StringBuilder();
    do {
      int neidx = text.indexOf(end, idx + start.length());
      if (neidx == -1) {
        break;
      }
      String k = text.substring(idx + start.length(), neidx);

      sb.append(text.substring(eidx, idx));
      String resolved = resolver.resolve(k);
      if (resolved == null) {
        return resolved;
      }
      sb.append(resolved);
      eidx = neidx + end.length();
      idx = text.indexOf(start, eidx);
    } while (idx != -1);
    String sub = text.substring(eidx);
    sb.append(sub);
    return sb.toString();
  }

  /**
   * replaces ${} variables with map content.
   *
   * @param text the text
   * @param context the context
   * @return the string
   */
  public static String resolveReplaceDollarVars(String text, final Map<String, String> context)
  {
    return resolveReplace(text, "${", "}", new StringResolver()
    {

      @Override
      public String resolve(String placeholder)
      {
        return context.get(placeholder);
      }
    });
  }

  /**
   * replaces ${} variables with map content.
   * 
   * If a paceholder is found, try to resolve also the value in the context
   *
   * @param text the text
   * @param context the context
   * @return the string
   */
  public static String resolveRecursiveReplaceDollarVars(String text, final Map<String, String> context)
  {
    return resolveReplace(text, "${", "}", new StringResolver()
    {

      @Override
      public String resolve(String placeholder)
      {
        return resolveRecursiveReplaceDollarVars(context.get(placeholder), context);
      }
    });
  }
}
