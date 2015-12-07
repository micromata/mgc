package de.micromata.genome.util.matcher.string;

/**
 * Matches if string ends with pattern.
 *
 * @author roger
 * @param <T> the generic type
 */
public class EndsWithMatcher<T>extends StringPatternMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 3016817124097150838L;

  /**
   * Instantiates a new ends with matcher.
   */
  public EndsWithMatcher()
  {

  }

  /**
   * Instantiates a new ends with matcher.
   *
   * @param pattern the pattern
   */
  public EndsWithMatcher(String pattern)
  {
    super(pattern);
  }

  @Override
  public boolean matchString(String token)
  {
    return token.endsWith(pattern);
  }

  @Override
  public String toString()
  {
    return "<EXPR>.endsWith(" + pattern + ")";
  }
}
