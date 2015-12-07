package de.micromata.genome.util.matcher;

/**
 * Never matches. equivalent to false.
 *
 * @author roger
 * @param <T> the generic type
 */
public class NoneMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5981777834465779179L;

  @Override
  public boolean match(T token)
  {
    return false;
  }

  @Override
  public String toString()
  {
    return "<NEVER>";
  }
}
