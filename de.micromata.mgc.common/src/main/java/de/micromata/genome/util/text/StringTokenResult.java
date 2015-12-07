package de.micromata.genome.util.text;

/**
 * The Class StringTokenResult.
 *
 * @author roger
 */
public class StringTokenResult extends TokenResultBase
{

  /**
   * The pattern.
   */
  private String pattern;

  /**
   * Instantiates a new string token result.
   *
   * @param token the token
   */
  public StringTokenResult(StringToken token)
  {
    super(token);
    this.pattern = token.getPattern();
  }

  @Override
  public String toString()
  {
    return getTokenType() + "|string=" + pattern;
  }

  @Override
  public String getConsumed()
  {
    return pattern;
  }

  @Override
  public int getConsumedLength()
  {
    return pattern.length();
  }
}
