package de.micromata.genome.util.matcher;

/**
 * Matches if nested matcher does not matches.
 *
 * @author roger
 * @param <T> the generic type
 */
public class NotMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 4329354733404236006L;

  /**
   * The nested.
   */
  private Matcher<T> nested;

  /**
   * Instantiates a new not matcher.
   */
  public NotMatcher()
  {

  }

  /**
   * Instantiates a new not matcher.
   *
   * @param nested the nested
   */
  public NotMatcher(Matcher<T> nested)
  {
    this.nested = nested;
  }

  @Override
  public boolean match(T token)
  {
    return nested.match(token) == false;
  }

  public Matcher<T> getNested()
  {
    return nested;
  }

  public void setNested(Matcher<T> nested)
  {
    this.nested = nested;
  }

  @Override
  public String toString()
  {
    return "!" + nested.toString();
  }

}
