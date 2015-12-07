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
 * Fills current executed sql statement, if set.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class CurrentSqlFiller implements AttributeTypeDefaultFiller
{
  @Override
  public String getValue(LogWriteEntry lwe, LoggingContext ctx)
  {
    if (ctx == null || lwe.getLevel().getLevel() < LogLevel.Warn.getLevel()) {
      return null;
    }
    LogSqlAttribute attr = new LogSqlAttribute(ctx.getCurrentSql(), ctx.getCurrentSqlArgs());
    return attr.getValue();
  }
}
