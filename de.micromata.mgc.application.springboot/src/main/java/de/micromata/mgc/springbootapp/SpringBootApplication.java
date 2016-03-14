package de.micromata.mgc.springbootapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import de.micromata.genome.util.jdbc.LauncherDataSource;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.LocalSettingsEnv;
import de.micromata.genome.util.runtime.config.LocalSettingsConfigModel;
import de.micromata.mgc.application.AbstractMgcApplication;
import de.micromata.mgc.application.MgcApplicationStartStopStatus;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <M>
 */
public abstract class SpringBootApplication<M extends LocalSettingsConfigModel>extends AbstractMgcApplication<M>
{
  static SpringBootApplication<?> springBootApplication;
  SpringApplication springApp;
  protected ConfigurableApplicationContext applicationContext;

  protected abstract Class<?> getSpringBootApplicationClass();

  public SpringBootApplication()
  {

  }

  @Override
  public MgcApplicationStartStopStatus startImpl(String[] args)
  {
    springBootApplication = this;
    LocalSettingsEnv.dataSourceSuplier = () -> new LauncherDataSource();

    LocalSettingsEnv.get();
    LocalSettings.get().logloadedFiles();
    List<ApplicationContextInitializer<?>> inits = createInitializer();
    springApp = new SpringApplication(getSpringBootApplicationClass());

    for (ApplicationContextInitializer<?> init : inits) {
      springApp.addInitializers(init);
    }
    springApp.setBannerMode(Mode.OFF);
    applicationContext = springApp.run(args);

    return MgcApplicationStartStopStatus.StartSuccess;
  }

  protected List<ApplicationContextInitializer<?>> createInitializer()
  {
    List<ApplicationContextInitializer<?>> ret = new ArrayList<>();

    return ret;
  }

  @Override
  public MgcApplicationStartStopStatus stopImpl()
  {
    if (isRunning() == false) {
      return MgcApplicationStartStopStatus.StopAlreadyStopped;
    }
    ExitCodeGenerator ecg = () -> 0;
    SpringApplication.exit(applicationContext, ecg);
    applicationContext.close();
    // see https://dzone.com/articles/managing-spring-boot
    // TODO Auto-generated method stub
    springApp = null;
    applicationContext = null;
    return MgcApplicationStartStopStatus.StopSuccess;

  }

  @Override
  public boolean isRunning()
  {
    return applicationContext != null;
  }

}
