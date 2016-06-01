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

import de.micromata.genome.logging.spi.log4j.Log4JLogging;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.genome.util.validation.ValState;
import java.util.function.Supplier;

/**
 * Simplified interface for logging.
 *
 * @author roger@micromata.de
 */
public class GLog
{

  /**
   * The fall back logging.
   */
  public static Logging fallBackLogging = new Log4JLogging();

  /**
   * Do log.
   *
   * @param logLevel the log level
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public static void doLog(LogLevel logLevel, LogCategory cat, String msg, LogAttribute... attributes)
  {
    try {
      LoggingServiceManager.get().getLogging().doLog(logLevel, cat, msg, attributes);
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      fallBackLogging.doLog(logLevel, cat, msg, attributes);
    }
  }

  /**
   * Debug.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public static void debug(LogCategory cat, String msg, LogAttribute... attributes)
  {
    try {
      LoggingServiceManager.get().getLogging().debug(cat, msg, attributes);
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      fallBackLogging.debug(cat, msg, attributes);
    }
  }

  /**
   * Trace.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public static void trace(LogCategory cat, String msg, LogAttribute... attributes)
  {
    try {
      LoggingServiceManager.get().getLogging().trace(cat, msg, attributes);
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      fallBackLogging.trace(cat, msg, attributes);
    }
  }

  /**
   * Info.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public static void info(LogCategory cat, String msg, LogAttribute... attributes)
  {
    try {
      LoggingServiceManager.get().getLogging().info(cat, msg, attributes);
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      fallBackLogging.info(cat, msg, attributes);
    }
  }

  /**
   * Note.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public static void note(LogCategory cat, String msg, LogAttribute... attributes)
  {
    try {
      LoggingServiceManager.get().getLogging().note(cat, msg, attributes);
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      fallBackLogging.warn(cat, msg, attributes);
    }
  }

  /**
   * Warn.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public static void warn(LogCategory cat, String msg, LogAttribute... attributes)
  {
    try {
      LoggingServiceManager.get().getLogging().warn(cat, msg, attributes);
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      fallBackLogging.warn(cat, msg, attributes);
    }
  }

  /**
   * Error.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public static void error(LogCategory cat, String msg, LogAttribute... attributes)
  {
    try {
      LoggingServiceManager.get().getLogging().error(cat, msg, attributes);
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      fallBackLogging.error(cat, msg, attributes);
    }
  }

  /**
   * Fatal.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public static void fatal(LogCategory cat, String msg, LogAttribute... attributes)
  {
    try {
      LoggingServiceManager.get().getLogging().fatal(cat, msg, attributes);
    } catch (Exception ex) { // NOSONAR "Illegal Catch" framework
      fallBackLogging.fatal(cat, msg, attributes);
    }
  }

  public static LogLevel valStateToLogLevel(ValState level)
  {
    switch (level) {
      case BlockingError:
        return LogLevel.Fatal;
      case Error:
        return LogLevel.Error;
      case Warning:
        return LogLevel.Warn;
      case Info:
        return LogLevel.Note;
      default:
        return LogLevel.Debug;
    }
  }

  /**
   * The message has to be translated before
   * 
   * @param cat
   * @param msg
   */
  public static void logValMessage(LogCategory cat, ValMessage msg)
  {
    LogLevel level = valStateToLogLevel(msg.getValState());
    String message = msg.getMessage();
    if (message == null) {
      message = "???" + msg.getI18nkey() + "???";
    }
    if (msg.getException() != null) {
      doLog(level, cat, message, new LogExceptionAttribute(msg.getException()));
    } else {
      doLog(level, cat, message);
    }
  }

  /**
   * Checks if is log enabled.
   *
   * @param ll the ll
   * @return true, if is log enabled
   */
  public static boolean isLogEnabled(LogLevel ll)
  {
    return LoggingServiceManager.get().getLogConfigurationDAO().isLogEnabled(ll);
  }

  /**
   * Checks if is log enabled.
   *
   * @param logLevel the log level
   * @param fqCategory the fq category
   * @param msg the msg
   * @return true, if is log enabled
   */
  public static boolean isLogEnabled(LogLevel logLevel, String fqCategory, String msg)
  {
    return LoggingServiceManager.get().getLogConfigurationDAO().isLogEnabled(logLevel, fqCategory, msg);
  }

  /**
   * Checks if is debug enabled.
   *
   * @param logCat the log cat
   * @param msg the msg
   * @return true, if is debug enabled
   */
  public static boolean isDebugEnabled(LogCategory logCat, String msg)
  {
    return isDebugEnabled(logCat.getFqName(), msg);
  }

  /**
   * Checks if is debug enabled.
   *
   * @param fqCategory the fq category
   * @param msg the msg
   * @return true, if is debug enabled
   */
  public static boolean isDebugEnabled(String fqCategory, String msg)
  {
    return isLogEnabled(LogLevel.Debug, fqCategory, msg);
  }

  /**
   * Checks if is trace enabled.
   *
   * @param logCat the log cat
   * @param msg the msg
   * @return true, if is trace enabled
   */
  public static boolean isTraceEnabled(LogCategory logCat, String msg)
  {
    return isTraceEnabled(logCat.getFqName(), msg);
  }

  /**
   * Checks if is trace enabled.
   *
   * @param fqCategory the fq category
   * @param msg the msg
   * @return true, if is trace enabled
   */
  public static boolean isTraceEnabled(String fqCategory, String msg)
  {
    return isLogEnabled(LogLevel.Trace, fqCategory, msg);
  }

  public static boolean isDebugEnabled()
  {
    return isLogEnabled(LogLevel.Debug);
  }

  public static boolean isNoteEnabled()
  {
    return isLogEnabled(LogLevel.Note);
  }

  public static boolean isInfoEnabled()
  {
    return isLogEnabled(LogLevel.Info);
  }

  public static boolean isTraceEnabled()
  {
    return isLogEnabled(LogLevel.Trace);
  }



  /**
   * Adds a {@link LogAttribute} for the execution of the callback.
   * Pushes existing overridden attributes temporarily on the stack and
   * automatically restores them after execution
   *
   * @param attrType attribute type
   * @param data attribute value
   * @param r the callback
   */
  public static void runWithLogAttribute(LogAttributeType attrType, String data, Runnable r)
  {
    try (ScopedLogContextAttribute scopedAttr = new ScopedLogContextAttribute(attrType, data)) {
      r.run();
    }
  }

  /**
   * Adds a {@link LogAttribute} for the execution of the callback.
   * Pushes existing overridden attributes temporarily on the stack and
   * automatically restores them after execution
   *
   * @param attrType attribute type
   * @param data attribute value
   * @param r the callback
   * @param <T> type of the callback return value
   * @return return value of the callback
   */
  public static <T> T runWithLogAttribute(LogAttributeType attrType, String data, Supplier<T> r)
  {
    try (ScopedLogContextAttribute scopedAttr = new ScopedLogContextAttribute(attrType, data)) {
      return r.get();
    }
  }
}
