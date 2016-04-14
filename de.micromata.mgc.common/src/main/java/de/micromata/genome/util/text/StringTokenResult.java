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

/**
 * The Class StringTokenResult.
 *
 * @author roger
 */
public class StringTokenResult extends TokenResultBase
{

  /**
   * The pattern.
   */
  private String pattern;

  /**
   * Instantiates a new string token result.
   *
   * @param token the token
   */
  public StringTokenResult(StringToken token)
  {
    super(token);
    this.pattern = token.getPattern();
  }

  @Override
  public String toString()
  {
    return getTokenType() + "|string=" + pattern;
  }

  @Override
  public String getConsumed()
  {
    return pattern;
  }

  @Override
  public int getConsumedLength()
  {
    return pattern.length();
  }
}
