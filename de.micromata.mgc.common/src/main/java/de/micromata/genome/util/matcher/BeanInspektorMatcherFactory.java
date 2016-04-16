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

package de.micromata.genome.util.matcher;

import org.apache.commons.lang.StringUtils;

/**
 * A factory for creating BeanInspektorMatcher objects.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class BeanInspektorMatcherFactory implements MatcherFactory<Object>
{

  /**
   * Creates a new BeanInspektorMatcher object.
   *
   * @param pattern the pattern
   * @return the matcher
   */
  @Override
  public Matcher<Object> createMatcher(String pattern)
  {
    String matcherString = StringUtils.trim(StringUtils.substringBefore(pattern, "="));
    String valueString = StringUtils.trimToNull(StringUtils.substringAfter(pattern, "="));

    if (matcherString.trim().equals("instanceOf")) {
      try {
        // TODO (RK) wirklich nur von root classloader, nicht thread?
        return new BeanInstanceOfMatcher(Class.forName(valueString.trim()));
      } catch (Exception ex) {
        throw new RuntimeException(ex); // TODO better ex
      }
    }
    return new BeanPropertiesMatcher(matcherString, valueString);
  }

  /**
   * Gets the rule string.
   *
   * @param matcher the matcher
   * @return the rule string
   */
  @Override
  public String getRuleString(Matcher<Object> matcher)
  {
    if (matcher instanceof BeanInstanceOfMatcher) {
      return "instanceOf=" + ((BeanInstanceOfMatcher) matcher).getOfClass();
    }
    if (matcher instanceof BeanPropertiesMatcher) {
      BeanPropertiesMatcher bpm = ((BeanPropertiesMatcher) matcher);
      return bpm.getProperty() + "=" + bpm.getValue();
    }
    return "<unknown>";
  }
}
