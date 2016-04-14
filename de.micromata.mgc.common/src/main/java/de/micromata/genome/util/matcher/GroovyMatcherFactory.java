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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.util.matcher.BooleanListRulesFactory.TokenResultList;
import de.micromata.genome.util.text.TokenResult;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

/**
 * Creates a groovy expression.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public class GroovyMatcherFactory<T> implements MatcherFactory<T>, MatcherTokenFactory<T>
{

  /**
   * The Constant logger.
   */
  private static final Logger logger = Logger.getLogger(GroovyMatcherFactory.class);

  /**
   * If set, function from this class are available in expression.
   * 
   * The class has to define a constructor with the matching object as argument.
   */
  private Class<?> functionProvider = null;

  /**
   * Instantiates a new groovy matcher factory.
   */
  public GroovyMatcherFactory()
  {

  }

  /**
   * Instantiates a new groovy matcher factory.
   *
   * @param functionProvider the function provider
   */
  public GroovyMatcherFactory(Class<?> functionProvider)
  {
    this.functionProvider = functionProvider;
  }

  @Override
  public Matcher<T> createMatcher(String pattern)
  {
    String evaluate = pattern;
    if (functionProvider != null) {
      evaluate = "new " + functionProvider.getCanonicalName() + "(arg) { boolean _gfwd(arg) {" + pattern
          + "; } }._gfwd(arg)";

    }
    GroovyShell gs = new GroovyShell();
    Script script = gs.parse(evaluate);
    return new GroovyMatcher<T>(pattern, script.getClass());
  }

  @Override
  public String getRuleString(Matcher<T> matcher)
  {
    if ((matcher instanceof GroovyMatcher) == false) {
      return "<unknown>";
    }
    return "${" + ((GroovyMatcher<T>) matcher).getSource() + "}";
  }

  @Override
  public Matcher<T> createMatcher(TokenResultList tokens)
  {
    if (tokens.eof() == true) {
      return null;
    }
    if (tokens.curToken().getConsumed().startsWith("${") == false) {
      return null;
    }
    String expression = consumeGroovyExression(tokens);
    if (logger.isDebugEnabled() == true) {
      logger.debug("Parsed groovy exression: " + expression);
    }
    return createMatcher(expression);
  }

  /**
   * Consume groovy exression.
   *
   * @param tokens the tokens
   * @return the string
   */
  protected String consumeGroovyExression(TokenResultList tokens)
  {
    String ret = consumeGroovyExressionInternal(tokens);
    ret = StringUtils.replace(ret, " or ", " || ");
    ret = StringUtils.replace(ret, " and ", " && ");
    return ret;

  }

  /**
   * Append groovy code before.
   *
   * @param tk the tk
   * @param sb the sb
   */
  protected void appendGroovyCodeBefore(TokenResult tk, StringBuilder sb)
  {
    if (tk.getTokenType() == BooleanListRulesFactory.TK_AND || tk.getTokenType() == BooleanListRulesFactory.TK_OR) {
      sb.append(" ");
    }
  }

  /**
   * Append groovy code after.
   *
   * @param tk the tk
   * @param sb the sb
   */
  protected void appendGroovyCodeAfter(TokenResult tk, StringBuilder sb)
  {
    if (tk.getTokenType() == BooleanListRulesFactory.TK_AND || tk.getTokenType() == BooleanListRulesFactory.TK_OR) {
      sb.append(" ");
    }
  }

  /**
   * Consume groovy exression internal.
   *
   * @param tokens the tokens
   * @return the string
   */
  protected String consumeGroovyExressionInternal(TokenResultList tokens)
  {
    StringBuilder sb = new StringBuilder();
    TokenResult tk = tokens.curToken();
    String in = tk.getConsumed().substring(2);
    int curlBrackets = 1;
    char waitForChar = 0;
    while (tokens.eof() == false) {
      appendGroovyCodeBefore(tk, sb);
      for (int i = 0; i < in.length(); ++i) {
        char c = in.charAt(i);
        if (waitForChar != 0) {
          if (c == waitForChar) {
            waitForChar = 0;
          }
          sb.append(c);
          continue;
        }
        if (c == '{') {
          ++curlBrackets;
          sb.append(c);
        } else if (c == '}') {
          --curlBrackets;
          if (curlBrackets == 0) {
            if (i == in.length() - 1) {
              tokens.nextToken();
            } else {
              logger.warn("Unconsumed character while parsing parsing tokens");
            }
            return sb.toString();
          }
          sb.append(c);
        } else if (c == '\'' || c == '"') {
          waitForChar = c;
          sb.append(c);
        } else {
          sb.append(c);
        }
      }
      appendGroovyCodeAfter(tk, sb);
      tk = tokens.nextToken();
      if (tokens.eof() == true) {
        break;
      }
      in = tk.getConsumed();
    }
    throw new IllegalArgumentException("Cannot parse matcher with groovy expression. Missing closing '}");
  }

  public Class<?> getFunctionProvider()
  {
    return functionProvider;
  }

  public void setFunctionProvider(Class<?> functionProvider)
  {
    this.functionProvider = functionProvider;
  }

}
