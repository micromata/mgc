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
 * The Class TreeStateMatcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public class TreeStateMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -6117823200897120959L;

  /**
   * The value.
   */
  private boolean value;

  /**
   * The nested.
   */
  private Matcher<T> nested;

  /**
   * Instantiates a new tree state matcher.
   */
  public TreeStateMatcher()
  {

  }

  /**
   * Instantiates a new tree state matcher.
   *
   * @param nested the nested
   * @param value the value
   */
  public TreeStateMatcher(Matcher<T> nested, boolean value)
  {
    this.nested = nested;
    this.value = value;
  }

  @Override
  public MatchResult apply(T token)
  {
    MatchResult mr = nested.apply(token);
    if (mr == MatchResult.NoMatch) {
      return mr;
    }
    return value ? MatchResult.MatchPositive : MatchResult.MatchNegative;
  }

  @Override
  public boolean match(T token)
  {
    return apply(token) == MatchResult.MatchPositive;
  }

  @Override
  public String toString()
  {
    String s = "<EXPR>.match(" + nested.toString() + ")";
    if (value == false) {
      s = "!" + s;
    }
    return s;
  }

  public boolean isValue()
  {
    return value;
  }

  public void setValue(boolean value)
  {
    this.value = value;
  }

  public Matcher<T> getNested()
  {
    return nested;
  }

  public void setNested(Matcher<T> nested)
  {
    this.nested = nested;
  }

}
