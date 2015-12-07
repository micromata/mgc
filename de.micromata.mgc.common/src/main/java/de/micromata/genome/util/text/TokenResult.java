package de.micromata.genome.util.text;

/**
 * The Interface TokenResult.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface TokenResult
{

  /**
   * Gets the token type.
   *
   * @return the token type
   */
  public int getTokenType();

  /**
   * Gets the consumed.
   *
   * @return the consumed
   */
  public String getConsumed();

  /**
   * Gets the consumed length.
   *
   * @return the consumed length
   */
  public int getConsumedLength();

}
