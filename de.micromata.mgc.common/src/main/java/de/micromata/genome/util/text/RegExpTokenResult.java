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

package de.micromata.genome.util.text;

import java.util.regex.Matcher;

/**
 * The Class RegExpTokenResult.
 *
 * @author roger
 */
public class RegExpTokenResult extends TokenResultBase
{

  /**
   * The reg exp matcher.
   */
  private Matcher regExpMatcher;

  /**
   * The matched.
   */
  private String matched;

  /**
   * Instantiates a new reg exp token result.
   *
   * @param regExpToken the reg exp token
   * @param regExpMatcher the reg exp matcher
   * @param matched the matched
   */
  public RegExpTokenResult(RegExpToken regExpToken, Matcher regExpMatcher, String matched)
  {
    super(regExpToken);
    this.regExpMatcher = regExpMatcher;
    this.matched = matched;
  }

  @Override
  public String toString()
  {
    return getTokenType() + "|pattern=" + regExpMatcher.pattern().toString() + "|matched=" + matched;
  }

  @Override
  public String getConsumed()
  {
    return matched;
  }

  @Override
  public int getConsumedLength()
  {
    int gc = regExpMatcher.groupCount();
    if (gc > 1) {
      return regExpMatcher.start(2);
    }
    return getConsumed().length();
  }

  public Matcher getRegExpMatcher()
  {
    return regExpMatcher;
  }

  public void setRegExpMatcher(Matcher regExpMatcher)
  {
    this.regExpMatcher = regExpMatcher;
  }

}
