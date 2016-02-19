package de.micromata.mgc.jettystarter.sample;

import org.eclipse.jetty.servlet.ServletContextHandler;

import de.micromata.mgc.jettystarter.JettyConfigModel;
import de.micromata.mgc.jettystarter.JettyServer;

public class SampleJettyServer extends JettyServer
{

  @Override
  protected ServletContextHandler createContextHandler(JettyConfigModel config)
  {
    ServletContextHandler sch = new ServletContextHandler();
    sch.addServlet(SampleServlet.class.getName(), "/");
    return sch;
  }

}
