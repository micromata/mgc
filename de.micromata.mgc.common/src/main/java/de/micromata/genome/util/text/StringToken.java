package de.micromata.genome.util.text;

/**
 * The Class StringToken.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class StringToken extends TokenBase
{

  /**
   * The pattern.
   */
  private String pattern;

  /**
   * Instantiates a new string token.
   *
   * @param tokenType the token type
   * @param pattern the pattern
   */
  public StringToken(int tokenType, String pattern)
  {
    super(tokenType);
    this.pattern = pattern;
  }

  /**
   * Instantiates a new string token.
   */
  public StringToken()
  {

  }

  @Override
  public TokenResult consume(String text, char escapeChar)
  {
    if (text.startsWith(pattern) == true) {
      return new StringTokenResult(this);
    }

    return null;
  }

  @Override
  public boolean match(String text)
  {
    return text.startsWith(pattern) == true;
  }

  public String getPattern()
  {
    return pattern;
  }

  public void setPattern(String pattern)
  {
    this.pattern = pattern;
  }
}
