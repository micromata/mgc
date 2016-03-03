package de.micromata.genome.logging.config;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.spi.LoggingWrapper;
import de.micromata.genome.util.runtime.LocalSettings;

/**
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
    // TODO RK NPE
    setTarget(logging);
  }
}
