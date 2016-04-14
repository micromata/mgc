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

import de.micromata.genome.util.matcher.string.ContainsMatcher;
import de.micromata.genome.util.matcher.string.EndsWithMatcher;
import de.micromata.genome.util.matcher.string.RegExpMatcher;
import de.micromata.genome.util.matcher.string.StartWithMatcher;
import de.micromata.genome.util.matcher.string.WildcardMatcher;

/**
 * String based matchers.
 * 
 * @author roger@micromata.de
 */

public class StringMatchers extends CommonMatchers
{

  /**
   * The Constant DEFAULT_LOGICALRULESFACTORY.
   */
  private static final MatcherFactory<String> DEFAULT_LOGICALRULESFACTORY = new LogicalMatcherFactory<String>();

  /**
   * Contains string.
   *
   * @param obj the obj
   * @return the matcher
   */
  public static Matcher<String> containsString(String obj)
  {
    return new ContainsMatcher<String>(obj);
  }

  /**
   * Starts with.
   *
   * @param o the o
   * @return the matcher
   */
  public static Matcher<String> startsWith(String o)
  {
    return new StartWithMatcher<String>(o);
  }

  /**
   * Ends with.
   *
   * @param o the o
   * @return the matcher
   */
  public static Matcher<String> endsWith(String o)
  {
    return new EndsWithMatcher<String>(o);
  }

  /**
   * Reqexp.
   *
   * @param obj the obj
   * @return the matcher
   */
  public static Matcher<String> reqexp(String obj)
  {
    return new RegExpMatcher<String>(obj);
  }

  /**
   * Wildcart.
   *
   * @param obj the obj
   * @return the matcher
   */
  public static Matcher<String> wildcart(String obj)
  {
    return new WildcardMatcher<String>(obj);
  }

  /**
   * Logical list match.
   *
   * @param expression the expression
   * @return the matcher
   */
  public static Matcher<String> logicalListMatch(String expression)
  {
    return DEFAULT_LOGICALRULESFACTORY.createMatcher(expression);
  }
}
