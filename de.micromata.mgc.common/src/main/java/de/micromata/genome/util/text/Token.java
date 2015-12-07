package de.micromata.genome.util.text;

/**
 * The Interface Token.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface Token
{
  public int getTokenType();

  /**
   * return true if begin of text a token was found.
   *
   * @param text the text
   * @return true, if successful
   */
  public boolean match(String text);

  /**
   * Consume.
   *
   * @param text the text
   * @param escapeChar the escape char
   * @return the token result
   */
  public TokenResult consume(String text, char escapeChar);

}
