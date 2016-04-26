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

package de.micromata.genome.logging.spi.log4j;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.util.types.Pair;

/**
 * Configuration interface to Log4J.
 *
 * @author roger@micromata.de
 */
public class Log4JLogConfigurationDAOImpl implements LogConfigurationDAO
{

  /**
   * The log4j category prefix.
   */
  private String log4jCategoryPrefix = "de.micromata.genome.logging.";

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogConfigurationDAO#isLogEnabled(de.micromata.genome.logging.LogLevel)
   */
  @Override
  public boolean isLogEnabled(LogLevel logLevel)
  {
    return Logger.getRootLogger().isEnabledFor(Log4JLogging.mapLoglevel(logLevel));
  }

  /**
   * Map level to log level.
   *
   * @param level the level
   * @return the log level
   */
  public static LogLevel mapLevelToLogLevel(Level level)
  {
    switch (level.toInt()) {
      case Priority.OFF_INT:
        return LogLevel.Fatal;
      case Priority.FATAL_INT:
        return LogLevel.Fatal;
      case Priority.ERROR_INT:
        return LogLevel.Error;
      case Priority.WARN_INT:
        return LogLevel.Warn;
      case Priority.INFO_INT:
        return LogLevel.Info;
      case Priority.DEBUG_INT:
        return LogLevel.Debug;
      default:
        return LogLevel.Note;
    }
  }

  @Override
  public LogLevel getThreshold()
  {
    return mapLevelToLogLevel(LogManager.getLoggerRepository().getThreshold());
  }

  @Override
  public void setThreshold(LogLevel logLevel)
  {
    Level l = Log4JLogging.mapLogLevelToLevel(logLevel);
    LogManager.getLoggerRepository().setThreshold(l);

  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogConfigurationDAO#isLogEnabled(de.micromata.genome.logging.LogLevel,
   * java.lang.String, java.lang.String)
   */
  @Override
  public boolean isLogEnabled(LogLevel logLevel, String categoryNamePart, String message)
  {
    // NOT append message, because this results to a memory leak in org.apache.log4j.Hierarchy
    Logger log = Logger.getLogger(log4jCategoryPrefix + categoryNamePart);
    return log.isEnabledFor(Log4JLogging.mapLoglevel(logLevel));
  }

  @Override
  public void setLogLevel(LogLevel logLevel, String pattern)
  {
    Level l = Log4JLogging.mapLogLevelToLevel(logLevel);
    // TODO genome implement setting logLevel
    LogManager.getLoggerRepository().setThreshold(l);
  }

  @Override
  public void resetLogLevelRules()
  {

  }

  @Override
  public boolean filterView(LogEntry lwe)
  {
    return true;
  }

  @Override
  public List<Pair<String, LogLevel>> getLogLevelRules()
  {
    List<Pair<String, LogLevel>> ret = new ArrayList<Pair<String, LogLevel>>();
    return ret;
  }

  public String getLog4jCategoryPrefix()
  {
    return log4jCategoryPrefix;
  }

  public void setLog4jCategoryPrefix(String log4jCategoryPrefix)
  {
    this.log4jCategoryPrefix = log4jCategoryPrefix;
  }

}
