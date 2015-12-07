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
 * The Class RequestUrlDefaultFiller.
 *
 * @author roger@micromata.de
 */
public class RequestUrlDefaultFiller implements AttributeTypeDefaultFiller
{

  @Override
  public String getValue(LogWriteEntry lwe, LoggingContext ctx)
  {
    if (ctx == null) {
      return null;
    }
    if (lwe.getLevel().getLevel() >= LogLevel.Warn.getLevel()) {
      return ctx.getRequestUrl();
    }
    return null;
  }

}
