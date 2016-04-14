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

package de.micromata.mgc.javafx.launcher.sample.jetty;

import org.eclipse.jetty.servlet.ServletContextHandler;

import de.micromata.mgc.application.jetty.JettyServer;
import de.micromata.mgc.application.webserver.config.JettyConfigModel;

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
