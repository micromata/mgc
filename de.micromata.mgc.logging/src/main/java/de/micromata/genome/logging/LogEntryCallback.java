/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   19.03.2007
// Copyright Micromata 19.03.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

/**
 * Callback which will be called on every fetched LogEntry.
 *
 * @author roger
 */
public interface LogEntryCallback
{

  /**
   * Called on each row.
   *
   * @param le the le
   * @throws EndOfSearch if seach should be terminated
   */
  public void onRow(LogEntry le) throws EndOfSearch;

  /**
   * create a log entry on base a log entry.
   * 
   * This can also simply return the same LogEntry
   *
   * @param le the le
   * @return the log entry
   */
  public LogEntry createLogEntry(LogEntry le);
}
