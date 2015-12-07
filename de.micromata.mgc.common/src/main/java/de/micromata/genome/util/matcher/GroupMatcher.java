package de.micromata.genome.util.matcher;

/**
 * Just a grouped matcher.
 *
 * @author roger@micromata.de
 * @param <T> the generic type
 */
public class GroupMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -3472867724633767575L;

  /**
   * The grouped matcher.
   */
  private Matcher<T> groupedMatcher;

  /**
   * Instantiates a new group matcher.
   */
  public GroupMatcher()
  {

  }

  /**
   * Instantiates a new group matcher.
   *
   * @param groupedMatcher the grouped matcher
   */
  public GroupMatcher(Matcher<T> groupedMatcher)
  {
    this.groupedMatcher = groupedMatcher;
  }

  @Override
  public boolean match(T object)
  {
    return groupedMatcher.match(object);
  }

  @Override
  public String toString()
  {
    return "(" + groupedMatcher.toString() + ")";
  }
}
