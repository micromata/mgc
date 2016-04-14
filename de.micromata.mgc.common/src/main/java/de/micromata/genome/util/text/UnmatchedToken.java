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
 * The Class UnmatchedToken.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class UnmatchedToken implements TokenResult
{

  /**
   * The unmatched.
   */
  private String unmatched;

  /**
   * Instantiates a new unmatched token.
   *
   * @param unmatched the unmatched
   */
  public UnmatchedToken(String unmatched)
  {
    this.unmatched = unmatched;
  }

  @Override
  public String toString()
  {
    return unmatched;
  }

  @Override
  public int getTokenType()
  {
    return 0;
  }

  @Override
  public String getConsumed()
  {
    return unmatched;
  }

  @Override
  public int getConsumedLength()
  {
    return unmatched.length();
  }

  public String getUnmatched()
  {
    return unmatched;
  }

  public void setUnmatched(String unmatched)
  {
    this.unmatched = unmatched;
  }

}
