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

package de.micromata.genome.util.matcher.norm;

import java.util.List;

import de.micromata.genome.util.matcher.BooleanListRulesFactory;
import de.micromata.genome.util.matcher.InvalidMatcherGrammar;
import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;
import de.micromata.genome.util.matcher.MatcherTokenFactory;
import de.micromata.genome.util.text.RegExpToken;
import de.micromata.genome.util.text.TextSplitterUtils;
import de.micromata.genome.util.text.Token;
import de.micromata.genome.util.text.TokenResult;

/**
 * Like BooleanListRulesFactory, but but support and, or and not as aliase to &amp;&amp; || ! and normalized searches.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public class NormBooleanMatcherFactory<T>extends BooleanListRulesFactory<T>
{

  /**
   * The Constant ExtDefaultToken.
   */
  protected static final Token[] ExtDefaultToken = new Token[] { //
      new RegExpToken(TK_COMMA, "^[ \\t]*(,)[ \\t]*(.*)"), //
      new RegExpToken(TK_BO, "^[ \\t]*(\\()[\\t ]*(.*)"), //
      new RegExpToken(TK_BC, "^[ \\t]*(\\))[ \\t]*(.*)"), //
      new RegExpToken(TK_AND, "^[ \\t]*(\\&\\&)[ \\t]*(.*)"), //
      new RegExpToken(TK_AND, "^[ \\t]*( and )[ \\t]*(.*)"), //
      new RegExpToken(TK_OR, "^[ \\t]*(\\|\\|)[ \\t]*(.*)"), //
      new RegExpToken(TK_OR, "^[ \\t]*( or )[ \\t]*(.*)"), //
      new RegExpToken(TK_PLUS, "^[ \\t]*(\\+)[ \\t]*(.*)"), //
      new RegExpToken(TK_MINUS, "^[ \\t]*(\\-)[ \\t]*(.*)"), //
      new RegExpToken(TK_NOT, "^[ \\t]*(\\!)[ \\t]*(.*)"), //
      new RegExpToken(TK_NOT, "^[ \\t]*( not )[ \\t]*(.*)") //
  };

  /**
   * Instantiates a new norm boolean matcher factory.
   */
  public NormBooleanMatcherFactory()
  {
    super(new NormWildcardMatcherFactory<T>());
  }

  /**
   * Instantiates a new norm boolean matcher factory.
   *
   * @param elementFactory the element factory
   * @param tokenFactories the token factories
   */
  public NormBooleanMatcherFactory(MatcherFactory<T> elementFactory, MatcherTokenFactory<T>... tokenFactories)
  {
    super(elementFactory, tokenFactories);
  }

  @Override
  public Matcher<T> createMatcher(String pattern)
  {
    List<TokenResult> tokenResults = TextSplitterUtils.parseStringTokens(pattern, ExtDefaultToken, escapeChar, true,
        true);
    TokenResultList tkl = new TokenResultList(tokenResults, 0, pattern);
    Matcher<T> ret = consume(tkl);
    if (tkl.eof() == false) {
      throw new InvalidMatcherGrammar("unconsumed tokens. pattern: " + pattern + "; rest: " + tkl.restOfTokenString());
    }
    return ret;
  }
}
