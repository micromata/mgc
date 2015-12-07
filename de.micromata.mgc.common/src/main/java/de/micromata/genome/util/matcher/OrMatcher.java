/*
 * 
 */
package de.micromata.genome.util.matcher;

/**
 * The Class OrMatcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public class OrMatcher<T>extends LeftRightMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -1065235686007587539L;

  /**
   * Instantiates a new or matcher.
   *
   * @param leftMatcher the left matcher
   * @param rightMatcher the right matcher
   */
  public OrMatcher(Matcher<T> leftMatcher, Matcher<T> rightMatcher)
  {
    super(leftMatcher, rightMatcher);
  }

  @Override
  public boolean match(T object)
  {
    if (leftMatcher.match(object) == true) {
      return true;
    }
    return rightMatcher.match(object);
  }

  @Override
  public String toString()
  {
    return "(" + leftMatcher.toString() + ") || (" + rightMatcher.toString() + ")";
  }
}
