package de.micromata.genome.util.matcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Caching matcher by pattern. The CachedMatcherFactory can held as (static) singleton.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public class CachedMatcherFactory<T>implements MatcherFactory<T>
{

  /**
   * The target.
   */
  private final MatcherFactory<T> target;

  /**
   * The cache.
   */
  private final Map<String, Matcher<T>> cache = Collections.synchronizedMap(new HashMap<String, Matcher<T>>());

  /**
   * Instantiates a new cached matcher factory.
   *
   * @param target the target
   */
  public CachedMatcherFactory(MatcherFactory<T> target)
  {
    this.target = target;
  }

  /**
   * Clear cache.
   */
  public void clearCache()
  {
    cache.clear();
  }

  @Override
  public Matcher<T> createMatcher(String pattern)
  {
    Matcher<T> ret = cache.get(pattern);
    if (ret != null) {
      return ret;
    }
    ret = target.createMatcher(pattern);
    cache.put(pattern, ret);
    return ret;
  }

  @Override
  public String getRuleString(Matcher<T> matcher)
  {
    return target.getRuleString(matcher);
  }

}
