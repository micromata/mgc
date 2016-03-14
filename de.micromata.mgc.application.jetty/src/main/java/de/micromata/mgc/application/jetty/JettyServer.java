package de.micromata.mgc.application.jetty;

import java.lang.management.ManagementFactory;
import java.net.URL;

import javax.management.MBeanServer;

import org.apache.commons.lang.ArrayUtils;
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

  public void initJetty(JettyConfigModel config)
  {
    LocalSettingsEnv localEnv = LocalSettingsEnv.get();

    //    LocalSettings localSettings = localEnv.getLocalSettings();
    server = new Server();
    HttpConfiguration http_config = new HttpConfiguration();
    ServerConnector connector = initHttpConnector(config, server, http_config);
    ServerConnector sslConnector = initSslConnector(config, server, http_config);

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

  protected void initJmx(JettyConfigModel config, ServletContextHandler contextHandler)
  {
    if (config.isServerEnableJmx() == false) {
      return;
    }
    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
    contextHandler.addEventListener(mBeanContainer);
  }

  protected void initRequestLogger(JettyConfigModel config, ServletContextHandler contextHandler)
  {
    if (config.isServerRequestLoggingEnabled() == false) {
      return;
    }
    HandlerCollection handlers = new HandlerCollection();
    RequestLogHandler requestLogHandler = new RequestLogHandler();
    handlers.setHandlers(new Handler[] { contextHandler, new DefaultHandler(), requestLogHandler });
    server.setHandler(handlers);
    // TODO RK check if directory exists and if not, create it.
    NCSARequestLog requestLog = new NCSARequestLog("log/genome-yyyy_mm_dd.request.log");
    requestLog.setExtended(true);
    requestLog.setRetainDays(90);
    requestLog.setAppend(true);
    requestLog.setExtended(true);
    requestLog.setLogTimeZone("GMT");
    requestLogHandler.setRequestLog(requestLog);
  }

  protected void registerJmxBeans(JettyConfigModel config, MBeanContainer mBeanContainer)
  {

  }

  protected void initSessionTimeout(JettyConfigModel config)
  {
    // TODO RRK implement

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
    https.setIdleTimeout(config.getSessionTimeoutAsInt());

    return https;
  }

  protected Resource getWebResource(String pathInCp)
  {
    URL url = getClass().getClassLoader().getResource(pathInCp);
    // TODO RK debug only
    LOG.warn("Bound web public directory to " + url);

    System.out.println("Bound web public directory to " + url);
    return URLResource.newResource(url);
  }
}
