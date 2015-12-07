package de.micromata.genome.util.matcher.string;

import de.micromata.genome.util.matcher.MatcherBase;

/**
 * The Class StringMatcherBase.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public abstract class StringMatcherBase<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6604905652756969768L;

  /**
   * Match string.
   *
   * @param s the s
   * @return true, if successful
   */
  public abstract boolean matchString(String s);

  @Override
  public boolean match(T o)
  {
    if ((o instanceof String) == false) {
      return false;
    }
    return matchString((String) o);
  }
}
