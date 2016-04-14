//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.mgc.javafx.launcher;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.application.AbstractMgcApplicationStartStopListener;
import de.micromata.mgc.application.MgcApplicationStartStopEvent;
import de.micromata.mgc.application.MgcApplicationStartStopStatus;
import de.micromata.mgc.javafx.FXEvents;

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
        FXEvents.get().fireEvent(new MgcLauncherEvent(MgcLauncherEvent.APP_STOPPED, status, msg));
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
