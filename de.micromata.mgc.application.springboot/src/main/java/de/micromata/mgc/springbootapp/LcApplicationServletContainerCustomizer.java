package de.micromata.mgc.springbootapp;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.stereotype.Component;

import de.micromata.genome.util.runtime.config.AbstractCompositLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.mgc.application.MgcApplication;
import de.micromata.mgc.application.webserver.config.JettyConfigModel;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Component
public class LcApplicationServletContainerCustomizer implements EmbeddedServletContainerCustomizer
{
  static final org.apache.log4j.Logger log = org.apache.log4j.Logger
      .getLogger(LcApplicationServletContainerCustomizer.class);

  private MgcApplication<?> application;

  public LcApplicationServletContainerCustomizer()
  {
    application = SpringBootApplication.springBootApplication;
  }

  public LcApplicationServletContainerCustomizer(MgcApplication<?> application)
  {
    this.application = application;
  }

  @Override
  public void customize(ConfigurableEmbeddedServletContainer container)
  {
    LocalSettingsConfigModel projectCfg = application.getConfigModel();

    JettyConfigModel scm = ((AbstractCompositLocalSettingsConfigModel) projectCfg)
        .castTo(JettyConfigModel.class);

    container.setPort(scm.getPortAsInt());
    container.setContextPath(scm.getContextPath());
    container.setSessionTimeout(scm.getSessionTimeoutAsInt());

    if (container instanceof JettyEmbeddedServletContainerFactory) {

      ((JettyEmbeddedServletContainerFactory) container)
          .addServerCustomizers(new JettyServletContainerCustomizer(scm));
    }
  }
  //INFOs:
  //http://stackoverflow.com/questions/24122123/spring-boot-jetty-ssl-port
}
