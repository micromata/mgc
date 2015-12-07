package de.micromata.genome.util.text;

/**
 * The Class TokenBase.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public abstract class TokenBase implements Token
{

  /**
   * The token type.
   */
  private int tokenType;

  /**
   * Instantiates a new token base.
   */
  public TokenBase()
  {

  }

  /**
   * Instantiates a new token base.
   *
   * @param tokenType the token type
   */
  public TokenBase(int tokenType)
  {
    this.tokenType = tokenType;
  }

  @Override
  public int getTokenType()
  {
    return tokenType;
  }

  public void setTokenType(int tokenType)
  {
    this.tokenType = tokenType;
  }

}
