package de.micromata.mgc.jettystarter;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;

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
