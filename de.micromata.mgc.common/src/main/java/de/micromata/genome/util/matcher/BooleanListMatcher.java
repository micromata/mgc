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

import java.util.List;

/**
 * Contains a list of Matchers with negativ and positiv matchers
 * 
 * returns the result of the last matcher that does not return NoMatch (i.e. neither positive nor negative)
 *
 * @author roger
 * @param <T> the generic type
 */
public class BooleanListMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 2488710769981109527L;

  /**
   * The matcher list.
   */
  private List<Matcher<T>> matcherList;

  /**
   * Instantiates a new boolean list matcher.
   */
  public BooleanListMatcher()
  {

  }

  /**
   * Instantiates a new boolean list matcher.
   *
   * @param matcherList the matcher list
   */
  public BooleanListMatcher(List<Matcher<T>> matcherList)
  {
    this.matcherList = matcherList;
  }

  @Override
  public MatchResult apply(T token)
  {
    MatchResult res = MatchResult.NoMatch;
    for (Matcher<T> m : matcherList) {
      MatchResult mr = m.apply(token);
      if (mr != MatchResult.NoMatch) {
        res = mr;
      }
    }
    return res;
  }

  @Override
  public boolean match(T token)
  {
    return apply(token) == MatchResult.MatchPositive;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    sb.append("<Expr>.matchList(");
    for (Matcher<T> m : matcherList) {
      if (isFirst == false) {
        sb.append(",");
      } else {
        isFirst = false;
      }
      sb.append(m.toString());
    }
    sb.append(")");
    return sb.toString();
  }

  public List<Matcher<T>> getMatcherList()
  {
    return matcherList;
  }

  public void setMatcherList(List<Matcher<T>> matcherList)
  {
    this.matcherList = matcherList;
  }

}
