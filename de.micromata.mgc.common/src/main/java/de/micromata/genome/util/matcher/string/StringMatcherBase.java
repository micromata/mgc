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

import de.micromata.genome.util.matcher.MatcherBase;

/**
 * The Class StringMatcherBase.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public abstract class StringMatcherBase<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6604905652756969768L;

  /**
   * Match string.
   *
   * @param s the s
   * @return true, if successful
   */
  public abstract boolean matchString(String s);

  @Override
  public boolean match(T o)
  {
    if ((o instanceof String) == false) {
      return false;
    }
    return matchString((String) o);
  }
}
