package de.micromata.genome.util.runtime.config;

/**
 * Cast one model to another.
 * 
 * This will used for composed configurations.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface CastableLocalSettingsConfigModel extends LocalSettingsConfigModel
{
  /**
   * Get a sub node.
   * 
   * @param other
   * @return
   */
  <T extends LocalSettingsConfigModel> T castTo(Class<T> other);

  /**
   * Should a configuration dialog tag created
   * 
   * @param other
   * @return
   */
  default <T extends LocalSettingsConfigModel> T castToForConfigDialog(Class<T> other)
  {
    return castTo(other);
  }

}
