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

/**
 * Same as BooleanListRulesFactory, but support and, or and not as aliase to && || !.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public class LogicalMatcherFactory<T>extends BooleanListRulesFactory<T>
{

  /**
   * Instantiates a new logical matcher factory.
   */
  public LogicalMatcherFactory()
  {
    super();
  }

  /**
   * Instantiates a new logical matcher factory.
   *
   * @param elementFactory the element factory
   */
  public LogicalMatcherFactory(MatcherFactory<T> elementFactory)
  {
    super(elementFactory);
  }

  /**
   * Prepare rule.
   *
   * @param rule the rule
   * @return the string
   */
  private String prepareRule(String rule)
  {
    rule = StringUtils.replace(rule, " and ", " && ");
    rule = StringUtils.replace(rule, " or ", " || ");
    rule = StringUtils.replace(rule, " not ", " !");
    return rule;
  }

  @Override
  public Matcher<T> createMatcher(String pattern)
  {
    return super.createMatcher(prepareRule(pattern));
  }

}
