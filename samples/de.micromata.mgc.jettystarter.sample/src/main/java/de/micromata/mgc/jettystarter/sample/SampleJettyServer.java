package de.micromata.mgc.jettystarter.sample;

import org.eclipse.jetty.servlet.ServletContextHandler;

import de.micromata.mgc.jettystarter.JettyConfigModel;
import de.micromata.mgc.jettystarter.JettyServer;

/**
 * Minimal Jetty Server sample.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleJettyServer extends JettyServer
{

  public SampleJettyServer()
  {
    super();
  }

  @Override
  protected ServletContextHandler createContextHandler(JettyConfigModel config)
  {
    ServletContextHandler sch = new ServletContextHandler();
    // just one hello world servlet.
    sch.addServlet(SampleServlet.class.getName(), "/");
    return sch;
  }

}
