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

package de.micromata.genome.logging;

import java.util.List;

import de.micromata.genome.util.types.Pair;

/**
 * Interface for configuration of Logging Domain Logging Category LogLevel properties analog zu log4j.
 *
 * @author roger@micromata.de
 */
public interface LogConfigurationDAO
{
  /**
   * @return the minimum LogLevel
   */
  public LogLevel getThreshold();

  /**
   * Sets the threshold for LogLevel
   * 
   * @param level
   */
  public void setThreshold(LogLevel level);

  /**
   * return true, if the given LogLevel is enabled
   * 
   * The LogLevel is a global minimum LogLevel.
   *
   * @param logLevel the log level
   * @return true, if is log enabled
   */
  public boolean isLogEnabled(LogLevel logLevel);

  /**
   * return true, if logging for given logLevel and category is enabled.
   *
   * @param logLevel the log level
   * @param category the category
   * @param msg the msg
   * @return true, if is log enabled
   */
  public boolean isLogEnabled(LogLevel logLevel, String category, String msg);

  /**
   * Sets the log level.
   *
   * @param logLevel if logLevel is null delete this entry
   * @param pattern fileMatcher pattern
   */
  public void setLogLevel(LogLevel logLevel, String pattern);

  /**
   * Cleares all logLevel Rules.
   */
  public void resetLogLevelRules();

  /**
   * 
   * @return the configuration values with the rules
   */
  public List<Pair<String, LogLevel>> getLogLevelRules();

  /**
   * Filter given Log Entry.
   * 
   * The filter may modify lwe, f.e. also remove Attributes
   *
   * @param lwe the lwe
   * @return false do not display this LogEntry
   */
  public boolean filterView(LogEntry lwe);

}
