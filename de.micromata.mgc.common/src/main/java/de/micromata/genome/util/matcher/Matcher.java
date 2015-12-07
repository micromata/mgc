package de.micromata.genome.util.matcher;

import java.io.Serializable;
import java.util.Collection;

/**
 * Base class for matching.
 *
 * @author roger
 * @param <T> the generic type
 */
public interface Matcher<T>extends Serializable
{

  /**
   * Allgemeine Methode um ein Object auf das "Passen" zu überpüfen.
   *
   * @param object Das zu checkende Objekt
   * @return true, falls das Objekt passt
   */
  public boolean match(T object);

  /**
   * similar to match, but return 3 state.
   *
   * @param object the object
   * @return the match result
   */
  public MatchResult apply(T object);

  /**
   * Match any.
   *
   * @param sl the sl
   * @param defaultValue if none are matched, returns defualtValue
   * @return true, if successful
   */
  boolean matchAny(Collection<T> sl, boolean defaultValue);

  /**
   * Match all.
   *
   * @param sl the sl
   * @param defaultValue the default value
   * @return true, if successful
   */
  boolean matchAll(Collection<T> sl, boolean defaultValue);
}
