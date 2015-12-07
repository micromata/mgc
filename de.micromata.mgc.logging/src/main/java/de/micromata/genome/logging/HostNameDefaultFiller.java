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

import de.micromata.genome.util.runtime.HostUtils;

/**
 * Fills current hostname as logattribute.
 * 
 * @author roger@micromata.de
 * 
 */
public class HostNameDefaultFiller implements AttributeTypeDefaultFiller
{

  @Override
  public String getValue(LogWriteEntry lwe, LoggingContext ctx)
  {
    return HostUtils.getThisHostName();
  }

}
