package de.micromata.genome.logging.config;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingWithFallback;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;

/**
 * configure FallbackLogging
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class LoggingWithFallbackLocalSettingsConfigModel extends BaseLoggingLocalSettingsConfigModel
{

  private String fallbackTypeId;

  private LsLoggingLocalSettingsConfigModel fallbackConfig;

  protected abstract LoggingWithFallback createFallbackLogging();

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    String fallbackId = localSettings.get(buildKey("fallback.typeId"));
    if (StringUtils.isNotBlank(fallbackId) == true) {
      fallbackConfig = new LsLoggingLocalSettingsConfigModel(getKeyPrefix() + "fallback.");
      fallbackConfig.fromLocalSettings(localSettings);
    }
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    LocalSettingsWriter ret = super.toProperties(writer);
    if (fallbackConfig != null) {
      fallbackConfig.toProperties(ret);
    }
    return ret;
  }

  @Override
  public Logging createLogging()
  {
    LoggingWithFallback fallbackLogging = createFallbackLogging();
    if (fallbackConfig != null) {
      Logging fallback = fallbackConfig.createLogging();
      fallbackLogging.setSecondary(fallback);
    }
    return fallbackLogging;
  }

  public String getFallbackTypeId()
  {
    return fallbackTypeId;
  }

  public void setFallbackTypeId(String fallbackTypeId)
  {
    this.fallbackTypeId = fallbackTypeId;
  }

  public LsLoggingLocalSettingsConfigModel getFallbackConfig()
  {
    return fallbackConfig;
  }

  public void setFallbackConfig(LsLoggingLocalSettingsConfigModel fallbackConfig)
  {
    this.fallbackConfig = fallbackConfig;
  }

}
