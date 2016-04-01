package de.micromata.mgc.application;

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
   * Check the configuration, if it is valid.
   * 
   * @return
   */
  boolean checkConfiguration();

  /**
   * If the configuraiton is valid, call initialize the applications configuration, but without starting the
   * application.
   * 
   * @return
   */
  boolean initWithConfig();

  /**
   * realod config intialize application.
   * 
   * 
   */
  void reInit();

  /**
   * public method to start an application.
   * 
   * Calls inside startImpl.
   * 
   * @param args
   * @return
   */
  MgcApplicationStartStopStatus start(String[] args);

  /**
   * Implemented by individual application.
   *
   * @param args the args
   * @return the mgc application start stop status
   */
  MgcApplicationStartStopStatus startImpl(String[] args) throws Exception;

  /**
   * Public method to stop an application.
   * 
   * @return
   */
  MgcApplicationStartStopStatus stop();

  /**
   * Stop the application.
   *
   * @return the mgc application start stop status
   */
  MgcApplicationStartStopStatus stopImpl() throws Exception;

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
