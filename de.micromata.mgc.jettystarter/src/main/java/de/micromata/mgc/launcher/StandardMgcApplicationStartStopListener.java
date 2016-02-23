package de.micromata.mgc.launcher;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.util.validation.ValMessage;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class StandardMgcApplicationStartStopListener implements MgcApplicationStartStopListener
{

  @Override
  public void listen(MgcApplication<?> application, MgcApplicationStartStopStatus status, ValMessage msg)
  {

    // TODO invoke event, so buttons can react.
    msg.getTranslatedMessage(application.getTranslateService());
    GLog.logValMessage(GenomeLogCategory.System, msg);
  }

}
