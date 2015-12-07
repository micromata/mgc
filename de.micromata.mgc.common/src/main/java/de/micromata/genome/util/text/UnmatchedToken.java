package de.micromata.genome.util.text;

/**
 * The Class UnmatchedToken.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class UnmatchedToken implements TokenResult
{

  /**
   * The unmatched.
   */
  private String unmatched;

  /**
   * Instantiates a new unmatched token.
   *
   * @param unmatched the unmatched
   */
  public UnmatchedToken(String unmatched)
  {
    this.unmatched = unmatched;
  }

  @Override
  public String toString()
  {
    return unmatched;
  }

  @Override
  public int getTokenType()
  {
    return 0;
  }

  @Override
  public String getConsumed()
  {
    return unmatched;
  }

  @Override
  public int getConsumedLength()
  {
    return unmatched.length();
  }

  public String getUnmatched()
  {
    return unmatched;
  }

  public void setUnmatched(String unmatched)
  {
    this.unmatched = unmatched;
  }

}
