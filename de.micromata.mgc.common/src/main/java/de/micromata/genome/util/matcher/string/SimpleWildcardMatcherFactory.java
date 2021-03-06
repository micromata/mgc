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

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.matcher.BooleanListRulesFactory.TokenResultList;
import de.micromata.genome.util.matcher.EqualsMatcher;
import de.micromata.genome.util.matcher.EveryMatcher;
import de.micromata.genome.util.matcher.GroovyMatcher;
import de.micromata.genome.util.matcher.GroovyMatcherFactory;
import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherFactory;
import de.micromata.genome.util.matcher.MatcherTokenFactory;
import de.micromata.genome.util.matcher.NotMatcher;
import de.micromata.genome.util.text.TextSplitterUtils;
import de.micromata.genome.util.text.TokenResult;

/**
 * Creates a Wildcard Matcher with some improvements.
 * 
 * In case of simple pattern it creates optimized Matcher
 * <p>
 * </p>
 * <ul>
 * <li>: EveryMatcher
 * <li>string: EqualsMatcher
 * <li>string*: StartWithMatcher
 * <li>string: EndsWithMatcher
 * <li>string*: ContainsMatcher
 * <li>string*string: WildcardMatcher
 * <li>contains ?: WildcardMatcher
 * </ul>
 *
 * @author roger
 * @param <T> the generic type
 */
public class SimpleWildcardMatcherFactory<T> implements MatcherFactory<T>, MatcherTokenFactory<T>
{

  /**
   * The escape char.
   */
  private char escapeChar = '\\';

  /**
   * The Constant defaultImpl.
   */
  // Note this creates an anonous class, which is wanted! don't delete {}!
  private final static SimpleWildcardMatcherFactory<String> defaultImpl = new SimpleWildcardMatcherFactory<String>()
  {
  };

  public static SimpleWildcardMatcherFactory<String> getDefaultImpl()
  {
    return defaultImpl;
  }

  /**
   * Find tokens.
   *
   * @param pattern the pattern
   * @param tk the tk
   * @return the list
   */
  protected List<Integer> findTokens(String pattern, char tk)
  {
    List<Integer> ret = new ArrayList<Integer>();
    for (int i = 0; i < pattern.length(); ++i) {
      if (pattern.charAt(i) == tk) {
        ret.add(i);
      }
    }
    return ret;
  }

  /**
   * Creates a new SimpleWildcardMatcher object.
   *
   * @param pattern the pattern
   * @return the matcher
   */
  protected Matcher<T> createWildcartMatcher(String pattern)
  {
    if (TextSplitterUtils.getUnescapedIndexOf(pattern, '?', escapeChar) != -1) {
      return new WildcardMatcher<T>(pattern);
    }

    if (pattern.length() == 1 && pattern.charAt(0) == '*') {
      return new EveryMatcher<T>();
    }

    List<Integer> positions = TextSplitterUtils.findTokenPos(pattern, '*', escapeChar);
    String upattern = TextSplitterUtils.unescape(pattern, escapeChar, '?', '*');
    // List<Integer> positions = findTokens(pattern, '*');
    if (positions.size() == 0) {
      return new EqualsMatcher<T>((T) upattern);
    }

    if (positions.size() == 1) {
      if (positions.get(0) == 0) {
        return new EndsWithMatcher<T>(upattern.substring(1));
      }

      if (positions.get(0) == pattern.length() - 1) {
        return new StartWithMatcher<T>(upattern.substring(0, upattern.length() - 1));
      }
    }

    if (positions.size() > 2) {
      return new WildcardMatcher<T>(upattern);
    }

    if (positions.size() == 2) {
      if (positions.get(0) == 0 && positions.get(1) == pattern.length() - 1) {
        return new ContainsMatcher<T>(upattern.substring(1, upattern.length() - 1));
      }
      return new WildcardMatcher<T>(upattern);
    }

    return new WildcardMatcher<T>(upattern);
  }

  @Override
  public Matcher<T> createMatcher(TokenResultList tokens)
  {
    if (tokens.eof() == true) {
      return null;
    }
    TokenResult tk = tokens.curToken();
    String cons = tk.getConsumed();
    if (cons.startsWith("${") == true) {
      return new GroovyMatcherFactory<T>().createMatcher(tokens);
    }
    return null;
  }

  @Override
  public Matcher<T> createMatcher(String pattern)
  {

    if (pattern.startsWith("!") == true) {
      return new NotMatcher<T>(createMatcher(pattern.substring(1)));
    }
    if (pattern.startsWith("~") == true) {
      return new RegExpMatcherFactory<T>().createMatcher(pattern.substring(1));
    }
    if (pattern.startsWith("${") == true && pattern.endsWith("}") == true) {
      return new GroovyMatcherFactory<T>().createMatcher(pattern.substring(2, pattern.length() - 1));
    }

    return createWildcartMatcher(pattern);
  }

  @Override
  public String getRuleString(Matcher<T> matcher)
  {
    if (matcher instanceof EveryMatcher) {
      return "*";
    }
    if (matcher instanceof GroovyMatcher) {
      return new GroovyMatcherFactory<T>().getRuleString(matcher);
    }
    if (matcher instanceof RegExpMatcher) {
      return "~" + new RegExpMatcherFactory<T>().getRuleString(matcher);
    }
    if (matcher instanceof NotMatcher) {
      return "!" + getRuleString(((NotMatcher<T>) matcher).getNested());
    }
    String s = getWildcartRuleString(matcher);
    return s;// TextSplitterUtils.escape(s, escapeChar, '?', '*');
  }

  /**
   * Gets the wildcart rule string.
   *
   * @param matcher the matcher
   * @return the wildcart rule string
   */
  public String getWildcartRuleString(Matcher<T> matcher)
  {
    if (matcher instanceof EqualsMatcher) {
      return ((EqualsMatcher<T>) matcher).getOther().toString();
    }
    if (matcher instanceof WildcardMatcher) {
      return ((WildcardMatcher<T>) matcher).getPattern();
    }
    if (matcher instanceof ContainsMatcher) {
      return "*" + ((ContainsMatcher<T>) matcher).getPattern() + "*";
    }
    if (matcher instanceof EndsWithMatcher) {
      return "*" + ((EndsWithMatcher<T>) matcher).getPattern();
    }
    if (matcher instanceof StartWithMatcher) {
      return ((StartWithMatcher<T>) matcher).getPattern() + "*";
    }

    return "<unkown>";
  }

  public char getEscapeChar()
  {
    return escapeChar;
  }

  public void setEscapeChar(char escapeChar)
  {
    this.escapeChar = escapeChar;
  }

}
