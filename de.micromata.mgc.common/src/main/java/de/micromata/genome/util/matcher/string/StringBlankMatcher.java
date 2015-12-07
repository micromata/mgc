package de.micromata.genome.util.matcher.string;

import org.apache.commons.lang.StringUtils;

/**
 * Matches if string is null or empty.
 *
 * @author roger
 * @param <T> the generic type
 */
public class StringBlankMatcher<T>extends StringMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 7658840012005864847L;

  /**
   * Instantiates a new string blank matcher.
   */
  public StringBlankMatcher()
  {

  }

  @Override
  public boolean matchString(String token)
  {
    return StringUtils.isBlank(token);
  }

  @Override
  public String toString()
  {
    return "<EXPR>.isBlank()";
  }
}
