package de.micromata.genome.util.matcher;

/**
 * Matches if pattern is equals to token.
 *
 * @author roger
 * @param <T> the generic type
 */
public class EqualsMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -531763040829078392L;

  /**
   * The other.
   */
  private T other;

  /**
   * Instantiates a new equals matcher.
   */
  public EqualsMatcher()
  {

  }

  /**
   * Instantiates a new equals matcher.
   *
   * @param other the other
   */
  public EqualsMatcher(T other)
  {
    this.other = other;
  }

  @Override
  public boolean match(T token)
  {
    if (token == null && other == null) {
      return true;
    }
    if (token == null && other != null) {
      return false;
    }
    if (token != null && other == null) {
      return false;
    }
    return token.equals(other);
  }

  public T getOther()
  {
    return other;
  }

  public void setOther(T other)
  {
    this.other = other;
  }

  @Override
  public String toString()
  {
    return other.toString() + " = <Expr>";
  }
}
