package de.micromata.mgc.javafx.logging;

import de.micromata.genome.logging.events.LogRegisteredCategoryChangedEvent;
import de.micromata.genome.util.event.MgcEventListener;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class FxLogconsoleLogRegisteredCategoryChangedEventListener
    implements MgcEventListener<LogRegisteredCategoryChangedEvent>
{

  @Override
  public void onEvent(LogRegisteredCategoryChangedEvent event)
  {
    LoggingController instance = LoggingController.getInstance();
    if (instance == null || instance.loggingAdapter == null) {
      return;
    }
    instance.refreshLogConfiguration();
  }

}
