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

/**
 * Matches if string is containing.
 *
 * @author roger
 * @param <T> the generic type
 */
public class ContainsMatcher<T>extends StringPatternMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6751376242568909350L;

  /**
   * Instantiates a new contains matcher.
   */
  public ContainsMatcher()
  {

  }

  /**
   * Instantiates a new contains matcher.
   *
   * @param pattern the pattern
   */
  public ContainsMatcher(String pattern)
  {
    super(pattern);
  }

  @Override
  public boolean matchString(String token)
  {
    return token.indexOf(pattern) != -1;
  }

  @Override
  public String toString()
  {
    return "<EXPR>.contains(" + pattern + ")";
  }
}
