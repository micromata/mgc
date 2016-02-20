package de.micromata.mgc.javafx.launcher.sample;

import de.micromata.genome.util.validation.ValTranslateService;
import de.micromata.genome.util.validation.ValTranslateServices;
import de.micromata.mgc.jettystarter.JettyConfigModel;
import de.micromata.mgc.jettystarter.JettyServer;
import de.micromata.mgc.jettystarter.MgcApplicationWithJettyApplication;
import de.micromata.mgc.jettystarter.sample.SampleJettyServer;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SampleLauncherApplication extends MgcApplicationWithJettyApplication<SampleLocalSettingsConfigModel>
{
  @Override
  public ValTranslateService getTranslateService()
  {
    return ValTranslateServices.noTranslation();
  }

  @Override
  public void reInit()
  {
    // TODO Auto-generated method stub

  }

  @Override
  protected SampleLocalSettingsConfigModel newModel()
  {
    return new SampleLocalSettingsConfigModel();
  }

  @Override
  protected JettyServer newJettyServer(JettyConfigModel cfg)
  {
    return new SampleJettyServer(cfg);
  }

}
