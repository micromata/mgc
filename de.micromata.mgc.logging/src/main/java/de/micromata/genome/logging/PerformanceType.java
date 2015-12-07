// ///////////////////////////////////////////////////////////////////////////
//
// Project DHL-ParcelOnlinePostage
//
// Author roger@micromata.de
// Created 19.08.2006
// Copyright Micromata 19.08.2006
//
// ///////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

/**
 * Type of Performance.
 * 
 * Commonly implemented via an enumeration.
 * 
 * @author roger@micromata.de
 * 
 */
public interface PerformanceType
{

  /**
   * Name.
   *
   * @return the string
   */
  public String name();

  /**
   * For which category.
   *
   * @return the stats category
   */
  public LogCategory getStatsCategory();

}
