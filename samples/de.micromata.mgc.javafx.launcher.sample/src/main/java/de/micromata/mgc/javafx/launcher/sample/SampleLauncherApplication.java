//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.mgc.javafx.launcher.sample;

import de.micromata.genome.util.i18n.ChainedResourceBundleTranslationResolver;
import de.micromata.genome.util.i18n.DefaultWarnI18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslationProviderImpl;
import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.i18n.PlaceholderTranslationProvider;
import de.micromata.genome.util.runtime.InitWithCopyFromCpLocalSettingsClassLoader;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.ExtLocalSettingsLoader;
import de.micromata.mgc.application.jetty.JettyServer;
import de.micromata.mgc.application.jetty.MgcApplicationWithJettyApplication;
import de.micromata.mgc.application.webserver.config.JettyConfigModel;
import de.micromata.mgc.javafx.launcher.sample.jetty.SampleJettyServer;

/**
 * Application with a embedded jetty
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleLauncherApplication extends MgcApplicationWithJettyApplication<SampleLocalSettingsConfigModel>
{
  public SampleLauncherApplication()
  {
    super();
    LocalSettings.localSettingsLoaderFactory = new InitWithCopyFromCpLocalSettingsClassLoader(
        () -> {
          ExtLocalSettingsLoader ret = new ExtLocalSettingsLoader();
          ret.setLocalSettingsPrefixName("launchersample");
          return ret;
        });

    // configure the translation 
    I18NTranslationProvider provider = new DefaultWarnI18NTranslationProvider(new PlaceholderTranslationProvider(
        new I18NTranslationProviderImpl(I18NTranslations.systemDefaultLocaleProvider(),
            new ChainedResourceBundleTranslationResolver("mgclauncher", "mgcapp", "mgcjetty"))));
    setTranslateService(provider);
  }

  /**
   * Provides the configuration model.
   * 
   * {@inheritDoc}
   *
   */
  @Override
  protected SampleLocalSettingsConfigModel newModel()
  {
    return new SampleLocalSettingsConfigModel();
  }

  /**
   * Create a jetty with deployed war or exlicite servlets.
   * 
   * {@inheritDoc}
   *
   */
  @Override
  protected JettyServer newJettyServer(JettyConfigModel cfg)
  {
    SampleJettyServer ret = new SampleJettyServer();
    ret.initJetty(cfg);
    return ret;
  }
}
