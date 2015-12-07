package de.micromata.genome.util.matcher;

/**
 * Matches if all matcher matches (all AndMatcher).
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public class AllMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6759388753435464468L;

  /**
   * The matchers.
   */
  private Iterable<Matcher<? super T>> matchers;

  /**
   * Instantiates a new all matcher.
   *
   * @param matchers the matchers
   */
  public AllMatcher(Iterable<Matcher<? super T>> matchers)
  {
    this.matchers = matchers;
  }

  @Override
  public boolean match(T object)
  {
    for (Matcher<? super T> m : matchers) {
      if (m.match(object) == false) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("allof(");
    boolean isFirst = true;
    for (Matcher<? super T> m : matchers) {
      if (isFirst == false) {
        sb.append(", ");
      }
      isFirst = false;
      sb.append(m);
    }
    sb.append(")");
    return sb.toString();
  }
}
