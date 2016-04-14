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
 * Matches if pattern is equals to token.
 * 
 * if token is false, also accept null values
 * 
 * @author roger
 * 
 */
public class EqualsWithBoolMatcher extends StringPatternMatcherBase<String>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -531763040829078392L;

  /**
   * Instantiates a new equals with bool matcher.
   */
  public EqualsWithBoolMatcher()
  {

  }

  /**
   * Instantiates a new equals with bool matcher.
   *
   * @param other the other
   */
  public EqualsWithBoolMatcher(String other)
  {
    super(other);
  }

  @Override
  public boolean match(String o)
  {
    return matchString(o);

  }

  @Override
  public boolean matchString(String token)
  {
    if (token == null && pattern != null) {
      if (pattern.equalsIgnoreCase("false") == true) {
        return true;
      }
      return false;
    }
    if (token != null && pattern == null) {
      if (token.equalsIgnoreCase("false") == false) {
        return true;
      }
      return false;
    }
    return token.equals(pattern);
  }

  @Override
  public String toString()
  {
    return pattern.toString() + " = <ExprWithBool>";
  }
}
