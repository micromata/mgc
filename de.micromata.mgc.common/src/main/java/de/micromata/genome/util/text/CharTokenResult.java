package de.micromata.genome.util.text;

/**
 * The Class CharTokenResult.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class CharTokenResult extends TokenResultBase
{

  /**
   * The character.
   */
  private char character;

  /**
   * Instantiates a new char token result.
   *
   * @param token the token
   */
  public CharTokenResult(CharToken token)
  {
    super(token);
    this.character = token.getCharacter();
  }

  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString()
  {
    return getTokenType() + "|char=" + character;
  }

  @Override
  public String getConsumed()
  {
    return Character.toString(character);
  }

  @Override
  public int getConsumedLength()
  {
    return 1;
  }

}
