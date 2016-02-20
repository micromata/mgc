package de.micromata.mgc.javafx.launcher;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.javafx.FXEvents;
import de.micromata.mgc.launcher.MgcApplication;
import de.micromata.mgc.launcher.MgcApplicationStartStopListener;
import de.micromata.mgc.launcher.MgcApplicationStartStopStatus;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MgcApplicationStartStopToEventListener implements MgcApplicationStartStopListener
{

  @Override
  public void listen(MgcApplication<?> application, MgcApplicationStartStopStatus status, ValMessage msg)
  {
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
