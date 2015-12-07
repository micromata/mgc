package de.micromata.genome.util.text;

/**
 * The Class TokenResultBase.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public abstract class TokenResultBase implements TokenResult
{

  /**
   * The token.
   */
  private Token token;

  /**
   * Instantiates a new token result base.
   *
   * @param token the token
   */
  public TokenResultBase(Token token)
  {
    this.token = token;
  }

  @Override
  public int getTokenType()
  {
    return token.getTokenType();
  }

  public Token getToken()
  {
    return token;
  }

  public void setToken(Token token)
  {
    this.token = token;
  }

}
