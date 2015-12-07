package de.micromata.genome.util.matcher;

/**
 * Matches if token is null.
 *
 * @author roger
 * @param <T> the generic type
 */
public class IsNullMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -6603198094053117381L;

  @Override
  public boolean match(T token)
  {
    return token == null;
  }

  @Override
  public String toString()
  {
    return "<expr>.isNull()";
  }

}
