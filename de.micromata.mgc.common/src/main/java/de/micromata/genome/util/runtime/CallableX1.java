/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   12.12.2009
// Copyright Micromata 12.12.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.runtime;

/**
 * The Interface CallableX1.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <V> the value type
 * @param <ARG1> the generic type
 * @param <EX> the generic type
 */
public interface CallableX1<V, ARG1, EX extends Throwable>
{

  /**
   * Call.
   *
   * @param arg1 the arg1
   * @return the v
   * @throws EX the ex
   */
  public V call(ARG1 arg1) throws EX;
}
