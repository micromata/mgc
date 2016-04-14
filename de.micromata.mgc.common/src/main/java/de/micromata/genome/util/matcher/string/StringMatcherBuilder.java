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

package de.micromata.genome.util.matcher.string;

import de.micromata.genome.util.matcher.Matcher;

/**
 * Matcher builder for string based matcher.
 * 
 * @author roger@micromata.de
 * 
 */
public class StringMatcherBuilder
{

  /**
   * Contains.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> contains(String text)
  {
    return new ContainsMatcher<String>(text);
  }

  /**
   * Starts with.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> startsWith(String text)
  {
    return new StartWithMatcher<String>(text);
  }

  /**
   * Ends with.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> endsWith(String text)
  {
    return new EndsWithMatcher<String>(text);
  }

  /**
   * Wildcart.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> wildcart(String text)
  {
    return new WildcardMatcher<String>(text);
  }

  /**
   * Regexp.
   *
   * @param text the text
   * @return the matcher
   */
  public static Matcher<String> regexp(String text)
  {
    return new RegExpMatcher<String>(text);
  }
}
