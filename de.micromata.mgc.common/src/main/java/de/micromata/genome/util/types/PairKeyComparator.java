package de.micromata.genome.util.types;

import java.util.Comparator;

/**
 * Comparator to compare keys
 * 
 * @author roger@micromata.de
 * 
 */
public class PairKeyComparator<K extends Comparable<K>, V> implements Comparator<Pair<K, V>>
{

  @Override
  public int compare(Pair<K, V> o1, Pair<K, V> o2)
  {
    K k1 = o1.getFirst();
    K k2 = o2.getFirst();
    if (k1 == null) {
      return 1;
    }
    if (k2 == null) {
      return -1;
    }
    return k1.compareTo(k2);
  }

}