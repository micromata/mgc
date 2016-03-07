package de.micromata.mgc.jettystarter;

import org.apache.log4j.Logger;

import de.micromata.genome.util.event.MgcEventRegistries;
import de.micromata.genome.util.i18n.ChainedResourceBundleTranslationResolver;
import de.micromata.genome.util.i18n.DefaultWarnI18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslationProviderImpl;
import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.i18n.PlaceholderTranslationProvider;
import de.micromata.genome.util.runtime.LocalSettingsEnv;
import de.micromata.genome.util.runtime.config.AbstractCompositLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.genome.util.validation.ValState;
import de.micromata.mgc.launcher.AbstractMgcApplication;
import de.micromata.mgc.launcher.MgcApplicationStartStopEvent;
import de.micromata.mgc.launcher.MgcApplicationStartStopStatus;

/**
 * Application, which will be started by an embedded jetty server.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <M> configuration model of the application.
 */
public abstract class MgcApplicationWithJettyApplication<M extends LocalSettingsConfigModel>
    extends AbstractMgcApplication<M>

{

  /**
   * The Constant LOG.
   */
  private static final Logger LOG = Logger.getLogger(MgcApplicationWithJettyApplication.class);

  /**
   * The jetty server.
   */
  protected JettyServer jettyServer;

  /**
   * New jetty server.
   *
   * @param cfg the cfg
   * @return the jetty server
   */
  protected abstract JettyServer newJettyServer(JettyConfigModel cfg);

  /**
   * Instantiates a new mgc application with jetty application.
   */
  public MgcApplicationWithJettyApplication()
  {
    I18NTranslationProvider provider = new DefaultWarnI18NTranslationProvider(new PlaceholderTranslationProvider(
        new I18NTranslationProviderImpl(I18NTranslations.systemDefaultLocaleProvider(),
            new ChainedResourceBundleTranslationResolver("mgcapp", "mgcjetty"))));
    setTranslateService(provider);
  }

  @Override
  public String getPublicUrl()
  {
    JettyConfigModel jettyConfig = AbstractCompositLocalSettingsConfigModel.castTo(getConfigModel(),
        JettyConfigModel.class);
    if (jettyConfig == null) {
      return super.getPublicUrl();
    }
    return jettyConfig.getPublicUrl();
  }

  /**
   * Gets the creates the jetty server.
   *
   * @return the creates the jetty server
   */
  protected void getCreateJettyServer()
  {
    if (jettyServer != null) {
      return;
    }
    JettyConfigModel cfg = getJettyConfig();
    jettyServer = newJettyServer(cfg);
  }

  protected JettyConfigModel getJettyConfig()
  {
    JettyConfigModel ret = null;
    M cfg = getConfigModel();
    if (cfg instanceof JettyConfigModel) {
      return (JettyConfigModel) cfg;
    } else if (cfg instanceof CastableLocalSettingsConfigModel) {
      ret = ((CastableLocalSettingsConfigModel) cfg).castTo(JettyConfigModel.class);
    }
    if (ret != null) {
      return ret;
    }
    throw new IllegalArgumentException(
        "The configuration contains no Jetty configuration: " + cfg.getClass().getName());
  }

  @Override
  public MgcApplicationStartStopStatus start(String[] args)
  {

    try {
      getCreateJettyServer();
      LocalSettingsEnv.get();
      jettyServer.getServer().start();

      MgcEventRegistries.getEventInstanceRegistry()
          .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StartSuccess,
              new ValMessage(ValState.Info, "mgc.jetty.msg.jettystarted")));
      return MgcApplicationStartStopStatus.StartSuccess;
    } catch (Exception ex) {
      LOG.error("MgcApp start failed: " + ex.getMessage(), ex);
      MgcEventRegistries.getEventInstanceRegistry()
          .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StartError,
              new ValMessage(ValState.Error, "mgc.jetty.msg.jettystartedfailed", ex,
                  new Object[] { ex.getMessage() })));
      return MgcApplicationStartStopStatus.StartError;
    }
  }

  @Override
  public MgcApplicationStartStopStatus stop()
  {
    if (jettyServer == null) {
      MgcEventRegistries.getEventInstanceRegistry()
          .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StopAlreadyStopped,
              new ValMessage(ValState.Warning, "mgc.jetty.msg.jettyalreadystopped")));
      return MgcApplicationStartStopStatus.StopAlreadyStopped;
    }
    try {
      jettyServer.getServer().stop();
      MgcEventRegistries.getEventInstanceRegistry()
          .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StopSuccess,
              new ValMessage(ValState.Info, "mgc.jetty.msg.jettystopped")));
      return MgcApplicationStartStopStatus.StopSuccess;
    } catch (Exception ex) {
      MgcEventRegistries.getEventInstanceRegistry()
          .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StopError,
              new ValMessage(ValState.Error, "mgc.jetty.msg.jettystopFailed", ex, new Object[] { ex.getMessage() })));
      return MgcApplicationStartStopStatus.StopError;
    }
  }

  @Override
  public boolean isRunning()
  {
    if (jettyServer == null || jettyServer.getServer() == null) {
      return false;
    }

    return jettyServer.getServer().isRunning();
  }

  /**
   * Stop and wait.
   *
   * @return the mgc application start stop status
   */
  public MgcApplicationStartStopStatus stopAndWait()
  {
    MgcApplicationStartStopStatus res = stop();
    try {
      jettyServer.getServer().join();
      return res;
    } catch (Exception ex) {
      MgcEventRegistries.getEventInstanceRegistry()
          .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StopError,
              new ValMessage(ValState.Error, "Jetty join failed", ex)));
      return MgcApplicationStartStopStatus.StopError;
    }
  }

}
