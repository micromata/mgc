package de.micromata.mgc.launcher;

import java.lang.Thread.UncaughtExceptionHandler;

import de.micromata.genome.util.i18n.I18NTranslationProvider;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;

/**
 * An application with configuration.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface MgcApplication<M extends LocalSettingsConfigModel>
{
  MgcApplicationInfo getApplicationInfo();

  /**
   * Initialize after creating instance.
   */
  void initializeAfterConstruction();

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

  /**
   * Starting the application.
   *
   * @param args the args
   * @return the mgc application start stop status
   */
  MgcApplicationStartStopStatus start(String[] args);

  /**
   * Stop the application.
   *
   * @return the mgc application start stop status
   */
  MgcApplicationStartStopStatus stop();

  /**
   * checks if application is running. Preventing to start application twice.
   *
   * @return true, if is running
   */
  boolean isRunning();

  /**
   * Use to translate gui and validation.
   *
   * @return the translate service
   */
  I18NTranslationProvider getTranslateService();

  /**
   * The official url. Use to launch an browser.
   * 
   * @return may be null or empty.
   */
  String getPublicUrl();

  UncaughtExceptionHandler getUncaughtExceptionHandler();
}
