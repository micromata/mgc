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

package de.micromata.genome.logging.config;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.spi.LoggingWrapper;
import de.micromata.genome.logging.spi.log4j.GLogAppender;
import de.micromata.genome.logging.spi.log4j.Log4JLogAttributeType;
import de.micromata.genome.logging.spi.log4j.Log4JLogCategory;
import de.micromata.genome.util.runtime.LocalSettings;

/**
 * Initialize from local settings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsLoggingImpl extends LoggingWrapper
{

  public LsLoggingImpl()
  {
    super();
    init();
  }

  public LsLoggingImpl(Logging target)
  {
    super(target);
  }

  public LsLoggingImpl(LocalSettings localSettings)
  {
    super();
    init(localSettings);
  }

  protected void init()
  {
    init(LocalSettings.get());
  }

  protected void init(LocalSettings ls)
  {
    LsLoggingLocalSettingsConfigModel cfgModel = new LsLoggingLocalSettingsConfigModel();
    cfgModel.fromLocalSettings(ls);
    Logging logging = cfgModel.createLogging();
    setTarget(logging);

    if (cfgModel.isLog4JToGenomeLogging() == true) {
      Log4JLogCategory.values();
      Log4JLogAttributeType.values();
      new GLogAppender().register();
    }

  }
}
