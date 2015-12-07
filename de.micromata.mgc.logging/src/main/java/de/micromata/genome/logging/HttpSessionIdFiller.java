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

import org.apache.commons.lang.StringUtils;

/**
 * Fills session id as logattribute.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HttpSessionIdFiller implements AttributeTypeDefaultFiller
{
  @Override
  public String getValue(LogWriteEntry lwe, LoggingContext ctx)
  {
    if (ctx == null || StringUtils.isEmpty(ctx.getSessionId()) == true) {
      return null;
    }
    return ctx.getSessionId();
  }
}
