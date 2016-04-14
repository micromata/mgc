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

import de.micromata.genome.util.matcher.BooleanListRulesFactory.TokenResultList;

/**
 * Creates a Matcher consuming tokens.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public interface MatcherTokenFactory<T>
{
  /**
   * Reading tokens and crate a matcher.
   * 
   * The curToken should point after consuming behind last consumed token.
   * 
   * @param tokens
   * @return null, if not be consumed.
   */
  public Matcher<T> createMatcher(TokenResultList tokens);
}
