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
  <T extends LocalSettingsConfigModel> T castTo(Class<T> other);
}
