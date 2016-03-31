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
