package de.micromata.genome.util.matcher;

/**
 * Matches always.
 *
 * @author roger
 * @param <T> the generic type
 */
public class EveryMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -6603198094053117381L;

  /**
   * The singleton.
   */
  private static EveryMatcher<?> SINGLETON = new EveryMatcher<Object>();

  /**
   * Single instanceof.
   *
   * @param <T> the generic type
   * @return the every matcher
   */
  @SuppressWarnings("unchecked")
  public static <T> EveryMatcher<T> everyMatcher()
  {
    return (EveryMatcher<T>) SINGLETON;
  }

  @Override
  public boolean match(T token)
  {
    return true;
  }

  @Override
  public String toString()
  {
    return "*";
  }

}
