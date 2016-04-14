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

/*
 * 
 */
package de.micromata.genome.util.matcher.norm;

import java.util.List;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory;
import de.micromata.genome.util.text.TextSplitterUtils;

/**
 * A factory for creating NormWildcardMatcher objects.
 * 
 * Pattern ist ^flags^wildcarmatcher with
 * <ul>
 * <li>flags are cobination of following:
 * <ul>
 * <li>u = uppercase</li>
 * <li>a = only ascii and numbers, strip away all others</li>
 * <li>x = replace umlauts and accents</li>
 * <li>e = soundex englisch</li>
 * <li>c = cologne soundex</li>
 * </ul>
 * </li>
 * <li>pattern a wildcart matcher expression. see SimpleWildcardMatcherFactory</li>
 * </ul>
 * 
 * @param <T> the generic type
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class NormWildcardMatcherFactory<T>extends SimpleWildcardMatcherFactory<T>
{

  /**
   * Instantiates a new norm wildcard matcher factory.
   */
  public NormWildcardMatcherFactory()
  {
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory#createMatcher(java.lang.String)
   */
  @Override
  public Matcher<T> createMatcher(String pattern)
  {
    //^flags^
    if (pattern.startsWith("^") == true) {

      String subPattern = pattern.substring(1);
      int idx = subPattern.indexOf('^');
      if (idx == -1) {
        throw new IllegalArgumentException("^ for normsearch needs an ^ at the end of the flags");
      }
      String flags = subPattern.substring(0, idx);
      subPattern = subPattern.substring(idx + 1);
      List<String> tokens = TextSplitterUtils.parseStringTokens(subPattern, new char[] { '?', '*' }, true);
      StringBuffer sb = new StringBuffer();
      for (String st : tokens) {
        if (st.equals("?") == true || st.equals("*") == true) {
          sb.append(st);
        } else {
          sb.append(StringNormalizeUtils.normalize(st, flags));
        }
      }

      //      subPattern = StringNormalizeUtils.normalize(subPattern, flags);
      Matcher<T> wcm = createWildcartMatcher(sb.toString());
      return new NormalizedMatcher<T>(flags, wcm);
    }
    return super.createMatcher(pattern);
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory#findTokens(java.lang.String, char)
   */
  @Override
  protected List<Integer> findTokens(String pattern, char tk)
  {
    return super.findTokens(pattern, tk);
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory#getRuleString(de.micromata.genome.util.matcher.Matcher)
   */
  @Override
  public String getRuleString(Matcher<T> matcher)
  {
    return super.getRuleString(matcher);
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.string.SimpleWildcardMatcherFactory#getWildcartRuleString(de.micromata.genome.util.matcher.Matcher)
   */
  @Override
  public String getWildcartRuleString(Matcher<T> matcher)
  {
    if (matcher instanceof NormalizedMatcher) {
      NormalizedMatcher<T> child = (NormalizedMatcher<T>) matcher;
      return "^" + child.getFlags() + "^" + getWildcartRuleString(child.getChild());
    }
    return super.getWildcartRuleString(matcher);
  }
}
