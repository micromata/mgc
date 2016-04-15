/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   17.02.2007
// Copyright Micromata 17.02.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.spi.jdbc;

/**
 * Can be transferted to a string representation.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface Stringifiable
{

  /**
   * As string.
   *
   * @return the string
   */
  public String asString();
}
