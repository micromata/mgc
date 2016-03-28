package de.micromata.genome.logging.loghtmlwindow;

import de.micromata.genome.logging.events.LogWriteEntryEvent;
import de.micromata.genome.util.event.MgcEventListener;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogHtmlLiveBuffer implements MgcEventListener<LogWriteEntryEvent>
{

  @Override
  public void onEvent(LogWriteEntryEvent event)
  {
    if (LogHtmlWindowServlet.INSTANCE != null) {
      LogHtmlWindowServlet.INSTANCE.addLogEntry(event.getResult());
    }
  }

}
