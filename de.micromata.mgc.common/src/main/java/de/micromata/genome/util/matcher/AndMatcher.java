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
 * The Class AndMatcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public class AndMatcher<T>extends LeftRightMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -2200236053889779000L;

  /**
   * Instantiates a new and matcher.
   *
   * @param leftMatcher the left matcher
   * @param rightMatcher the right matcher
   */
  public AndMatcher(Matcher<T> leftMatcher, Matcher<T> rightMatcher)
  {
    super(leftMatcher, rightMatcher);
  }

  @Override
  public boolean match(T object)
  {
    if (leftMatcher.match(object) == false) {
      return false;
    }
    return rightMatcher.match(object);
  }

  @Override
  public String toString()
  {
    return "(" + leftMatcher.toString() + ") && (" + rightMatcher.toString() + ")";
  }

}
