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

import java.util.regex.Pattern;

/**
 * Matches if regular expression pattern matches.
 *
 * @author roger
 * @param <T> the generic type
 */
public class RegExpMatcher<T>extends StringMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -4826993544156110602L;

  /**
   * The pattern.
   */
  private Pattern pattern;

  /**
   * Instantiates a new reg exp matcher.
   */
  public RegExpMatcher()
  {
  }

  /**
   * Instantiates a new reg exp matcher.
   *
   * @param pattern the pattern
   */
  public RegExpMatcher(Pattern pattern)
  {
    this.pattern = pattern;

  }

  /**
   * Instantiates a new reg exp matcher.
   *
   * @param pattern the pattern
   */
  public RegExpMatcher(String pattern)
  {
    this.pattern = Pattern.compile(pattern);
  }

  @Override
  public boolean matchString(String token)
  {
    return pattern.matcher(token).matches();
  }

  @Override
  public String toString()
  {
    return "<EXPR>.regexp(" + pattern.toString() + ")";
  }

  public Pattern getPattern()
  {
    return pattern;
  }

  public void setPattern(Pattern pattern)
  {
    this.pattern = pattern;
  }

}
