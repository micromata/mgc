package de.micromata.mgc.javafx.logging;

import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.logging.events.LogWriteEntryEvent;
import de.micromata.genome.util.event.MgcEventListener;

public class FxLogconsoleLogWriteEntryEventListener implements MgcEventListener<LogWriteEntryEvent>
{
  @Override
  public void onEvent(LogWriteEntryEvent event)
  {
    LoggingController instance = LoggingController.getInstance();
    if (instance != null) {
      instance.doLogImpl(event.getResult());
    }
    event.nextFilter();

  }

  public static void registerEvent()
  {
    LoggingServiceManager.get().getLoggingEventListenerRegistryService()
        .registerListener(FxLogconsoleLogWriteEntryEventListener.class);
  }

}
