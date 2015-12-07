package de.micromata.genome.util.matcher.norm;

import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.MatcherBase;

/**
 * The Class NormalizedMatcher.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> only works with String
 * @see StringNormalizeUtils
 */
public class NormalizedMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -1951174536024791603L;

  /**
   * The flags.
   */
  private String flags;

  /**
   * The child.
   */
  private Matcher<T> child;

  /**
   * Instantiates a new normalized matcher.
   *
   * @param flags the flags
   * @param child the child
   */
  public NormalizedMatcher(String flags, Matcher<T> child)
  {
    this.flags = flags;
    this.child = child;
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.Matcher#match(java.lang.Object)
   */
  @Override
  public boolean match(T object)
  {
    if (object instanceof String) {
      String normobj = StringNormalizeUtils.normalize((String) object, flags);
      return child.match((T) normobj);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "<EXPR>.normalizedMatch(" + flags + ", " + child.toString() + ")";
  }

  public Matcher<T> getChild()
  {
    return child;
  }

  public String getFlags()
  {
    return flags;
  }

}
