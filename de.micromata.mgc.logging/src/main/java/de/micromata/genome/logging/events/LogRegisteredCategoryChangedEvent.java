package de.micromata.genome.logging.events;

import java.util.Map;

import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.event.MgcFilterEventImpl;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogRegisteredCategoryChangedEvent extends MgcFilterEventImpl<LogWriteEntry> implements LoggingEvent
{
  private Map<String, LogCategory> registerdLogCategories;

  public LogRegisteredCategoryChangedEvent(Map<String, LogCategory> registerdLogCategories)
  {
    this.registerdLogCategories = registerdLogCategories;
  }

  public Map<String, LogCategory> getRegisterdLogCategories()
  {
    return registerdLogCategories;
  }

}
