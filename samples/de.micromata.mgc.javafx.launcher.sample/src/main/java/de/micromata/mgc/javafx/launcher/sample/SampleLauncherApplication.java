package de.micromata.mgc.javafx.launcher.sample;

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
  public void reInit()
  {

  }

  @Override
  protected SampleLocalSettingsConfigModel newModel()
  {
    return new SampleLocalSettingsConfigModel();
  }

  @Override
  protected JettyServer newJettyServer(JettyConfigModel cfg)
  {
    SampleJettyServer ret = new SampleJettyServer();
    ret.initJetty(cfg);
    return ret;
  }

}
