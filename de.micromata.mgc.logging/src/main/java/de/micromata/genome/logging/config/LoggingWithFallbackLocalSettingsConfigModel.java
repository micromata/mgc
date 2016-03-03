package de.micromata.genome.logging.config;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.FallbackLogging;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingWithFallback;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;

/**
 * configure FallbackLogging
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class LoggingWithFallbackLocalSettingsConfigModel extends BaseLoggingLocalSettingsConfigModel
{

  @ALocalSettingsPath
  private String fallbackTypeId;

  private LsLoggingLocalSettingsConfigModel fallbackConfig;

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    String fallbackId = localSettings.get(buildKey(".fallback.typeId"));
    if (StringUtils.isNotBlank(fallbackId) == true) {
      fallbackConfig = new LsLoggingLocalSettingsConfigModel(getKeyPrefix() + "fallback.");
      fallbackConfig.fromLocalSettings(localSettings);
    }
  }

  protected abstract LoggingWithFallback createFallbackLogging();

  @Override
  public Logging createLogging()
  {
    LoggingWithFallback fallbackLogging = createFallbackLogging();
    Logging fallback = fallbackConfig.createLogging();
    fallbackLogging.setSecondary(fallback);

    return fallbackLogging;
  }

}
