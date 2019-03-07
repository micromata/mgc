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

package de.micromata.genome.logging.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;

/**
 * Adapter for java.util.logging: delegates all Log-Events from jul to genome-Logging. WARNING: This implementation
 * removes the default {@link ConsoleHandler} on the loggers if not overwritten by setting {@link #replaceConsole} to
 * <code>false</code>.
 *
 * @author wolle
 *
 */
public class GJLogHandler extends Handler
{

  /**
   * The log adapter helper.
   */
  private GenomeLogAdapterHelper logAdapterHelper = new GenomeLogAdapterHelper();

  /**
   * The old handlers.
   */
  private Map<String, Handler> oldHandlers = new HashMap<String, Handler>();

  /**
   * The replace console.
   */
  private boolean replaceConsole = true;

  /**
   * If set to <code>false</code>, the console logger is not replaced automatically
   * @param replaceConsole this value is set on the inner field this.replaceConsole
   */
  public void setReplaceConsole(boolean replaceConsole)
  {
    this.replaceConsole = replaceConsole;
  }

  /**
   * Removes the default {@link ConsoleHandler} and adds the genome handler instead. The old handlers were stored in the
   * map {@link #oldHandlers}
   */

  public void afterPropertiesSet()
  {

    LogManager logManager = LogManager.getLogManager();
    Enumeration<String> loggerNames = logManager.getLoggerNames();
    while (loggerNames.hasMoreElements()) {
      String name = loggerNames.nextElement();
      Logger logger = logManager.getLogger(name);
      Handler[] handlers = logger.getHandlers();
      // remove annoying stdout handler
      for (Handler oldHandler : handlers) {
        if (oldHandler instanceof ConsoleHandler && replaceConsole == true) {
          ConsoleHandler handler = (ConsoleHandler) oldHandler;
          oldHandlers.put(name, handler);
          logger.removeHandler(handler);
        }
      }
      // add Genome Logger
      if (logger.getUseParentHandlers() == false || name.equals("")) {
        logger.addHandler(this);
      }
    }
  }

  /**
   * Restore the old java.util.logging configuration by removing the genome handler and adding the previous saved
   * {@link Handler} for each known {@link Logger}
   */
  public void destroy()
  {
    LogManager logManager = LogManager.getLogManager();
    Enumeration<String> loggerNames = logManager.getLoggerNames();
    while (loggerNames.hasMoreElements()) {
      String name = loggerNames.nextElement();
      Logger logger = logManager.getLogger(name);
      Handler[] handlers = logger.getHandlers();
      // remove annoying stdout handler
      for (Handler oldHandler : handlers) {
        if (oldHandler instanceof GJLogHandler) {
          GJLogHandler handler = (GJLogHandler) oldHandler;
          logger.removeHandler(handler);
        }
      }
      // add Genome Logger
      if (oldHandlers.get(name) != null && replaceConsole == true) {
        logger.addHandler(oldHandlers.get(name));
      }
    }
  }

  @Override
  public void publish(LogRecord record)
  {

    if (GenomeLogAdapterHelper.getRecursiveGuard().get() != null
        && GenomeLogAdapterHelper.getRecursiveGuard().get() == Boolean.TRUE) {
      return;
    }

    String logName = record.getLoggerName();
    if (logAdapterHelper.ignoreCat(logName) == true) {
      return;
    }

    try {
      GenomeLogAdapterHelper.getRecursiveGuard().set(Boolean.TRUE);
      Level level = record.getLevel();
      LogLevel logLevel = mapLevelToLogLevel(level);

      LogCategory cat = logAdapterHelper.createCat(logName);
      String msg = record.getMessage();
      List<LogAttribute> las = new ArrayList<LogAttribute>();
      if (record.getParameters() != null) {
        las.add(new LogAttribute(GenomeAttributeType.Miscellaneous2, Arrays.toString(record.getParameters())));
      }
      if (record.getSourceClassName() != null) {
        las.add(new LogAttribute(GenomeAttributeType.ClassName, record.getSourceClassName()));
      }
      if (record.getSourceMethodName() != null) {
        las.add(new LogAttribute(GenomeAttributeType.Miscellaneous, record.getSourceMethodName()));
      }
      las.add(new LogAttribute(GenomeAttributeType.ThreadContext, Integer.toString(record.getThreadID())));
      if (record.getThrown() != null) {
        las.add(new LogExceptionAttribute(record.getThrown()));
      }
      LogAttribute[] la = las.toArray(new LogAttribute[las.size()]);
      GLog.doLog(logLevel, cat, msg, la);

    } finally {
      GenomeLogAdapterHelper.getRecursiveGuard().set(Boolean.FALSE);
    }
  }

  /**
   * Map level to log level.
   *
   * @param level the level
   * @return the log level
   */
  private LogLevel mapLevelToLogLevel(Level level)
  {
    if (level.intValue() >= Level.SEVERE.intValue()) {
      return LogLevel.Error;
    }
    if (level.intValue() >= Level.WARNING.intValue()) {
      return LogLevel.Warn;
    }
    if (level.intValue() >= Level.CONFIG.intValue()) {
      return LogLevel.Note;
    }
    if (level.intValue() >= Level.INFO.intValue()) {
      return LogLevel.Info;
    }
    if (level.intValue() >= Level.FINE.intValue()) {
      return LogLevel.Info;
    }
    if (level.intValue() >= Level.FINER.intValue()) {
      return LogLevel.Trace;
    }
    if (level.intValue() >= Level.FINEST.intValue()) {
      return LogLevel.Debug;
    }
    return LogLevel.Warn;
  }

  @Override
  public void flush()
  {
  }

  @Override
  public void close()
  {
  }

  public void setIgnoreNamespaces(String ignoreNamespaces)
  {
    logAdapterHelper.setIgnoreNamespaces(ignoreNamespaces);
  }

  public void setStripNamespaces(String stripNamespaces)
  {
    logAdapterHelper.setStripNamespaces(stripNamespaces);
  }

}
