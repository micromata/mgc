package de.micromata.genome.util.matcher.string;

/**
 * The Class StringPatternMatcherBase.
 *
 * @author roger
 * @param <T> the generic type
 */
public abstract class StringPatternMatcherBase<T>extends StringMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -1169791355239547248L;

  /**
   * The pattern.
   */
  protected String pattern;

  /**
   * Instantiates a new string pattern matcher base.
   */
  public StringPatternMatcherBase()
  {

  }

  /**
   * Instantiates a new string pattern matcher base.
   *
   * @param pattern the pattern
   */
  public StringPatternMatcherBase(String pattern)
  {
    this.pattern = pattern;
  }

  public String getPattern()
  {
    return pattern;
  }

  public void setPattern(String pattern)
  {
    this.pattern = pattern;
  }

  @Override
  public String toString()
  {
    return "<EXPR>.patternMatch(" + pattern + ")";
  }

}
