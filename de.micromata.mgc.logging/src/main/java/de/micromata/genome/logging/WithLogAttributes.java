/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   29.03.2008
// Copyright Micromata 29.03.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import java.util.Collection;

/**
 * A Logattribute, which has nested log attributes.
 * 
 * @author roger
 * 
 */
public interface WithLogAttributes
{

  /**
   * Gets the log attributes.
   *
   * @return the log attributes
   */
  public Collection<LogAttribute> getLogAttributes();
}
