package de.micromata.genome.util.text;

import java.util.regex.Matcher;

/**
 * The Class RegExpTokenResult.
 *
 * @author roger
 */
public class RegExpTokenResult extends TokenResultBase
{

  /**
   * The reg exp matcher.
   */
  private Matcher regExpMatcher;

  /**
   * The matched.
   */
  private String matched;

  /**
   * Instantiates a new reg exp token result.
   *
   * @param regExpToken the reg exp token
   * @param regExpMatcher the reg exp matcher
   * @param matched the matched
   */
  public RegExpTokenResult(RegExpToken regExpToken, Matcher regExpMatcher, String matched)
  {
    super(regExpToken);
    this.regExpMatcher = regExpMatcher;
    this.matched = matched;
  }

  @Override
  public String toString()
  {
    return getTokenType() + "|pattern=" + regExpMatcher.pattern().toString() + "|matched=" + matched;
  }

  @Override
  public String getConsumed()
  {
    return matched;
  }

  @Override
  public int getConsumedLength()
  {
    int gc = regExpMatcher.groupCount();
    if (gc > 1) {
      return regExpMatcher.start(2);
    }
    return getConsumed().length();
  }

  public Matcher getRegExpMatcher()
  {
    return regExpMatcher;
  }

  public void setRegExpMatcher(Matcher regExpMatcher)
  {
    this.regExpMatcher = regExpMatcher;
  }

}
