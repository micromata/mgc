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

package de.micromata.genome.db.jpa.logging;

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.logging.config.LoggingWithFallbackLocalSettingsConfigModel;
import de.micromata.genome.logging.config.LsLoggingService;

/**
 * Loader service for base logging implementations.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JPALsLoggingServiceImpl implements LsLoggingService
{

  @Override
  public List<LsLoggingDescription> getLsLoggingImpls()
  {
    List<LsLoggingDescription> ret = new ArrayList<>();
    ret.add(createJpaLogging());
    return ret;
  }

  LsLoggingDescription createJpaLogging()
  {
    return new LsLoggingDescription()
    {

      @Override
      public String typeId()
      {
        return "jpa";
      }

      @Override
      public String toString()
      {
        return "Genome Database Logging via JPA";
      }

      @Override
      public String description()
      {
        return "Need to declare datasource under JNDI java:comp/env/genome/jdbc/dsLogging";
      }

      @Override
      public LoggingWithFallbackLocalSettingsConfigModel getConfigModel()
      {
        LoggingWithFallbackLocalSettingsConfigModel ret = new JpaLoggingLocalSettingsConfigModel();
        ret.setTypeId(typeId());
        return ret;
      }

    };
  }

  @Override
  public List<LsLogConfigurationDescription> getLsLogConfigurationImpls()
  {
    List<LsLogConfigurationDescription> ret = new ArrayList<>();

    return ret;
  }

}
