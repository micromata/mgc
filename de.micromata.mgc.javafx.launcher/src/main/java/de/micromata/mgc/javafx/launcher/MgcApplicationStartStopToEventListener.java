package de.micromata.mgc.javafx.launcher;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.launcher.AbstractMgcApplicationStartStopListener;
import de.micromata.mgc.launcher.MgcApplicationStartStopEvent;
import de.micromata.mgc.launcher.MgcApplicationStartStopStatus;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MgcApplicationStartStopToEventListener extends AbstractMgcApplicationStartStopListener
{

  @Override
  public void onEvent(MgcApplicationStartStopEvent event)
  {
    ValMessage msg = event.getValMessage();
    MgcApplicationStartStopStatus status = event.getStatus();
    msg.getTranslatedMessage(event.getApplication().getTranslateService());
    GLog.logValMessage(GenomeLogCategory.System, msg);
    switch (status) {
      case StartError:
      case StartNoConfiguration:
        break;
      case StartAlreadyRunning:
      case StartSuccess:
        FXEvents.get().fireEvent(new MgcLauncherEvent(MgcLauncherEvent.APP_STARTED, status, msg));
        break;

      case StopError:
        break;
      case StopSuccess:
      case StopAlreadyStopped:
        FXEvents.get().fireEvent(new MgcLauncherEvent(MgcLauncherEvent.APP_STOPPED, status, msg));
    }

  }

}
