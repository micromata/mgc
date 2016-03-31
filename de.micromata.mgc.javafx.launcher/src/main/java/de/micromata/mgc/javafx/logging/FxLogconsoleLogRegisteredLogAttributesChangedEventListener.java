package de.micromata.mgc.javafx.logging;

import de.micromata.genome.logging.events.LogRegisteredLogAttributesChangedEvent;
import de.micromata.genome.util.event.MgcEventListener;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class FxLogconsoleLogRegisteredLogAttributesChangedEventListener
    implements MgcEventListener<LogRegisteredLogAttributesChangedEvent>
{

  @Override
  public void onEvent(LogRegisteredLogAttributesChangedEvent event)
  {

    LoggingController instance = LoggingController.getInstance();
    if (instance == null || instance.loggingAdapter == null) {
      return;
    }
    instance.refreshLogConfiguration();

  }
}
