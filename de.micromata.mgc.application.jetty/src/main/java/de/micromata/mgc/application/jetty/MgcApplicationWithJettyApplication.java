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

package de.micromata.mgc.application.jetty;

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
import de.micromata.mgc.application.AbstractMgcApplication;
import de.micromata.mgc.application.MgcApplicationStartStopEvent;
import de.micromata.mgc.application.MgcApplicationStartStopStatus;
import de.micromata.mgc.application.webserver.config.JettyConfigModel;

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
  public MgcApplicationStartStopStatus startImpl(String[] args) throws Exception
  {

    getCreateJettyServer();
    LocalSettingsEnv.get();
    jettyServer.getServer().start();
    return MgcApplicationStartStopStatus.StartSuccess;
  }

  @Override
  public MgcApplicationStartStopStatus stopImpl() throws Exception
  {
    if (jettyServer == null) {
      return MgcApplicationStartStopStatus.StopAlreadyStopped;
    }
    jettyServer.getServer().stop();
    jettyServer = null;
    return MgcApplicationStartStopStatus.StopSuccess;
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
  public MgcApplicationStartStopStatus stopAndWait() throws Exception
  {
    MgcApplicationStartStopStatus res = stopImpl();
    try {
      jettyServer.getServer().join();
      jettyServer = null;
      return res;
    } catch (Exception ex) {
      MgcEventRegistries.getEventInstanceRegistry()
          .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StopError,
              new ValMessage(ValState.Error, "Jetty join failed", ex)));
      return MgcApplicationStartStopStatus.StopError;
    }
  }

}
