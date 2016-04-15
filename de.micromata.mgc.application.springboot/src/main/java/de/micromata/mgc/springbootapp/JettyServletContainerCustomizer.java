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

package de.micromata.mgc.springbootapp;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.component.Container;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;

import de.micromata.mgc.application.webserver.config.JettyConfigModel;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JettyServletContainerCustomizer implements JettyServerCustomizer
{
  JettyConfigModel config;

  public JettyServletContainerCustomizer(JettyConfigModel config)
  {
    this.config = config;
  }

  @Override
  public void customize(Server server)
  {
    Handler[] handlers = server.getHandlers();

    HttpConfiguration http_config = new HttpConfiguration();
    ServerConnector connector = initHttpConnector(config, server, http_config);
    ServerConnector sslConnector = initSslConnector(config, server, http_config);

    Connector[] connectors = new Connector[] { connector };
    if (sslConnector != null)

    {
      if (config.isSslOnly() == true) {
        connectors = new Connector[] { sslConnector };
      } else {
        connectors = (Connector[]) ArrayUtils.add(connectors, sslConnector);
      }

    }
    server.setConnectors(connectors);
    initRequestLogger(server, config);
    initJmx(server, config);

  }

  protected void initJmx(Server server, JettyConfigModel config)
  {
    if (config.isServerEnableJmx() == false) {
      return;
    }
    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
    for (Handler h : server.getHandlers()) {
      addEventListener(h, mBeanContainer);
    }
  }

  private void addEventListener(Handler handler, Container.Listener listener)
  {
    if (handler instanceof ServletContextHandler) {
      ((ServletContextHandler) handler).addEventListener(listener);
    }
    if (handler instanceof HandlerCollection) {
      HandlerCollection hc = (HandlerCollection) handler;
      for (Handler h : hc.getHandlers()) {
        addEventListener(h, listener);
      }
    }
  }

  protected void initRequestLogger(Server server, JettyConfigModel config)
  {
    if (config.isServerRequestLoggingEnabled() == false) {
      return;
    }
    Handler[] prevhandler = server.getHandlers();
    RequestLogHandler requestLogHandler = new RequestLogHandler();

    NCSARequestLog requestLog = new NCSARequestLog("log/genome-yyyy_mm_dd.request.log");
    requestLog.setExtended(true);
    requestLog.setRetainDays(90);
    requestLog.setAppend(true);
    requestLog.setExtended(true);
    requestLog.setLogTimeZone("GMT");
    requestLogHandler.setRequestLog(requestLog);

    HandlerCollection handlers = new HandlerCollection();

    for (Handler h : prevhandler) {
      handlers.addHandler(h);
    }
    handlers.addBean(requestLogHandler);
    server.setHandler(handlers);

  }

  protected SessionHandler createSessionHandler()
  {
    return new SessionHandler();
  }

  private ServerConnector initHttpConnector(JettyConfigModel config, Server server, HttpConfiguration http_config)
  {
    int port = config.getPortAsInt();
    ServerConnector http = new ServerConnector(server,
        new HttpConnectionFactory(http_config));
    http.setPort(port);
    http.setIdleTimeout(config.getSessionTimeoutAsInt());
    if (StringUtils.isNotBlank(config.getListenHost()) == true) {
      http.setHost(config.getListenHost());
    }
    return http;
  }

  private ServerConnector initSslConnector(JettyConfigModel config, Server server, HttpConfiguration http_config)
  {

    if (config.isSslEnabled() == false) {
      return null;
    }

    SslContextFactory sslContextFactory = new SslContextFactory();
    String keystorePath = config.getSslKeystorePath();
    sslContextFactory.setKeyStorePath(keystorePath);
    // no null check required, because if password is null, setPassword will prompt user to provide a password
    sslContextFactory.setKeyStorePassword(config.getSslKeystorePassword());
    sslContextFactory.setKeyManagerPassword(config.getSslKeyManagerPassword());

    sslContextFactory.setTrustStorePath(config.getTrustStorePath());
    sslContextFactory.setTrustStorePassword(config.getTrustStorePassword());

    sslContextFactory.setCertAlias(config.getSslCertAlias());
    sslContextFactory.setTrustAll(true);
    HttpConfiguration https_config = new HttpConfiguration(http_config);
    https_config.addCustomizer(new SecureRequestCustomizer());
    ServerConnector https = new ServerConnector(server,
        new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
        new HttpConnectionFactory(https_config));

    int port = config.getSslPortAsInt();
    https.setPort(port);
    if (StringUtils.isNotBlank(config.getListenHost()) == true) {
      https.setHost(config.getListenHost());
    }
    https.setIdleTimeout(config.getSessionTimeoutAsInt());

    return https;
  }
  //
  //  protected Resource getWebResource(String pathInCp)
  //  {
  //    URL url = getClass().getClassLoader().getResource(pathInCp);
  //    // TODO RK debug only
  //    LOG.warn("Bound web public directory to " + url);
  //
  //    System.out.println("Bound web public directory to " + url);
  //    return URLResource.newResource(url);
  //  }
}
