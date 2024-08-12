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

import java.lang.reflect.InvocationTargetException;

import jakarta.servlet.ServletException;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Utility-Klasse für das Exception Framework.
 * 
 * @author jens@micromata.de
 */
public class LoggableExceptionHandlingUtil
{

  /**
   * logs first LoggableRuntimeException on the cause stack if it has not been logged before and returns what has been
   * logged.
   *
   * @param loglevel how well is the reaction. overwrites captured loglevel may also be null to not overwrite
   * @param category overwrites captured category (old is saved in an attribute) may also be null to not overwrite
   * @param message overwrites captured logmessage (old is saved in an attribute) may also be null to not overwrite
   * @param e the e
   * @param attributes additional attributes
   * @return first logged or loggable exception on the cause stack or null if no such is found
   */
  public static LogAttributeRuntimeException logFirstLoggableRuntimeExceptionOnCauseStack(LogLevel loglevel,
      LogCategory category,
      String message, Throwable e, LogAttribute... attributes)
  {
    LogAttributeRuntimeException le = getFirstLoggableRuntimeExceptionOnCauseStack(e);
    if (le instanceof LoggableRuntimeException) {
      LoggableRuntimeException lre = (LoggableRuntimeException) le;
      lre.writeLogMsg(loglevel, category, message, attributes);
      return lre;
    }
    return le;
  }

  /**
   * Gets the first loggable runtime exception on cause stack.
   *
   * @param e the e
   * @return first logged or loggable exception on the cause stack or null if no such is found
   */
  public static LogAttributeRuntimeException getFirstLoggableRuntimeExceptionOnCauseStack(Throwable e)
  {
    if (e instanceof LoggedRuntimeException) {
      return (LoggedRuntimeException) e;
    }
    if (e instanceof LoggableRuntimeException) {
      return (LoggableRuntimeException) e;
    }
    Throwable cause = getRootCause(e);
    if ((cause != null) && (cause != e)) {
      return getFirstLoggableRuntimeExceptionOnCauseStack(cause);
    }
    return null;
  }

  /**
   * Gets the root cause.
   *
   * @param e the e
   * @return the root cause
   */
  public static Throwable getRootCause(Throwable e)
  {
    if (e == null) {
      return null;
    }
    if (e instanceof ServletException) {
      return ((ServletException) e).getRootCause();
    }

    if (e instanceof InvocationTargetException) {
      return ((InvocationTargetException) e).getTargetException();
    }
    return e.getCause();
  }

  /**
   * eats up an exception. Only use this if you know how to react to the exception
   *
   * @param loglevel how well is the reaction. overwrites captured loglevel may also be null to not overwrite
   * @param category overwrites captured category (old is saved in an attribute) may also be null to not overwrite
   * @param message overwrites captured logmessage (old is saved in an attribute) may also be null to not overwrite
   * @param e Exception to be logged. Is added to attributes by this method.
   * @param attributes additional params
   */
  public static void logException(LogLevel loglevel, LogCategory category, String message, final Throwable e,
      LogAttribute... attributes)
  {
    if (e != null) {
      LogAttributeRuntimeException le = logFirstLoggableRuntimeExceptionOnCauseStack(loglevel, category, message, e,
          attributes);
      if (le == e) {
        return;
      }
    }
    if (category == null) {
      category = GenomeLogCategory.Coding;
    }
    if (loglevel == null) {
      loglevel = LogLevel.Warn;
    }
    if (message == null) {
      message = "null message";
    }
    boolean nestedInServletException = false;
    Throwable re = e;
    while (re instanceof ServletException) {
      nestedInServletException = true;
      Throwable cause = ((ServletException) re).getRootCause();
      if (cause == re) {
        re = null;
      } else {
        re = cause;
      }
    }
    if (re == null) {
      if (nestedInServletException == true) {
        re = e;
        nestedInServletException = false;
      }
    }
    if (re == null) {
      LoggingServiceManager.get().getLogging().doLog(loglevel, category, message);
      return;
    }
    if (nestedInServletException == true) {
      message = "Nested in " + e.getClass().getSimpleName() + ": " + message;
    }
    LoggingServiceManager.get().getLogging().doLog(loglevel, category, message,
        (LogAttribute[]) ArrayUtils.add(attributes, new LogExceptionAttribute(re)));
  }

  /**
   * Compat function only.
   *
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param e the e
   * @param attributes the attributes
   */
  public static void logException(LogLevel loglevel, GenomeLogCategory category, String message, final Throwable e,
      LogAttribute... attributes)
  {
    logException(loglevel, (LogCategory) category, message, e, attributes);
  }

  /**
   * Unwrapp exception.
   *
   * @param ex the ex
   * @return the throwable
   */
  static Throwable unwrappException(Throwable ex)
  {
    Throwable exlocal = null;
    if (ex instanceof InvocationTargetException) {
      exlocal = ((InvocationTargetException) ex).getTargetException();
    } else if (ex instanceof ServletException) {
      exlocal = ((ServletException) ex).getRootCause();
    }
    if (exlocal != null) {
      return exlocal;
    }
    return ex;
  }

  /**
   * stellt sicher, dass die Exception in eine LoggableRuntimeException umgewandelt wird und sowohl logfelder der
   * bisherigen exception als auch die neuen angegebenen exception nicht verloren gehen.
   * 
   * @param category never null
   * @param level never null
   * @param msg never null
   * @param e may be null which means throw new exception
   */
  public static void throwLoggableRuntimeException(LogCategory category, LogLevel level, String msg, Exception e)
  {
    if (e == null) {
      throw new LoggableRuntimeException(level, category, msg);
    }
    LogAttributeRuntimeException lae = getFirstLoggableRuntimeExceptionOnCauseStack(e);
    if (lae instanceof LoggableRuntimeException) {
      LoggableRuntimeException lre = (LoggableRuntimeException) lae;
      addToMessageStack(lre, category, level, msg);
      throw lre;
    } else {
      throw new LoggableRuntimeException(level, category, msg, new LogExceptionAttribute(e));
    }
  }

  /**
   * Adds the to message stack.
   *
   * @param lre never null
   * @param category never null
   * @param level never null
   * @param msg never null
   */
  private static void addToMessageStack(LoggableRuntimeException lre, LogCategory category, LogLevel level, String msg)
  {
    LogAttribute attr = lre.getLogAttributeMap().get(GenomeAttributeType.MessageStackTrace.name());
    if (attr == null) {
      attr = new LogAttribute(GenomeAttributeType.MessageStackTrace, lre.getCategory().name()
          + "; "
          + lre.getLoglevel().name()
          + ": "
          + lre.getMessage());
      lre.getLogAttributeMap().put(GenomeAttributeType.MessageStackTrace.name(), attr);
    }
    attr.setValue(category.name() + "; " + level.name() + ": " + msg + "\ncaused by: " + attr.getValue());

    // TODO R3V3 loglevel und message direkt in Exception setzen
  }
}
