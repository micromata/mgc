package de.micromata.mgc.launcher;

import java.io.File;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValTranslateService;
import de.micromata.genome.util.validation.ValTranslateServices;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <M>
 */
public abstract class AbstractMgcApplication<M extends LocalSettingsConfigModel> implements MgcApplication<M>
{
  private M model;

  protected ValTranslateService ranslateService;

  public AbstractMgcApplication()
  {
    ranslateService = ValTranslateServices.noTranslation();
  }

  protected abstract M newModel();

  @Override
  public M getConfigModel()
  {
    if (model == null) {
      model = loadConfigModel();
    }
    return model;
  }

  @Override
  public M loadConfigModel()
  {
    M nmodel = newModel();
    loadFromLocalSettings(nmodel, LocalSettings.get());
    return nmodel;
  }

  @Override
  public void storeConfig(ValContext ctx, M config)
  {
    storeToLocalSettings(config);

  }

  protected void loadFromLocalSettings(M model, LocalSettings localSettings)
  {
    model.fromLocalSettings(localSettings);
  }

  protected void storeToLocalSettings(M model)
  {
    LocalSettingsWriter writer = new LocalSettingsWriter();
    model.toProperties(writer);
    File file = new File(LocalSettings.get().getLocalSettingsFile());
    writer.store(file);
  }
}
