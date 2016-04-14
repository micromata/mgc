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

/**
 * matches nothing, but is used to identify inactive derivation of e.g. rights/roles that can be selected for
 * sub-matchers in a hierarchy. In that case the following mappong is used: MatchPositive -> match in sub-matcher if not
 * -'ed there MatchNegative or NoMatch -> match in sub-matcher only if +'ed there
 *
 * @author jens@micromata.de
 * @param <T> the generic type
 */
public class HashmarkMatcher<T>extends NoneMatcher<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5434145883323585312L;

  /**
   * The enclosed matcher.
   */
  protected Matcher<T> enclosedMatcher;

  /**
   * Instantiates a new hashmark matcher.
   *
   * @param enclosedMatcher the enclosed matcher
   */
  public HashmarkMatcher(Matcher<T> enclosedMatcher)
  {
    this.enclosedMatcher = enclosedMatcher;
  }

  public Matcher<T> getEnclosedMatcher()
  {
    return enclosedMatcher;
  }

  @Override
  public boolean match(T object)
  {
    return false;
  }
}
