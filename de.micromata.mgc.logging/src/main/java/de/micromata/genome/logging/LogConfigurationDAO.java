/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   26.01.2008
// Copyright Micromata 26.01.2008
//
/////////////////////////////////////////////////////////////////////////////
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
