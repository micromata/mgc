package de.micromata.genome.util.collections;

import java.util.AbstractMap;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;

import de.micromata.genome.util.types.Pair;

/**
 * Map implemented on base of a ArrayList.
 * 
 * Insert order is stable. Only use this for a few elements, because key access is always full seek in list.
 *
 * @author roger
 * @param <K> the key type
 * @param <V> the value type
 */
public class ArrayMap<K, V>extends AbstractMap<K, V>
{

  /**
   * The entries.
   */
  private ArraySet<Entry<K, V>> entries = new ArraySet<Entry<K, V>>();

  @Override
  public boolean containsKey(Object key)
  {
    for (Entry<K, V> me : entries) {
      if (ObjectUtils.equals(key, me.getKey())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsValue(Object value)
  {
    for (Entry<K, V> me : entries) {
      if (ObjectUtils.equals(value, me.getValue())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Set<java.util.Map.Entry<K, V>> entrySet()
  {
    return entries;
  }

  @Override
  public V put(K key, V value)
  {
    for (Entry<K, V> me : entries) {
      if (ObjectUtils.equals(me.getKey(), key) == true) {
        V rv = me.getValue();
        me.setValue(value);
        return rv;
      }
    }
    entries.add(new Pair<K, V>(key, value));
    return null;
  }

  public ArraySet<Entry<K, V>> getEntries()
  {
    return entries;
  }

  public void setEntries(ArraySet<Entry<K, V>> entries)
  {
    this.entries = entries;
  }
}
