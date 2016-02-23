package de.micromata.mgc.jettystarter;

import de.micromata.genome.util.i18n.ChainedResourceBundleTranslationResolver;
import de.micromata.genome.util.i18n.DefaultWarnI18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslationProviderImpl;
import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.i18n.PlaceholderTranslationProvider;
import de.micromata.genome.util.runtime.LocalSettingsEnv;
import de.micromata.genome.util.runtime.config.CastableLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.genome.util.validation.ValState;
import de.micromata.mgc.launcher.AbstractMgcApplication;
import de.micromata.mgc.launcher.MgcApplicationStartStopListener;
import de.micromata.mgc.launcher.MgcApplicationStartStopStatus;

public abstract class MgcApplicationWithJettyApplication<M extends LocalSettingsConfigModel>
    extends AbstractMgcApplication<M>

{

  protected JettyServer jettyServer;

  protected abstract JettyServer newJettyServer(JettyConfigModel cfg);

  public MgcApplicationWithJettyApplication()
  {
    I18NTranslationProvider provider = new DefaultWarnI18NTranslationProvider(new PlaceholderTranslationProvider(
        new I18NTranslationProviderImpl(I18NTranslations.systemDefaultLocaleProvider(),
            new ChainedResourceBundleTranslationResolver("mgcapp", "mgcjetty"))));
    setTranslateService(provider);
  }

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
  public MgcApplicationStartStopStatus start(String[] args, MgcApplicationStartStopListener listener)
  {

    try {
      getCreateJettyServer();
      LocalSettingsEnv.get();
      jettyServer.getServer().start();

      listener.listen(this, MgcApplicationStartStopStatus.StartSuccess,
          new ValMessage(ValState.Info, "mgc.jetty.msg.jettystarted"));
      return MgcApplicationStartStopStatus.StartSuccess;
    } catch (Exception ex) {
      listener.listen(this, MgcApplicationStartStopStatus.StartError,
          new ValMessage(ValState.Error, "mgc.jetty.msg.jettystartedfailed", ex, new Object[] { ex.getMessage() }));
      return MgcApplicationStartStopStatus.StartError;
    }
  }

  @Override
  public MgcApplicationStartStopStatus stop(MgcApplicationStartStopListener listener)
  {
    if (jettyServer == null) {
      listener.listen(this, MgcApplicationStartStopStatus.StopAlreadyStopped,
          new ValMessage(ValState.Warning, "mgc.jetty.msg.jettyalreadystopped"));
      return MgcApplicationStartStopStatus.StopAlreadyStopped;
    }
    try {
      jettyServer.getServer().stop();
      listener.listen(this, MgcApplicationStartStopStatus.StopSuccess,
          new ValMessage(ValState.Info, "mgc.jetty.msg.jettystopped"));
      return MgcApplicationStartStopStatus.StopSuccess;
    } catch (Exception ex) {
      listener.listen(this, MgcApplicationStartStopStatus.StopError,
          new ValMessage(ValState.Error, "mgc.jetty.msg.jettystopFailed", ex, new Object[] { ex.getMessage() }));
      return MgcApplicationStartStopStatus.StopError;
    }
  }

  @Override
  public boolean isRunning()
  {
    return jettyServer.getServer().isRunning();
  }

  public MgcApplicationStartStopStatus stopAndWait(MgcApplicationStartStopListener listener)
  {
    MgcApplicationStartStopStatus res = stop(listener);
    try {
      jettyServer.getServer().join();
      return res;
    } catch (Exception ex) {
      listener.listen(this, MgcApplicationStartStopStatus.StopError,
          new ValMessage(ValState.Error, "Jetty join failed", ex));
      return MgcApplicationStartStopStatus.StopError;
    }
  }

}
