package de.micromata.genome.util.runtime.config;

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
   * When store into properties.
   * 
   * @return
   */
  String getSectionComment();

  /**
   * store the configuration into local settings.
   * 
   * @param props
   * @return if created a new section writer, returned this.
   */
  LocalSettingsWriter toProperties(LocalSettingsWriter writer);

  /**
   * load the configuration from local settings.
   * 
   * @param localSettings
   */
  void fromLocalSettings(LocalSettings localSettings);

  /**
   * Build a key from base key name.
   * 
   * @param key
   * @return
   */
  String buildKey(String key);

  /**
   * get the commented giben by a @ALocalSettingsPath annotion.
   * 
   * @param localProperty/field name
   * @return
   */
  String findCommentForProperty(String localProperty);

}
