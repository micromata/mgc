package de.micromata.genome.util.matcher;

/**
 * A Factory, which creates a Matcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public interface MatcherFactory<T>
{

  /**
   * Creates a new Matcher object from Pattern.
   *
   * @param pattern the pattern
   * @return the matcher< t>
   */
  public Matcher<T> createMatcher(String pattern);

  /**
   * Gets the rule string from Matcher.
   *
   * @param matcher the matcher
   * @return the rule string
   */
  public String getRuleString(Matcher<T> matcher);
}
