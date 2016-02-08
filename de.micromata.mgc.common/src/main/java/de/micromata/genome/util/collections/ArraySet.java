package de.micromata.genome.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Add items to a ArrayList, but discarges duplicated elements.
 *
 * @author roger
 * @param <T> the generic type
 */
public class ArraySet<T>extends ArrayList<T> implements Set<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -1762503806954764180L;

  /**
   * Instantiates a new array set.
   */
  public ArraySet()
  {
    super();
  }

  /**
   * Instantiates a new array set.
   *
   * @param c the c
   */
  public ArraySet(Collection<? extends T> c)
  {
    super(c.size());
    for (T e : c) {
      add(e);
    }
  }

  /**
   * Instantiates a new array set.
   *
   * @param initialCapacity the initial capacity
   */
  public ArraySet(int initialCapacity)
  {
    super(initialCapacity);
  }

  @Override
  public boolean add(T e)
  {
    if (contains(e) == true) {
      return false;
    }
    return super.add(e);
  }

}
