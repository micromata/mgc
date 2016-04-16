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
