package de.micromata.genome.util.matcher.collections;

import java.util.Set;

import de.micromata.genome.util.matcher.MatcherBase;

/**
 * Looks if pattern is in set.
 * 
 * @param <T> the generic type
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class InSetMatcher<T> extends MatcherBase<Set<T>>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -8834777353135063333L;

  /**
   * The pattern.
   */
  private String pattern;

  /**
   * Instantiates a new in set matcher.
   * 
   * @param pattern the pattern
   */
  public InSetMatcher(String pattern)
  {
    super();
    this.pattern = pattern;
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.Matcher#match(java.lang.Object)
   */
  public boolean match(Set<T> object)
  {
    return object.contains(pattern);
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "InSetMatcher(" + pattern + ")";
  }

}
