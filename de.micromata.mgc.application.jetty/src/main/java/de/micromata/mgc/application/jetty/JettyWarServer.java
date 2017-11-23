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

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import de.micromata.mgc.application.webserver.config.JettyConfigModel;

/**
 * Deploys a war archive.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JettyWarServer extends JettyServer
{
  @Override
  protected ServletContextHandler createContextHandler(JettyConfigModel config)
  {
    return createDirWarContextHandler(config);
  }

  /**
   * Create a ServletContextHandler or WebAppContext.
   * 
   * @return
   */

  protected ServletContextHandler createDirWarContextHandler(JettyConfigModel config)
  {
    WebAppContext war = new WebAppContext();

    war.setContextPath(getContextPath(config));

    //    String warDir = new File(localsettings.getProperty("genome.wardir", "src/main/webapp")).getPath();
    String warDir = getWarDir(config);

    String descriptor = getWebDescritorFile(config, warDir);
    if (StringUtils.isNotBlank(descriptor) == true) {
      war.setDescriptor(descriptor);
    }
    //  TODO RK??  initExtraWarCp(localsettings, war);
    war.setWar(warDir);
    return war;
  }

  protected String getContextPath(JettyConfigModel config)
  {
    return "/";
  }

  protected String getWarDir(JettyConfigModel config)
  {
    return "src/main/webapp";
  }

  protected String getWebDescritorFile(JettyConfigModel config, String warDir)
  {
    return new File(new File(warDir), "WEB-INF/web.xml").getAbsolutePath();
  }

  //  protected void initExtraWarCp(JettyConfigModel config, WebAppContext bb)
  //  {
  //    String extraCp = localsettings.get("genome.extracp");
  //    if (StringUtils.isEmpty(extraCp) == false) {
  //      bb.setExtraClasspath(extraCp);
  //    }
  //  }
}
