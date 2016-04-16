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

package de.micromata.genome.chronos.spi.lsconfig;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.chronos.manager.JobBeanDefinition;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.chronos.util.ClassJobDefinition;
import de.micromata.genome.chronos.util.SchedulerFactory;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;

/**
 * LocalSettings Configuration Model for Chronos Initialization.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ChronosLocalSettingsConfigModel extends AbstractLocalSettingsConfigModel
{
  private SchedulerManager manager = new SchedulerManager();
  private ValContext parseValContext;
  @ALocalSettingsPath(comment = "Virtual Hostname used by chronos")
  private String virtualHostName;
  @ALocalSettingsPath(comment = "Node binding time in milliseconds", defaultValue = "3600000")
  private String minNodeBindTime;

  public ChronosLocalSettingsConfigModel()
  {
    parseValContext = new ValContext().createSubContext(this, null);
  }

  @Override
  public void validate(ValContext ctx)
  {
    ctx.getMessages().addAll(parseValContext.getMessages());
    if (isLong(minNodeBindTime) == false) {
      ctx.directError("minNodeBindTime", "Require long value");
    } else {
      manager.setMinNodeBindTime(asLong(minNodeBindTime));
    }

  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    writer = super.toProperties(writer);
    writeSchedulerFactories(writer);
    // TODO continue
    return writer;

  }

  private void writeSchedulerFactories(LocalSettingsWriter writer)
  {
    for (SchedulerFactory sf : manager.getScheduleFactories()) {
      writeSchedulerFactory(sf, writer);
    }

  }

  private void writeSchedulerFactory(SchedulerFactory sf, LocalSettingsWriter writer)
  {
    LocalSettingsWriter w = writer.newSection("Scheduler Definition Chronos");
    String prefix = getKeyPrefix() + ".scheduler." + sf.getSchedulerName();

    w.put(prefix + ".name", sf.getSchedulerName(), "Name of the Scheduler");

    w.put(prefix + ".threadPoolSize", sf.getThreadPoolSize(), "Number of Threads for the Scheduler");
    // TODO RK continue

  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    parseValContext.getMessages().clear();
    super.fromLocalSettings(localSettings);
    List<String> keys = localSettings.getKeysPrefixWithInfix(getKeyPrefix() + ".scheduler", "name");
    for (String key : keys) {
      manager.getScheduleFactories().add(parseScheduler(key, localSettings));
    }
    keys = localSettings.getKeysPrefixWithInfix(getKeyPrefix() + ".standardJobs", "name");
    for (String key : keys) {
      JobBeanDefinition jd = parseJobDefinition(key, localSettings);
      if (jd != null) {
        manager.getStandardJobs().add(jd);
      }
    }
  }

  private JobBeanDefinition parseJobDefinition(String key, LocalSettings localSettings)
  {
    JobBeanDefinition ret = new JobBeanDefinition();
    String name = localSettings.get(key + ".name");
    ret.setBeanName(name);
    String jobArgument = localSettings.get(key + ".jobArgument");
    ret.setJobArgument(jobArgument);
    ret.setJobName(name);
    String schedulerName = localSettings.get(key + ".schedulerName");

    ret.setSchedulerName(schedulerName);
    String jobClass = localSettings.get(key + ".jobClass");
    if (StringUtils.isBlank(jobClass) == true) {
      parseValContext.directError(key + ".jobClass", "jobClass is required");
      return null;
    }
    ClassJobDefinition jd = new ClassJobDefinition(jobClass);

    //jd.setBeanProperties(beanProperties);
    ret.setJobDefinition(jd);
    return ret;
  }

  protected SchedulerFactory parseScheduler(String key, LocalSettings localSettings)
  {
    String name = localSettings.get(key + ".name");
    SchedulerFactory fac = new SchedulerFactory();
    fac.setSchedulerName(name);
    String startOnCreate = localSettings.get(key + ".startOnCreate");
    if (StringUtils.isNotBlank(startOnCreate) == true) {
      fac.setStartOnCreate(Boolean.valueOf(startOnCreate));
    }
    String startupTimeout = localSettings.get(key + ".startupTimeout");
    if (StringUtils.isNotBlank(startupTimeout) == true) {
      fac.setStartupTimeout(Integer.valueOf(startOnCreate));
    }
    String jobRetryTime = localSettings.get(key + ".jobRetryTime");
    if (StringUtils.isNotBlank(jobRetryTime) == true) {
      fac.setJobRetryTime(Integer.valueOf(jobRetryTime));
    }
    String jobMaxRetryCount = localSettings.get(key + ".jobMaxRetryCount");
    if (StringUtils.isNotBlank(jobMaxRetryCount) == true) {
      fac.setJobMaxRetryCount(Integer.valueOf(jobMaxRetryCount));
    }
    String serviceRetryTime = localSettings.get(key + ".serviceRetryTime");
    if (StringUtils.isNotBlank(serviceRetryTime) == true) {
      fac.setServiceRetryTime(Integer.valueOf(serviceRetryTime));
    }
    String threadPoolSize = localSettings.get(key + ".threadPoolSize");
    if (StringUtils.isNotBlank(threadPoolSize) == true) {
      fac.setThreadPoolSize(Integer.valueOf(threadPoolSize));
    }
    String nodeBindingTimeout = localSettings.get(key + ".nodeBindingTimeout");
    if (StringUtils.isNotBlank(nodeBindingTimeout) == true) {
      fac.setNodeBindingTimeout(Integer.valueOf(nodeBindingTimeout));
    }
    return fac;
  }

  @Override
  public String getKeyPrefix()
  {
    return "genome.chronos";
  }

  public SchedulerManager getManager()
  {
    return manager;
  }

  public void setManager(SchedulerManager manager)
  {
    this.manager = manager;
  }

  public String getVirtualHostName()
  {
    return virtualHostName;
  }

  public void setVirtualHostName(String virtualHostName)
  {
    this.virtualHostName = virtualHostName;
  }

  public String getMinNodeBindTime()
  {
    return minNodeBindTime;
  }

  public void setMinNodeBindTime(String minNodeBindTime)
  {
    this.minNodeBindTime = minNodeBindTime;
  }

}
