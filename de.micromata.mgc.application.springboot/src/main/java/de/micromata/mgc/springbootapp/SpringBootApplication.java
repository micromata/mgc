//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
