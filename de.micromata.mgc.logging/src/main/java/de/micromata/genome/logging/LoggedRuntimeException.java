/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   03.07.2006
// Copyright Micromata 03.07.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import org.apache.commons.lang.ArrayUtils;

/**
 * Exception, die strukturiert geloggt wird.
 *
 * @author roger
 */
public class LoggedRuntimeException extends LogAttributeRuntimeException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -8526594930837970168L;

  /**
   * Instantiates a new logged runtime exception.
   *
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LoggedRuntimeException(LogLevel loglevel, LogCategory category, String message, LogAttribute... attributes)
  {
    super(message, null, true, attributes);
    writeLogMsg(loglevel, category, message, null, attributes);
  }

  /**
   * Instantiates a new logged runtime exception.
   *
   * @param cause the cause
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LoggedRuntimeException(Throwable cause, LogLevel loglevel, LogCategory category, String message, LogAttribute... attributes)
  {
    super(message, cause, true, attributes);
    writeLogMsg(loglevel, category, message, cause, attributes);
  }

  /**
   * Write log msg.
   *
   * @param log the log
   * @param category the category
   * @param message the message
   * @param cause the cause
   * @param attributes the attributes
   */
  private void writeLogMsg(LogLevel log, LogCategory category, String message, Throwable cause, LogAttribute[] attributes)
  {
    if (cause != null) {
      attributes = (LogAttribute[]) ArrayUtils.add(attributes, new LogExceptionAttribute(cause));
    }
    GLog.doLog(log, category, message, attributes);
  }
}
