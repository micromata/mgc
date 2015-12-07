package de.micromata.genome.util.matcher;

/**
 * The Class PatternMatcherBase.
 *
 * @author roger
 * @param <T> the generic type
 */
public abstract class PatternMatcherBase<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -2009019329583789214L;

  /**
   * The pattern.
   */
  protected String pattern;

  /**
   * Instantiates a new pattern matcher base.
   */
  public PatternMatcherBase()
  {
  }

  /**
   * Instantiates a new pattern matcher base.
   *
   * @param pattern the pattern
   */
  public PatternMatcherBase(String pattern)
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
}
