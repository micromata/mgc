package de.micromata.genome.util.matcher.string;

/**
 * Matches if string starts with pattern.
 *
 * @author roger
 * @param <T> the generic type
 */
public class StartWithMatcher<T>extends StringPatternMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7078608821658998712L;

  /**
   * Instantiates a new start with matcher.
   */
  public StartWithMatcher()
  {

  }

  /**
   * Instantiates a new start with matcher.
   *
   * @param pattern the pattern
   */
  public StartWithMatcher(String pattern)
  {
    super(pattern);
  }

  @Override
  public boolean matchString(String token)
  {
    return token.startsWith(pattern);
  }

  @Override
  public String toString()
  {
    return "<EXPR>.startsWith(" + pattern + ")";
  }
}
