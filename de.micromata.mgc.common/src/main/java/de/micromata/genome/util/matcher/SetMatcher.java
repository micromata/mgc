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

import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * Matches agains a set via contains.
 *
 * @author roger@micromata.de
 * @param <T> the generic type
 */
public class SetMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -870906355711283761L;

  /**
   * The set.
   */
  private Set<T> set;

  /**
   * Instantiates a new sets the matcher.
   *
   * @param set the set
   */
  public SetMatcher(Set<T> set)
  {
    Validate.notNull(set);
    this.set = set;
  }

  @Override
  public boolean match(T object)
  {
    return set.contains(object);
  }

  @Override
  public String toString()
  {
    return "inSet(" + set + ")";
  }

}
