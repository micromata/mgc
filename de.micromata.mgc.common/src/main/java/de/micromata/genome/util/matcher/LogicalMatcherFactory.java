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
