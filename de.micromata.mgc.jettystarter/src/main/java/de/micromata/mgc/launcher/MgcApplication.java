package de.micromata.mgc.launcher;

import de.micromata.genome.util.i18n.I18NTranslationProvider;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface MgcApplication<M extends LocalSettingsConfigModel>
{
  MgcApplicationInfo getApplicationInfo();

  /**
   * get the current configuration model
   * 
   * @return
   */
  M getConfigModel();

  /**
   * Loads a new Configuration model from persistence.
   * 
   * @return
   */
  M loadConfigModel();

  /**
   * Persist configuration.
   */
  void storeConfig(ValContext ctx, M config);

  /**
   * realod config intialize application.
   * 
   * 
   */
  void reInit();

  MgcApplicationStartStopStatus start(String[] args);

  MgcApplicationStartStopStatus stop();

  boolean isRunning();

  I18NTranslationProvider getTranslateService();

  /**
   * The official url.
   * 
   * @return may be null or empty.
   */
  String getPublicUrl();
}
