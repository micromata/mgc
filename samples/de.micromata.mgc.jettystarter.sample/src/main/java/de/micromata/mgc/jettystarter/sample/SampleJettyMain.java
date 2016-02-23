package de.micromata.mgc.jettystarter.sample;

import javax.servlet.http.HttpServletResponse;

import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.mgc.jettystarter.JettyConfigModel;
import de.micromata.mgc.jettystarter.JettyServer;
import de.micromata.mgc.jettystarter.JettyServerRunner;
import de.micromata.mgc.jettystarter.MgcApplicationWithJettyApplication;

/**
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
      public void reInit()
      {
        // TODO Auto-generated method stub

      }

      @Override
      protected JettyServer newJettyServer(JettyConfigModel cfg)
      {
        return new SampleJettyServer(cfg);
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
