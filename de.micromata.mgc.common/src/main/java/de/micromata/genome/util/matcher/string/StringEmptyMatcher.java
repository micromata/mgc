package de.micromata.genome.util.matcher.string;

import org.apache.commons.lang.StringUtils;

/**
 * Matches if string is null or empty.
 *
 * @author roger
 * @param <T> the generic type
 */
public class StringEmptyMatcher<T>extends StringMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -6538036307459516611L;

  /**
   * Instantiates a new string empty matcher.
   */
  public StringEmptyMatcher()
  {

  }

  @Override
  public boolean matchString(String token)
  {
    return StringUtils.isEmpty(token);
  }

  @Override
  public String toString()
  {
    return "<EXPR>.isEmpty()";
  }
}
