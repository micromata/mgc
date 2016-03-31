package de.micromata.genome.logging.events;

import java.util.Map;

import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.event.MgcFilterEventImpl;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogRegisteredLogAttributesChangedEvent extends MgcFilterEventImpl<LogWriteEntry> implements LoggingEvent
{

  Map<String, LogAttributeType> logAttributes;

  public LogRegisteredLogAttributesChangedEvent(Map<String, LogAttributeType> logAttributes)
  {
    this.logAttributes = logAttributes;
  }

  public Map<String, LogAttributeType> getLogAttributes()
  {
    return logAttributes;
  }

}
