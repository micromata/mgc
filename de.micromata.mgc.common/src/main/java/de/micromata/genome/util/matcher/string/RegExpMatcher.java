package de.micromata.genome.util.matcher.string;

import java.util.regex.Pattern;

/**
 * Matches if regular expression pattern matches.
 *
 * @author roger
 * @param <T> the generic type
 */
public class RegExpMatcher<T>extends StringMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -4826993544156110602L;

  /**
   * The pattern.
   */
  private Pattern pattern;

  /**
   * Instantiates a new reg exp matcher.
   */
  public RegExpMatcher()
  {
  }

  /**
   * Instantiates a new reg exp matcher.
   *
   * @param pattern the pattern
   */
  public RegExpMatcher(Pattern pattern)
  {
    this.pattern = pattern;

  }

  /**
   * Instantiates a new reg exp matcher.
   *
   * @param pattern the pattern
   */
  public RegExpMatcher(String pattern)
  {
    this.pattern = Pattern.compile(pattern);
  }

  @Override
  public boolean matchString(String token)
  {
    return pattern.matcher(token).matches();
  }

  @Override
  public String toString()
  {
    return "<EXPR>.regexp(" + pattern.toString() + ")";
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
