package de.micromata.mgc.application;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.ValMessageLogAttribute;
import de.micromata.genome.util.event.MgcEventRegistries;
import de.micromata.genome.util.i18n.I18NTranslationProvider;
import de.micromata.genome.util.i18n.I18NTranslations;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.Log4JInitializer;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.genome.util.validation.ValState;

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

  /**
   * 
   */
  @Override
  public void initializeAfterConstruction()
  {
    configureLogging();
  }

  protected static void configureLogging()
  {
    Log4JInitializer.copyLogConfigFileFromCp();
    Log4JInitializer.initializeLog4J();

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
  public MgcApplicationStartStopStatus start(String[] args)
  {

    ValContext ctx = new ValContext();
    model.validate(ctx);
    if (ctx.hasErrors() == true) {
      GLog.error(GenomeLogCategory.Configuration, "Cannot start server, because configuration is not valid",
          new ValMessageLogAttribute(ctx.getMessages()));
      return MgcApplicationStartStopStatus.StartNoConfiguration;
    }
    GLog.note(GenomeLogCategory.System, "Start server: " + getClass().getSimpleName());
    try {
      MgcApplicationStartStopStatus ret = startImpl(args);

      switch (ret) {
        case StartAlreadyRunning:
          GLog.warn(GenomeLogCategory.System, "Server already running");
          break;
        case StartError:
          GLog.error(GenomeLogCategory.System, "Server failed starting");
          break;
        default:
          MgcEventRegistries.getEventInstanceRegistry()
              .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StartSuccess,
                  new ValMessage(ValState.Info, "mgc.application.start.success")));
          break;
      }
      return ret;
    } catch (Exception ex) {
      MgcEventRegistries.getEventInstanceRegistry()
          .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StartSuccess,
              new ValMessage(ValState.Info, "mgc.application.start.success.failed", ex)));

      GLog.error(GenomeLogCategory.System, "Server start failed: " + ex.getMessage(), new LogExceptionAttribute(ex));
    }
    return MgcApplicationStartStopStatus.StartError;
  }

  @Override
  public MgcApplicationStartStopStatus stop()
  {
    try {
      MgcApplicationStartStopStatus ret = stopImpl();
      switch (ret) {
        case StopSuccess:
          MgcEventRegistries.getEventInstanceRegistry()
              .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StopSuccess,
                  new ValMessage(ValState.Info, "mgc.application.stopped.success")));
          break;
        case StopAlreadyStopped:
          GLog.warn(GenomeLogCategory.System, "Server already stopped");
          break;
        case StartError:
          GLog.warn(GenomeLogCategory.System, "Server Stop failed");
          break;
      }
      return ret;
    } catch (Exception ex) {
      GLog.error(GenomeLogCategory.System, "Server stop throws exception: " + ex.getMessage(),
          new LogExceptionAttribute(ex));
      MgcEventRegistries.getEventInstanceRegistry()
          .dispatchEvent(new MgcApplicationStartStopEvent(this, MgcApplicationStartStopStatus.StopError,
              new ValMessage(ValState.Info, "mgc.application.stopped.failed", ex)));
      return MgcApplicationStartStopStatus.StopError;
    }
  }

  @Override
  public void reInit()
  {
    Log4JInitializer.reinit();

  }

  @Override
  public UncaughtExceptionHandler getUncaughtExceptionHandler()
  {
    return (thread, exception) -> {
      GLog.error(GenomeLogCategory.Internal, "Uncaught Exception: " + exception.getMessage() + " in thread " + thread,
          new LogExceptionAttribute(exception));
    };
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
