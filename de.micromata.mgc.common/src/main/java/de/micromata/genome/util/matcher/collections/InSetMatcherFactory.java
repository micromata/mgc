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

package de.micromata.genome.util.matcher.collections;

import java.util.Set;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;

/**
 * Creates a matching against a set.
 * 
 * @param <T> the generic type
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class InSetMatcherFactory<T> implements MatcherFactory<Set<T>>
{

  /**
   * Instantiates a new in set matcher factory.
   */
  public InSetMatcherFactory()
  {

  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.MatcherFactory#createMatcher(java.lang.String)
   */
  public Matcher<Set<T>> createMatcher(String pattern)
  {
    return new InSetMatcher<T>(pattern);
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.MatcherFactory#getRuleString(de.micromata.genome.util.matcher.Matcher)
   */
  public String getRuleString(Matcher<Set<T>> matcher)
  {
    return matcher.toString();
  }

}
