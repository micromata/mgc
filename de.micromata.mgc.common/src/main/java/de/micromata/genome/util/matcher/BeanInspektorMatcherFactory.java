package de.micromata.genome.util.matcher;

import org.apache.commons.lang.StringUtils;

/**
 * A factory for creating BeanInspektorMatcher objects.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class BeanInspektorMatcherFactory implements MatcherFactory<Object>
{

  /**
   * Creates a new BeanInspektorMatcher object.
   *
   * @param pattern the pattern
   * @return the matcher< object>
   */
  @Override
  public Matcher<Object> createMatcher(String pattern)
  {
    String matcherString = StringUtils.trim(StringUtils.substringBefore(pattern, "="));
    String valueString = StringUtils.trimToNull(StringUtils.substringAfter(pattern, "="));

    if (matcherString.trim().equals("instanceOf")) {
      try {
        // TODO (RK) wirklich nur von root classloader, nicht thread?
        return new BeanInstanceOfMatcher(Class.forName(valueString.trim()));
      } catch (Exception ex) {
        throw new RuntimeException(ex); // TODO better ex
      }
    }
    return new BeanPropertiesMatcher(matcherString, valueString);
  }

  /**
   * Gets the rule string.
   *
   * @param matcher the matcher
   * @return the rule string
   */
  @Override
  public String getRuleString(Matcher<Object> matcher)
  {
    if (matcher instanceof BeanInstanceOfMatcher) {
      return "instanceOf=" + ((BeanInstanceOfMatcher) matcher).getOfClass();
    }
    if (matcher instanceof BeanPropertiesMatcher) {
      BeanPropertiesMatcher bpm = ((BeanPropertiesMatcher) matcher);
      return bpm.getProperty() + "=" + bpm.getValue();
    }
    return "<unknown>";
  }
}
