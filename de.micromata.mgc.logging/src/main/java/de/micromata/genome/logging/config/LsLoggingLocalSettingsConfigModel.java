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

package de.micromata.genome.logging.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.logging.config.LsLoggingService.LsLoggingDescription;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.logging.spi.log4j.GLogAppender;
import de.micromata.genome.logging.spi.log4j.Log4JLogAttributeType;
import de.micromata.genome.logging.spi.log4j.Log4JLogCategory;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LsLoggingLocalSettingsConfigModel extends BaseLoggingLocalSettingsConfigModel
{

  /**
   * Created logging
   */
  private BaseLoggingLocalSettingsConfigModel nested;

  @ALocalSettingsPath(defaultValue = "false", comment = "Write Log4J Logs into Genome Logging")
  private String log4JToGenomeLogging;

  public LsLoggingLocalSettingsConfigModel()
  {

  }

  public LsLoggingLocalSettingsConfigModel(String lsPrefix)
  {
    super(lsPrefix);
  }

  @Override
  public void validate(ValContext ctx)
  {
    if (nested == null) {
      ctx.directError("typeId", "No logging selected");
      return;
    }
    nested.validate(ctx);
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    LsLoggingDescription desc = findByTypeId(getTypeId());
    if (desc == null) {
      return;
    }
    nested = desc.getConfigModel();
    nested.fromLocalSettings(localSettings);
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    writer.put(getKeyPrefix() + ".log4JToGenomeLogging", log4JToGenomeLogging);
    if (nested != null) {
      nested.setPrefix(getKeyPrefix());
      return nested.toProperties(writer);
    }
    return writer;
  }

  @Override
  public void initializeConfiguration()
  {
    nested.initializeConfiguration();
    Logging logging = createLogging();
    LoggingServiceManager.get().setLogging(logging);
    if (isLog4JToGenomeLogging() == true) {
      Log4JLogCategory.values();
      Log4JLogAttributeType.values();
      new GLogAppender().register();
    }

  }

  public static LsLoggingDescription findByTypeId(String typeId)
  {
    List<LsLoggingDescription> services = getAvailableServices();
    Optional<LsLoggingDescription> optional = services.stream().filter(e -> e.typeId().equals(typeId)).findFirst();
    if (optional.isPresent() == false) {
      return null;
    }
    return optional.get();

  }

  private void propagateLoggingConfig(Logging logging)
  {
    if ((logging instanceof BaseLogging) == false) {
      return;
    }
    BaseLogging baseLogging = (BaseLogging) logging;
    baseLogging.setMaxLogAttrLength(getMaxLogAttrLengthAsInt());

  }

  @Override
  public Logging createLogging()
  {
    Logging ret = nested.createLogging();
    propagateLoggingConfig(ret);
    return ret;
  }

  public static List<LsLoggingDescription> getAvailableServices()
  {
    List<LsLoggingDescription> ret = new ArrayList<>();
    ServiceLoader<LsLoggingService> loader = ServiceLoader.load(LsLoggingService.class);
    for (LsLoggingService lgs : loader) {
      ret.addAll(lgs.getLsLoggingImpls());
    }
    return ret;
  }

  public BaseLoggingLocalSettingsConfigModel getNested()
  {
    return nested;
  }

  public void setNested(BaseLoggingLocalSettingsConfigModel nested)
  {
    this.nested = nested;
  }

  public boolean isLog4JToGenomeLogging()
  {
    return Boolean.valueOf(log4JToGenomeLogging);
  }

  public String getLog4JToGenomeLogging()
  {
    return log4JToGenomeLogging;
  }

  public void setLog4JToGenomeLogging(String log4jToGenomeLogging)
  {
    log4JToGenomeLogging = log4jToGenomeLogging;
  }

}
