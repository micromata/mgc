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
 * The Class StringToken.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class StringToken extends TokenBase
{

  /**
   * The pattern.
   */
  private String pattern;

  /**
   * Instantiates a new string token.
   *
   * @param tokenType the token type
   * @param pattern the pattern
   */
  public StringToken(int tokenType, String pattern)
  {
    super(tokenType);
    this.pattern = pattern;
  }

  /**
   * Instantiates a new string token.
   */
  public StringToken()
  {

  }

  @Override
  public TokenResult consume(String text, char escapeChar)
  {
    if (text.startsWith(pattern) == true) {
      return new StringTokenResult(this);
    }

    return null;
  }

  @Override
  public boolean match(String text)
  {
    return text.startsWith(pattern) == true;
  }

  public String getPattern()
  {
    return pattern;
  }

  public void setPattern(String pattern)
  {
    this.pattern = pattern;
  }
}
