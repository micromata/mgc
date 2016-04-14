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

package de.micromata.genome.util.matcher.norm;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherBase;

/**
 * The Class NormalizedMatcher.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> only works with String
 * @see StringNormalizeUtils
 */
public class NormalizedMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -1951174536024791603L;

  /**
   * The flags.
   */
  private String flags;

  /**
   * The child.
   */
  private Matcher<T> child;

  /**
   * Instantiates a new normalized matcher.
   *
   * @param flags the flags
   * @param child the child
   */
  public NormalizedMatcher(String flags, Matcher<T> child)
  {
    this.flags = flags;
    this.child = child;
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.Matcher#match(java.lang.Object)
   */
  @Override
  public boolean match(T object)
  {
    if (object instanceof String) {
      String normobj = StringNormalizeUtils.normalize((String) object, flags);
      return child.match((T) normobj);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "<EXPR>.normalizedMatch(" + flags + ", " + child.toString() + ")";
  }

  public Matcher<T> getChild()
  {
    return child;
  }

  public String getFlags()
  {
    return flags;
  }

}
