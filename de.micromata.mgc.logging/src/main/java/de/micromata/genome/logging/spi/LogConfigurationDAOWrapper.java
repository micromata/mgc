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

package de.micromata.genome.logging.spi;

import java.util.List;

import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.util.types.Pair;

/**
 * Wrapper to LogConfigurationDAO.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogConfigurationDAOWrapper implements LogConfigurationDAO
{
  private LogConfigurationDAO target;

  public LogConfigurationDAOWrapper()
  {

  }

  public LogConfigurationDAOWrapper(LogConfigurationDAO target)
  {
    this.target = target;
  }

  @Override
  public LogLevel getThreshold()
  {
    return target.getThreshold();
  }

  @Override
  public void setThreshold(LogLevel level)
  {
    target.setThreshold(level);
  }

  @Override
  public boolean isLogEnabled(LogLevel logLevel)
  {
    return target.isLogEnabled(logLevel);
  }

  @Override
  public boolean isLogEnabled(LogLevel logLevel, String category, String msg)
  {
    return target.isLogEnabled(logLevel, category, msg);
  }

  @Override
  public void setLogLevel(LogLevel logLevel, String pattern)
  {
    target.setLogLevel(logLevel, pattern);
  }

  @Override
  public void resetLogLevelRules()
  {
    target.resetLogLevelRules();
  }

  @Override
  public List<Pair<String, LogLevel>> getLogLevelRules()
  {
    return target.getLogLevelRules();
  }

  @Override
  public boolean filterView(LogEntry lwe)
  {
    return target.filterView(lwe);
  }

  public LogConfigurationDAO getTarget()
  {
    return target;
  }

  public void setTarget(LogConfigurationDAO target)
  {
    this.target = target;
  }

}
