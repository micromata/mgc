/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   09.02.2007
// Copyright Micromata 09.02.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.types;

/**
 * Indirection to hold value. May be used where local variable has to be final.
 *
 * @author roger@micromata.de
 * @param <T> the generic type
 */
public class Holder<T>
{

  /**
   * The holded.
   */
  private T holded;

  /**
   * Instantiates a new holder.
   */
  public Holder()
  {
  }

  /**
   * Instantiates a new holder.
   *
   * @param t the t
   */
  public Holder(T t)
  {
    holded = t;
  }

  /**
   * Gets the.
   *
   * @return the t
   */
  public T get()
  {
    return holded;
  }

  /**
   * Sets the.
   *
   * @param t the t
   */
  public void set(T t)
  {
    holded = t;
  }

  public T getHolded()
  {
    return holded;
  }

  public void setHolded(T t)
  {
    holded = t;
  }
}
