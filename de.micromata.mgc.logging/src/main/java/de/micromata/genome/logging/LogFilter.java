/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   08.03.2007
// Copyright Micromata 08.03.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import de.micromata.genome.util.matcher.MatcherBase;

/**
 * Interface to filter a Log.
 * 
 * A filter can modify a LogWriteEntry (throw away attributes, etc.(
 * 
 * 
 * 
 * @author roger@micromata.de
 * 
 */
public abstract class LogFilter extends MatcherBase<LogEntry>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -146061547869821581L;

}
