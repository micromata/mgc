package de.micromata.mgc.javafx.launcher.sample.jetty;

import javax.servlet.http.HttpServletResponse;

import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.application.jetty.JettyServer;
import de.micromata.mgc.application.jetty.JettyServerRunner;
import de.micromata.mgc.application.jetty.MgcApplicationWithJettyApplication;
import de.micromata.mgc.application.webserver.config.JettyConfigModel;

/**
 * Runn a jetty on a console.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleJettyMain
{
  public static void main(String[] args)
  {
    HttpServletResponse.class.getName();
    JettyConfigModel jc = new JettyConfigModel();
    jc.fromLocalSettings(LocalSettings.get());
    ValContext ctx = new ValContext();
    jc.validate(ctx);
    if (ctx.hasErrors() == true) {
      for (ValMessage msg : ctx.getMessages()) {
        System.out.println(msg.getTranslatedMessage(I18NTranslations.noTranslationProvider()));
      }
      System.exit(10);
    }
    MgcApplicationWithJettyApplication server = new MgcApplicationWithJettyApplication()
    {

      @Override
      protected JettyServer newJettyServer(JettyConfigModel cfg)
      {
        SampleJettyServer ret = new SampleJettyServer();
        ret.initJetty(cfg);
        return ret;
      }

      @Override
      protected LocalSettingsConfigModel newModel()
      {
        return new JettyConfigModel();
      }

    };

    new JettyServerRunner().runServer(server);
  }
}
