package de.micromata.genome.util.matcher.string;

import org.apache.commons.io.FilenameUtils;

/**
 * Matches if Wildcard pattern matches.
 *
 * @author roger
 * @param <T> the generic type
 */
public class WildcardMatcher<T>extends StringPatternMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6447659182015217706L;

  /**
   * Instantiates a new wildcard matcher.
   */
  public WildcardMatcher()
  {

  }

  /**
   * Instantiates a new wildcard matcher.
   *
   * @param pattern the pattern
   */
  public WildcardMatcher(String pattern)
  {
    super(pattern);
  }

  @Override
  public boolean matchString(String token)
  {
    return FilenameUtils.wildcardMatch(token, pattern) == true;
  }

  @Override
  public String toString()
  {
    return "<EXPR>.wildCartMatch(" + pattern + ")";
  }
}
