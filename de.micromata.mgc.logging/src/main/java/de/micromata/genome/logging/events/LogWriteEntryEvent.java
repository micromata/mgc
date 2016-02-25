package de.micromata.genome.logging.events;

import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.event.MgcFilterEventImpl;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogWriteEntryEvent extends MgcFilterEventImpl<LogWriteEntry> implements LoggingEvent
{

  public LogWriteEntryEvent(LogWriteEntry logWriteEntry)
  {
    super(logWriteEntry);
  }

}
