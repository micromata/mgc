/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   20.01.2008
// Copyright Micromata 20.01.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

/**
 * For an AttributeType an default Filler
 * 
 * @author roger@micromata.de
 * 
 */
public interface AttributeTypeDefaultFiller
{
  /**
   * Give a default value for a AttributeType
   * 
   * @param lwe always filled
   * @param ctx may be null
   * @return a string value for the Log Attribute. Return null if attribute should be be set
   */
  public String getValue(LogWriteEntry lwe, LoggingContext ctx);
}
