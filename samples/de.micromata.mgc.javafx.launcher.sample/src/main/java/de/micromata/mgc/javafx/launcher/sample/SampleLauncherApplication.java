package de.micromata.mgc.javafx.launcher.sample;

import de.micromata.genome.util.i18n.ChainedResourceBundleTranslationResolver;
import de.micromata.genome.util.i18n.DefaultWarnI18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslationProviderImpl;
import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.i18n.PlaceholderTranslationProvider;
import de.micromata.mgc.jettystarter.JettyConfigModel;
import de.micromata.mgc.jettystarter.JettyServer;
import de.micromata.mgc.jettystarter.MgcApplicationWithJettyApplication;
import de.micromata.mgc.jettystarter.sample.SampleJettyServer;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleLauncherApplication extends MgcApplicationWithJettyApplication<SampleLocalSettingsConfigModel>
{
  public SampleLauncherApplication()
  {
    super();
    I18NTranslationProvider provider = new DefaultWarnI18NTranslationProvider(new PlaceholderTranslationProvider(
        new I18NTranslationProviderImpl(I18NTranslations.systemDefaultLocaleProvider(),
            new ChainedResourceBundleTranslationResolver("mgclauncher", "mgcapp", "mgcjetty"))));
    setTranslateService(provider);
  }

  @Override
  protected SampleLocalSettingsConfigModel newModel()
  {
    return new SampleLocalSettingsConfigModel();
  }

  @Override
  protected JettyServer newJettyServer(JettyConfigModel cfg)
  {
    SampleJettyServer ret = new SampleJettyServer();
    ret.initJetty(cfg);
    return ret;
  }

}
