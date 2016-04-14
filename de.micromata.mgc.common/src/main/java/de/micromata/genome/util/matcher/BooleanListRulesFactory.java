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

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory;
import de.micromata.genome.util.text.RegExpToken;
import de.micromata.genome.util.text.TextSplitterUtils;
import de.micromata.genome.util.text.Token;
import de.micromata.genome.util.text.TokenResult;

/**
 * A rule which contains a list of rules devided by divider.
 * 
 * Each rule starts with - or +, which mark the following rule if matches or not
 *
 * @author roger
 * @param <T> the generic type
 */
public class BooleanListRulesFactory<T> implements MatcherFactory<T>
{

  /**
   * The Constant TK_UNMATCHED.
   */
  public static final int TK_UNMATCHED = 0;

  /**
   * The Constant TK_COMMA.
   */
  public static final int TK_COMMA = 1;

  /**
   * The Constant TK_BO.
   */
  public static final int TK_BO = 2;

  /**
   * The Constant TK_BC.
   */
  public static final int TK_BC = 3;

  /**
   * The Constant TK_AND.
   */
  public static final int TK_AND = 4;

  /**
   * The Constant TK_OR.
   */
  public static final int TK_OR = 5;

  /**
   * The Constant TK_PLUS.
   */
  public static final int TK_PLUS = 6;

  /**
   * The Constant TK_MINUS.
   */
  public static final int TK_MINUS = 7;

  /**
   * The Constant TK_HASH.
   */
  public static final int TK_HASH = 8;

  /**
   * The Constant TK_NOT.
   */
  public static final int TK_NOT = 9;

  /**
   * The Constant TK_BOBC_PRIO.
   */
  public static final int TK_BOBC_PRIO = 3;

  /**
   * The Constant TK_COMMA_PRIO.
   */
  public static final int TK_COMMA_PRIO = 1;

  /**
   * The Constant TK_ANDOR_PRIO.
   */
  public static final int TK_ANDOR_PRIO = 2;

  /**
   * The Constant DefaultToken.
   */
  protected static final Token[] DefaultToken = new Token[] { //
      new RegExpToken(TK_COMMA, "^[ \\t]*(,)[ \\t]*(.*)"), //
      new RegExpToken(TK_BO, "^[ \\t]*(\\()[\\t ]*(.*)"), //
      new RegExpToken(TK_BC, "^[ \\t]*(\\))[ \\t]*(.*)"), //
      new RegExpToken(TK_AND, "^[ \\t]*(\\&\\&)[ \\t]*(.*)"), //
      new RegExpToken(TK_OR, "^[ \\t]*(\\|\\|)[ \\t]*(.*)"), //
      new RegExpToken(TK_PLUS, "^[ \\t]*(\\+)[ \\t]*(.*)"), //
      new RegExpToken(TK_MINUS, "^[ \\t]*(\\-)[ \\t]*(.*)"), //
      new RegExpToken(TK_NOT, "^[ \\t]*(\\!)[ \\t]*(.*)") //
  };

  // private String[] dividers = new String[] { ", ", " ,", ",", " (", "(", "( ", " )", ") ", " and ", " or "};

  /**
   * The escape char.
   */
  protected char escapeChar = '\\';

  /**
   * The token factories.
   */
  protected List<MatcherTokenFactory<T>> tokenFactories = new ArrayList<MatcherTokenFactory<T>>();

  /**
   * The element factory.
   */
  protected MatcherFactory<T> elementFactory = new SimpleWildcardMatcherFactory<T>();

  /**
   * Instantiates a new boolean list rules factory.
   */
  public BooleanListRulesFactory()
  {
    if (elementFactory instanceof MatcherTokenFactory) {
      this.tokenFactories.add((MatcherTokenFactory<T>) elementFactory);
    }
  }

  /**
   * Instantiates a new boolean list rules factory.
   *
   * @param elementFactory the element factory
   * @param tokenFactories the token factories
   */
  public BooleanListRulesFactory(MatcherFactory<T> elementFactory, MatcherTokenFactory<T>... tokenFactories)
  {
    this.elementFactory = elementFactory;
    if (elementFactory instanceof MatcherTokenFactory) {
      this.tokenFactories.add((MatcherTokenFactory<T>) elementFactory);
    }
    for (MatcherTokenFactory<T> tkf : tokenFactories) {
      this.tokenFactories.add(tkf);
    }
  }

  // public BooleanListRulesFactory(MatcherFactory<T> elementFactory, String... dividers)
  // {
  // this.elementFactory = elementFactory;
  // this.dividers = dividers;
  /**
   * The Class TokenResultList.
   */
  // }
  public static class TokenResultList
  {

    /**
     * The token results.
     */
    public List<TokenResult> tokenResults;

    /**
     * The position.
     */
    public int position;

    /**
     * The pattern.
     */
    public String pattern;

    /**
     * Instantiates a new token result list.
     *
     * @param tokens the tokens
     * @param position the position
     * @param pattern the pattern
     */
    public TokenResultList(List<TokenResult> tokens, int position, String pattern)
    {
      this.tokenResults = tokens;
      this.position = position;
      this.pattern = pattern;
    }

    /**
     * Cur token.
     *
     * @return the token result
     */
    public TokenResult curToken()
    {
      return tokenResults.get(position);
    }

    /**
     * Next token.
     *
     * @return the token result
     */
    public TokenResult nextToken()
    {
      ++position;
      if (position >= tokenResults.size()) {
        return null;
      }
      return tokenResults.get(position);
    }

    /**
     * Peek token.
     *
     * @param pos the pos
     * @return the token result
     */
    public TokenResult peekToken(int pos)
    {
      if (position + pos >= tokenResults.size() || position + pos < 0) {
        return null;
      }
      return tokenResults.get(position + pos);
    }

    /**
     * Eof.
     *
     * @return true, if successful
     */
    public boolean eof()
    {
      return position >= tokenResults.size();
    }

    /**
     * Rest of token string.
     *
     * @return the string
     */
    public String restOfTokenString()
    {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < tokenResults.size(); ++i) {
        sb.append(tokenResults.get(i).getConsumed());
      }
      return sb.toString();
    }
  }

  @Override
  public Matcher<T> createMatcher(String pattern)
  {

    List<TokenResult> tokenResults = TextSplitterUtils.parseStringTokens(pattern, DefaultToken, escapeChar, true, true);
    TokenResultList tkl = new TokenResultList(tokenResults, 0, pattern);
    Matcher<T> ret = consume(tkl);
    if (tkl.eof() == false) {
      throw new InvalidMatcherGrammar("unconsumed tokens. pattern: " + pattern + "; rest: " + tkl.restOfTokenString());
    }
    return ret;
  }

  /**
   * Consume.
   *
   * @param tokens the tokens
   * @return the matcher
   */
  protected Matcher<T> consume(TokenResultList tokens)
  {
    return consumeAndOr(tokens);
  }

  /**
   * Consume bracket.
   *
   * @param tks the tks
   * @return the matcher
   */
  protected Matcher<T> consumeBracket(TokenResultList tks)
  {
    if (tks.eof() == true) {
      return null;
    }
    TokenResult tk = tks.curToken();
    if (tk.getTokenType() == TK_BO) {
      tks.nextToken();
      Matcher<T> m = consume(tks);
      tk = tks.curToken();
      if (tk.getTokenType() != TK_BC) {
        throw new InvalidMatcherGrammar("grammar has no matching close bracket: " + tks.pattern);
      }
      tks.nextToken();
      return m;
    }
    return consumePlusMinus(tks);
  }

  /**
   * Consume and or.
   *
   * @param tks the tks
   * @return the matcher
   */
  protected Matcher<T> consumeAndOr(TokenResultList tks)
  {
    Matcher<T> left = consumeList(tks);
    if (left == null) {
      return left;
    }
    if (tks.eof() == true) {
      return left;
    }
    TokenResult tk = tks.curToken();
    if (tk.getTokenType() == TK_AND || tk.getTokenType() == TK_OR) {
      tks.nextToken();
      Matcher<T> right = consumeAndOr(tks);
      if (right == null) {
        throw new InvalidMatcherGrammar("Missing right express of <expr>[&&||\\|]<expr>: "
            + tks.pattern
            + "; rest: "
            + tks.restOfTokenString());
      }
      if (tk.getTokenType() == TK_AND) {
        return new AndMatcher<T>(left, right);
      }
      return new OrMatcher<T>(left, right);
    }
    return left;
  }

  /**
   * Consume list.
   *
   * @param tks the tks
   * @return the matcher
   */
  protected Matcher<T> consumeList(TokenResultList tks)
  {

    Matcher<T> left = consumeBracket(tks);
    if (left == null) {
      return null;
    }
    if (tks.eof() == true) {
      return left;
    }
    List<Matcher<T>> elements = new ArrayList<Matcher<T>>();
    elements.add(left);

    do {
      TokenResult tk = tks.curToken();
      if (tk.getTokenType() != TK_COMMA) {
        break;
      }
      tks.nextToken();
      left = consumeBracket(tks);
      if (left == null) {
        break;
      }
      elements.add(left);
    } while (left != null && tks.eof() == false);
    if (elements.size() == 1) {
      return elements.get(0);
    }
    return new BooleanListMatcher<T>(elements);
  }

  /**
   * Consume plus minus.
   *
   * @param tokens the tokens
   * @return the matcher
   */
  protected Matcher<T> consumePlusMinus(TokenResultList tokens)
  {
    if (tokens.eof() == true) {
      return null;
    }
    TokenResult tk = tokens.curToken();
    if (tk.getTokenType() == TK_NOT) {
      tokens.nextToken();
      Matcher<T> nt = consumeList(tokens);
      return new NotMatcher<T>(nt);
    }
    if (tk.getTokenType() == TK_PLUS || tk.getTokenType() == TK_MINUS) {
      tokens.nextToken();
      Matcher<T> n = consumeListElement(tokens);
      if (tk.getTokenType() == TK_HASH) {
        return new HashmarkMatcher<T>(n);
      }
      return new TreeStateMatcher<T>(n, tk.getTokenType() == TK_PLUS);
    }
    return consumeListElement(tokens);
  }

  /**
   * Consume list element.
   *
   * @param tokens the tokens
   * @return the matcher
   */
  protected Matcher<T> consumeListElement(TokenResultList tokens)
  {

    if (tokens.eof() == true) {
      return null;
    }
    TokenResult tk = tokens.curToken();

    for (MatcherTokenFactory<T> tkfact : tokenFactories) {
      Matcher<T> m = tkfact.createMatcher(tokens);
      if (m != null) {
        return m;
      }
    }
    if (tk.getTokenType() != TK_UNMATCHED) {
      throw new InvalidMatcherGrammar("Excepting element. Got: " + tk.getConsumed() + "; pattern: " + tokens.pattern);
    }
    // TODO ggf. problematisch, wenn intern auch escaped wird
    String elText = TextSplitterUtils.unescape(tk.getConsumed(), escapeChar);
    Matcher<T> m = elementFactory.createMatcher(elText);
    tokens.nextToken();
    return m;
  }

  @Override
  public String getRuleString(Matcher<T> matcher)
  {

    if (matcher instanceof BooleanListMatcher) {
      List<Matcher<T>> ruleList = ((BooleanListMatcher<T>) matcher).getMatcherList();
      boolean isFirst = true;
      StringBuilder sb = new StringBuilder();
      for (Matcher<T> r : ruleList) {
        if (isFirst == false) {
          sb.append(",");

        }
        sb.append(getRuleString(r));
        isFirst = false;
      }
      return sb.toString();
    }
    if (matcher instanceof NotMatcher) {
      return "!" + getRuleString(((NotMatcher<T>) matcher).getNested());
    }
    if (matcher instanceof AndMatcher) {
      AndMatcher<T> m = (AndMatcher<T>) matcher;
      return "(" + getRuleString(m.getLeftMatcher()) + ") && (" + getRuleString(m.getRightMatcher()) + ")";
    }
    if (matcher instanceof OrMatcher) {
      OrMatcher<T> m = (OrMatcher<T>) matcher;
      return "(" + getRuleString(m.getLeftMatcher()) + ") || (" + getRuleString(m.getRightMatcher()) + ")";
    }
    if (matcher instanceof TreeStateMatcher) {
      TreeStateMatcher<T> m = (TreeStateMatcher<T>) matcher;
      String sign = "+";
      if (m.isValue() == false) {
        sign = "-";
      }
      return sign + "(" + getRuleString(m.getNested()) + ")";
    }
    return TextSplitterUtils.escape(elementFactory.getRuleString(matcher), escapeChar, '&', ',', '|', '!', '(', ')',
        '+', '-');
  }

  public char getEscapeChar()
  {
    return escapeChar;
  }

  public void setEscapeChar(char escapeChar)
  {
    this.escapeChar = escapeChar;
  }

  public MatcherFactory<T> getElementFactory()
  {
    return elementFactory;
  }

  public void setElementFactory(MatcherFactory<T> elementFactory)
  {
    this.elementFactory = elementFactory;
  }

  public List<MatcherTokenFactory<T>> getTokenFactories()
  {
    return tokenFactories;
  }

  public void setTokenFactories(List<MatcherTokenFactory<T>> tokenFactories)
  {
    this.tokenFactories = tokenFactories;
  }

}
