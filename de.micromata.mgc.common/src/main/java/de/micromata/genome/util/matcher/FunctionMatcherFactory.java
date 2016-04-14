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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.micromata.genome.util.matcher.BooleanListRulesFactory.TokenResultList;
import de.micromata.genome.util.text.TokenResult;
import de.micromata.genome.util.types.Pair;

/**
 * Logical operations and functions provided by a bean.
 * 
 * The bean arguments doesn't support nestet function calls, but only simple strings.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public class FunctionMatcherFactory<T> implements MatcherTokenFactory<T>
{

  /**
   * The evaluation functions.
   */
  protected Map<String, Pair<Object, Method>> evaluationFunctions = new HashMap<String, Pair<Object, Method>>();

  /**
   * The evaluation providers.
   */
  protected Object[] evaluationProviders;

  /**
   * Instantiates a new function matcher factory.
   *
   * @param evaluationProviders the evaluation providers
   */
  public FunctionMatcherFactory(Object... evaluationProviders)
  {
    this.evaluationProviders = evaluationProviders;
    extractEvaluationFunctions();
  }

  @Override
  public Matcher<T> createMatcher(TokenResultList tokens)
  {
    if (tokens.eof() == true) {
      return null;
    }
    String name = tokens.curToken().getConsumed();
    if (Character.isJavaIdentifierStart(name.charAt(0)) == false) {
      return null;
    }
    for (int i = 1; i < name.length(); ++i) {
      if (Character.isJavaIdentifierPart(name.charAt(i)) == false) {
        return null;
      }
    }
    TokenResult nt = tokens.peekToken(1);
    if (nt == null) {
      return null;
    }
    if (nt.getConsumed().equals("(") == false) {
      return null;
    }
    tokens.nextToken();
    return consumeFunction(name, tokens);
  }

  /**
   * Consume function.
   *
   * @param name the name
   * @param tokens the tokens
   * @return the matcher
   */
  protected Matcher<T> consumeFunction(String name, TokenResultList tokens)
  {
    Pair<Object, Method> pair = evaluationFunctions.get(name);
    if (pair == null) {
      throw new InvalidMatcherGrammar("Function is not registered: " + name + "; pattern: " + tokens.pattern);
    }
    TokenResult tr = tokens.nextToken();
    List<Object> args = new ArrayList<Object>();
    finalParsing: while (true) {
      if (tokens.eof() == true) {
        throw new InvalidMatcherGrammar("Missing end of function; pattern: " + tokens.pattern);
      }
      if (tr.getTokenType() == BooleanListRulesFactory.TK_BC) {
        tokens.nextToken();
        break;
      }
      StringBuilder arg = new StringBuilder();
      while (tokens.eof() == false) {

        if (tr.getTokenType() == BooleanListRulesFactory.TK_BC) {
          if (arg.length() > 0) {
            args.add(arg.toString());
          }
          tokens.nextToken();
          break finalParsing;
        }
        if (tr.getTokenType() == BooleanListRulesFactory.TK_COMMA) {
          args.add(arg.toString());
          arg = new StringBuilder();
          continue;
        }
        arg.append(tr.getConsumed());
        tr = tokens.nextToken();
      }
      throw new InvalidMatcherGrammar("Expecting comma or closing bracket in function arg: " + tokens.pattern);
    }

    return new FunctionMatcher<T>(pair.getFirst(), pair.getSecond(), args.toArray());
  }

  /**
   * Extract evaluation functions.
   */
  protected void extractEvaluationFunctions()
  {
    for (Object bean : evaluationProviders) {
      for (Method m : bean.getClass().getMethods()) {
        if ((m.getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT) {
          continue;
        }
        if ((m.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
          continue;
        }
        if (m.getReturnType() != Boolean.TYPE && MatcherFactory.class.isAssignableFrom(m.getReturnType()) == false) {
          continue;
        }
        if (m.getParameterTypes().length == 0) {
          continue;
        }
        //        for (Class< ? > param : m.getParameterTypes()) {
        //          if (param != String.class) {
        //            continue nextMethod;
        //          }
        //        }
        if (evaluationFunctions.containsKey(m.getName()) == true) {
          throw new IllegalArgumentException("Duplicated method name found: " + m.getName());
        }
        evaluationFunctions.put(m.getName(), Pair.make(bean, m));
      }
    }
  }

}
