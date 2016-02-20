package de.micromata.mgc.launcher;

import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValTranslateService;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface MgcApplication<M extends LocalSettingsConfigModel>
{

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
   * TODO RK not yet implemented
   */
  void reInit();

  MgcApplicationStartStopStatus start(String[] args, MgcApplicationStartStopListener listener);

  MgcApplicationStartStopStatus stop(MgcApplicationStartStopListener listener);

  boolean isRunning();

  ValTranslateService getTranslateService();
}
