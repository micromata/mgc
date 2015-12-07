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
 * Fills the session id.
 * 
 * @author roger@micromata.de
 * 
 */
public class SessionIdDefaultFiller implements AttributeTypeDefaultFiller
{
  @Override
  public String getValue(LogWriteEntry lwe, LoggingContext ctx)
  {
    if (ctx == null) {
      return null;
    }
    return ctx.getSessionId();
  }

}
