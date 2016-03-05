package de.micromata.mgc.launcher;

import java.io.File;

import de.micromata.genome.util.i18n.I18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.Log4JInitializer;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <M>
 */
public abstract class AbstractMgcApplication<M extends LocalSettingsConfigModel> implements MgcApplication<M>
{
  private M model;

  protected I18NTranslationProvider translateService;

  public AbstractMgcApplication()
  {
    translateService = I18NTranslations.noTranslationProvider();
  }

  @Override
  public MgcApplicationInfo getApplicationInfo()
  {
    return new ManifestMgcApplicationInfo(this);
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
    model = nmodel;
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
    File file = new File(LocalSettings.get().getLocalSettingsLoader().getLocalSettingsFileName());
    writer.store(file);
  }

  @Override
  public String getPublicUrl()
  {
    return LocalSettings.get().get("cfg.public.url");

  }

  @Override
  public void reInit()
  {
    Log4JInitializer.reinit();

  }

  @Override
  public I18NTranslationProvider getTranslateService()
  {
    return translateService;
  }

  public void setTranslateService(I18NTranslationProvider translateService)
  {
    this.translateService = translateService;
  }

}
