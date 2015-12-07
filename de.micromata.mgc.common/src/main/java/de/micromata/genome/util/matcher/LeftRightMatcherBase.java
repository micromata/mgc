package de.micromata.genome.util.matcher;

/**
 * Matcher base with a left and right matcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public abstract class LeftRightMatcherBase<T>extends MatcherBase<T>
{

  /**
   * The left matcher.
   */
  protected Matcher<T> leftMatcher;

  /**
   * The right matcher.
   */
  protected Matcher<T> rightMatcher;

  /**
   * Instantiates a new left right matcher base.
   */
  public LeftRightMatcherBase()
  {

  }

  /**
   * Instantiates a new left right matcher base.
   *
   * @param leftMatcher the left matcher
   * @param rightMatcher the right matcher
   */
  public LeftRightMatcherBase(Matcher<T> leftMatcher, Matcher<T> rightMatcher)
  {
    this.leftMatcher = leftMatcher;
    this.rightMatcher = rightMatcher;
  }

  public Matcher<T> getLeftMatcher()
  {
    return leftMatcher;
  }

  public void setLeftMatcher(Matcher<T> leftMatcher)
  {
    this.leftMatcher = leftMatcher;
  }

  public Matcher<T> getRightMatcher()
  {
    return rightMatcher;
  }

  public void setRightMatcher(Matcher<T> rightMatcher)
  {
    this.rightMatcher = rightMatcher;
  }

}
