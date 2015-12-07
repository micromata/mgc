package de.micromata.genome.util.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class RegExpToken.
 *
 * @author roger
 */
public class RegExpToken extends TokenBase
{

  /**
   * The pattern.
   */
  private Pattern pattern;

  /**
   * Instantiates a new reg exp token.
   *
   * @param tokenType the token type
   * @param patternString the pattern string
   */
  public RegExpToken(int tokenType, String patternString)
  {
    super(tokenType);
    pattern = Pattern.compile(patternString);
  }

  @Override
  public boolean match(String text)
  {
    Matcher m = pattern.matcher(text);

    boolean doMatch = m.matches() == true;
    return doMatch;
  }

  @Override
  public TokenResult consume(String text, char escapeChar)
  {
    Matcher matcher = pattern.matcher(text);
    Matcher mr = matcher;// .toMatchResult();
    mr.find();
    if (mr.groupCount() < 2) {
      return null;
    }
    String matched = mr.group(1);

    return new RegExpTokenResult(this, matcher, matched);
  }

  @Override
  public String toString()
  {
    return pattern.toString();
  }

  public Pattern getPattern()
  {
    return pattern;
  }

  public void setPattern(Pattern pattern)
  {
    this.pattern = pattern;
  }

}
