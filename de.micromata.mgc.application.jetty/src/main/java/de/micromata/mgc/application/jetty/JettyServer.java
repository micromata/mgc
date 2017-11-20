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

import java.lang.management.ManagementFactory;
import java.net.URL;

import javax.management.MBeanServer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.URLResource;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import de.micromata.genome.util.runtime.LocalSettingsEnv;
import de.micromata.mgc.application.webserver.config.JettyConfigModel;

/**
 * Wrapper for Jetty Server.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class JettyServer
{
  private static final Logger LOG = Logger.getLogger(JettyServer.class);
  protected ServletContextHandler contextHandler;

  protected Server server;

  protected JettyConfigModel configModel;

  protected abstract ServletContextHandler createContextHandler(JettyConfigModel config);

  public JettyServer()
  {

  }

  public Server getServer()
  {
    return server;
  }

  public void initJetty(final JettyConfigModel config)
  {
    final LocalSettingsEnv localEnv = LocalSettingsEnv.get();

    //    LocalSettings localSettings = localEnv.getLocalSettings();
    server = new Server();
    final HttpConfiguration http_config = new HttpConfiguration();
    final ServerConnector connector = initHttpConnector(config, server, http_config);
    final ServerConnector sslConnector = initSslConnector(config, server, http_config);

    Connector[] connectors = new Connector[] { connector };
    if (sslConnector != null) {
      if (config.isSslOnly() == true) {
        connectors = new Connector[] { sslConnector };
      } else {
        connectors = (Connector[]) ArrayUtils.add(connectors, sslConnector);
      }

    }
    server.setConnectors(connectors);
    contextHandler = createContextHandler(config);
    contextHandler.setServer(server);
    contextHandler.setSessionHandler(createSessionHandler());
    server.setHandler(contextHandler);
    initRequestLogger(config, contextHandler);
    initSessionTimeout(config);
    initJmx(config, contextHandler);

  }

  protected void initJmx(final JettyConfigModel config, final ServletContextHandler contextHandler)
  {
    if (config.isServerEnableJmx() == false) {
      return;
    }
    final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    final MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
    contextHandler.addEventListener(mBeanContainer);
  }

  protected void initRequestLogger(final JettyConfigModel config, final ServletContextHandler contextHandler)
  {
    if (config.isServerRequestLoggingEnabled() == false) {
      return;
    }
    final HandlerCollection handlers = new HandlerCollection();
    final RequestLogHandler requestLogHandler = new RequestLogHandler();
    handlers.setHandlers(new Handler[] { contextHandler, new DefaultHandler(), requestLogHandler });
    server.setHandler(handlers);
    // TODO RK check if directory exists and if not, create it.
    final NCSARequestLog requestLog = new NCSARequestLog("log/genome-yyyy_mm_dd.request.log");
    requestLog.setExtended(true);
    requestLog.setRetainDays(90);
    requestLog.setAppend(true);
    requestLog.setExtended(true);
    requestLog.setLogTimeZone("GMT");
    requestLogHandler.setRequestLog(requestLog);
  }

  protected void registerJmxBeans(final JettyConfigModel config, final MBeanContainer mBeanContainer)
  {

  }

  protected void initSessionTimeout(final JettyConfigModel config)
  {
    final int sessionTimeout = config.getSessionTimeoutAsInt();
    contextHandler.getSessionHandler().getSessionManager().setMaxInactiveInterval(sessionTimeout);
  }

  protected SessionHandler createSessionHandler()
  {
    return new SessionHandler();
  }

  private ServerConnector initHttpConnector(final JettyConfigModel config, final Server server, final HttpConfiguration http_config)
  {
    final int port = config.getPortAsInt();
    final ServerConnector http = new ServerConnector(server,
        new HttpConnectionFactory(http_config));
    http.setPort(port);
    if (StringUtils.isNotBlank(config.getListenHost()) == true) {
      http.setHost(config.getListenHost());
    }
    
    http.setIdleTimeout(config.getIdleTimeoutAsLong());
    
    System.err.println("Initialize Jetty with JettyConfigModel: "+config);
    
    return http;
  }

  private ServerConnector initSslConnector(final JettyConfigModel config, final Server server, final HttpConfiguration http_config)
  {

    if (config.isSslEnabled() == false) {
      return null;
    }

    final SslContextFactory sslContextFactory = new SslContextFactory();
    final String keystorePath = config.getSslKeystorePath();
    sslContextFactory.setKeyStorePath(keystorePath);
    // no null check required, because if password is null, setPassword will prompt user to provide a password
    sslContextFactory.setKeyStorePassword(config.getSslKeystorePassword());
    sslContextFactory.setKeyManagerPassword(config.getSslKeyManagerPassword());

    sslContextFactory.setTrustStorePath(config.getTrustStorePath());
    sslContextFactory.setTrustStorePassword(config.getTrustStorePassword());

    sslContextFactory.setCertAlias(config.getSslCertAlias());
    sslContextFactory.setTrustAll(true);
    final HttpConfiguration https_config = new HttpConfiguration(http_config);
    https_config.addCustomizer(new SecureRequestCustomizer());
    final ServerConnector https = new ServerConnector(server,
        new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
        new HttpConnectionFactory(https_config));

    final int port = config.getSslPortAsInt();
    https.setPort(port);
    https.setIdleTimeout(config.getSessionTimeoutAsInt());
    if (StringUtils.isNotBlank(config.getListenHost()) == true) {
      https.setHost(config.getListenHost());
    }
    return https;
  }

  protected Resource getWebResource(final String pathInCp)
  {
    final URL url = getClass().getClassLoader().getResource(pathInCp);
    // TODO RK debug only
    LOG.warn("Bound web public directory to " + url);

    System.out.println("Bound web public directory to " + url);
    return URLResource.newResource(url);
  }
}
