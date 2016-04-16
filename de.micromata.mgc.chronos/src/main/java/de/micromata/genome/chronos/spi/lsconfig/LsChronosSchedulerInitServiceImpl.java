package de.micromata.genome.chronos.spi.lsconfig;

import de.micromata.genome.chronos.manager.ChronosSchedulerInitService;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.ValMessageLogAttribute;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.validation.ValContext;

/**
 * Configure initial Scheduler via LocalSettings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsChronosSchedulerInitServiceImpl implements ChronosSchedulerInitService
{

  @Override
  public void initSchedulerManager(SchedulerManager schedManager)
  {
    ChronosLocalSettingsConfigModel cm = new ChronosLocalSettingsConfigModel();
    try {
      cm.fromLocalSettings(LocalSettings.get());
      ValContext ctx = new ValContext();
      cm.validate(ctx);
      if (ctx.hasErrors() == true) {
        GLog.error(GenomeLogCategory.Configuration, "Ls Chronos Config not valid",
            new ValMessageLogAttribute(ctx.getMessages()));
        return;
      }
      copyAddSchedulerManager(cm.getManager(), schedManager);
    } catch (RuntimeException ex) {
      GLog.error(GenomeLogCategory.Configuration, "Invalid Ls Chronos Config: " + ex.getMessage(),
          new LogExceptionAttribute(ex));
    }
  }

}
