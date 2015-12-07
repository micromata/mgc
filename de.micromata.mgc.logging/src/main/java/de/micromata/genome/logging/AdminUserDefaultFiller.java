/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   19.03.2008
// Copyright Micromata 19.03.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

/**
 * If an Adminuser exists, automatically put admin users name.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class AdminUserDefaultFiller implements AttributeTypeDefaultFiller
{
  /**
   * 
   * {@inheritDoc}
   *
   */
  @Override
  public String getValue(LogWriteEntry lwe, LoggingContext ctx)
  {
    return LoggingServiceManager.get().getLoggingContextService().getCurrentUserName();
  }
}
