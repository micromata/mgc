package de.micromata.genome.util.matcher;

/**
 * The Class AndMatcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public class AndMatcher<T>extends LeftRightMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -2200236053889779000L;

  /**
   * Instantiates a new and matcher.
   *
   * @param leftMatcher the left matcher
   * @param rightMatcher the right matcher
   */
  public AndMatcher(Matcher<T> leftMatcher, Matcher<T> rightMatcher)
  {
    super(leftMatcher, rightMatcher);
  }

  @Override
  public boolean match(T object)
  {
    if (leftMatcher.match(object) == false) {
      return false;
    }
    return rightMatcher.match(object);
  }

  @Override
  public String toString()
  {
    return "(" + leftMatcher.toString() + ") && (" + rightMatcher.toString() + ")";
  }

}
