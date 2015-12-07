package de.micromata.genome.util.text;

/**
 * The Class CharToken.
 *
 * @author roger
 */
public class CharToken extends TokenBase
{

  /**
   * The character.
   */
  private char character;

  /**
   * Instantiates a new char token.
   *
   * @param tokenType the token type
   * @param character the character
   */
  public CharToken(int tokenType, char character)
  {
    super(tokenType);
    this.character = character;
  }

  /**
   * Instantiates a new char token.
   */
  public CharToken()
  {

  }

  @Override
  public TokenResult consume(String text, char escapeChar)
  {
    if (text.length() > 0 && text.charAt(0) == character) {
      return new CharTokenResult(this);
    }
    return null;
  }

  @Override
  public boolean match(String text)
  {
    return text.length() > 0 && text.charAt(0) == character;
  }

  public char getCharacter()
  {
    return character;
  }

  public void setCharacter(char character)
  {
    this.character = character;
  }

}
