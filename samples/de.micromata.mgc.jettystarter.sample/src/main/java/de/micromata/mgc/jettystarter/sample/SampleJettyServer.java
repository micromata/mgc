package de.micromata.mgc.jettystarter.sample;

import org.eclipse.jetty.servlet.ServletContextHandler;

import de.micromata.mgc.jettystarter.JettyConfigModel;
import de.micromata.mgc.jettystarter.JettyServer;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleJettyServer extends JettyServer
{

  public SampleJettyServer(JettyConfigModel config)
  {
    super(config);
  }

  @Override
  protected ServletContextHandler createContextHandler(JettyConfigModel config)
  {
    ServletContextHandler sch = new ServletContextHandler();
    sch.addServlet(SampleServlet.class.getName(), "/");
    return sch;
  }

}
