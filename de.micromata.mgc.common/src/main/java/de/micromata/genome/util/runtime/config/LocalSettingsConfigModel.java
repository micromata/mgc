package de.micromata.genome.util.runtime.config;

import java.util.Map;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.validation.ValContext;

/**
 * Maps a localsettings to a model.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface LocalSettingsConfigModel
{
  /**
   * Validate the model.
   * 
   * @param ctx
   */
  public void validate(ValContext ctx);

  /**
   * store the configuration into local settings.
   * 
   * @param props
   */
  public default void toProperties(Map<String, String> props)
  {
    LocalSettingsConfigUtils.toProperties(this, props);
  }

  /**
   * load the configuration from local settings.
   * 
   * @param localSettings
   */
  public default void fromLocalSettings(LocalSettings localSettings)
  {
    LocalSettingsConfigUtils.initFromLocalSettings(this, localSettings);
  }
}
