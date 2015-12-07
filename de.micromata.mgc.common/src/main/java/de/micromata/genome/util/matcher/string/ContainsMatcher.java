package de.micromata.genome.util.matcher.string;

/**
 * Matches if string is containing.
 *
 * @author roger
 * @param <T> the generic type
 */
public class ContainsMatcher<T>extends StringPatternMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6751376242568909350L;

  /**
   * Instantiates a new contains matcher.
   */
  public ContainsMatcher()
  {

  }

  /**
   * Instantiates a new contains matcher.
   *
   * @param pattern the pattern
   */
  public ContainsMatcher(String pattern)
  {
    super(pattern);
  }

  @Override
  public boolean matchString(String token)
  {
    return token.indexOf(pattern) != -1;
  }

  @Override
  public String toString()
  {
    return "<EXPR>.contains(" + pattern + ")";
  }
}
