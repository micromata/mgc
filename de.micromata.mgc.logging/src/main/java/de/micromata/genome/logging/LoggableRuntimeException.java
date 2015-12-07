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

import java.util.Collection;
import java.util.Collections;

/**
 * Exception, die strukturiert einmalig an dem Punkt geloggt werden sollte, wo man auf die Fehlersituation akkurat
 * reagieren und daher entscheiden kann, ob die Meldung wirklich geloggt werden muss. FALSCH ist es diese Exception zu
 * loggen und danach wieder (eingepackt) weiterzuwerfen (wuerde zu Doppeltem Logging fuehren) ! Es ist in Ordnung sie
 * einfach unbehandelt weiterzuwerfen, wenn nicht klar ist, wie darauf reagiert werden kann.
 * 
 * Captures LogAttributes in LogContext.
 */
public class LoggableRuntimeException extends LogAttributeRuntimeException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -8526594930837970168L;

  /**
   * The Constant DEFAULT_LOGLEVEL.
   */
  final protected static LogLevel DEFAULT_LOGLEVEL = LogLevel.Warn;

  /**
   * The user message.
   */
  private final String userMessage;

  /**
   * The loglevel.
   */
  private final LogLevel loglevel;

  /**
   * The category.
   */
  private final LogCategory category;

  /**
   * Instantiates a new loggable runtime exception.
   *
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LoggableRuntimeException(LogCategory category, String message, LogAttribute... attributes)
  {
    this(DEFAULT_LOGLEVEL, category, message, attributes);
  }

  /**
   * Instantiates a new loggable runtime exception.
   *
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LoggableRuntimeException(LogLevel loglevel, LogCategory category, String message, LogAttribute... attributes)
  {
    this(null, loglevel, category, message, message, attributes);
  }

  /**
   * Instantiates a new loggable runtime exception.
   *
   * @param category the category
   * @param message the message
   * @param userMessage the user message
   * @param attributes the attributes
   */
  public LoggableRuntimeException(LogCategory category, String message, String userMessage, LogAttribute... attributes)
  {
    this(DEFAULT_LOGLEVEL, category, message, message, attributes);
  }

  /**
   * Instantiates a new loggable runtime exception.
   *
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param userMessage the user message
   * @param attributes the attributes
   */
  public LoggableRuntimeException(LogLevel loglevel, LogCategory category, String message, String userMessage,
      LogAttribute... attributes)
  {
    this(null, loglevel, category, message, userMessage, attributes);
  }

  /**
   * Instantiates a new loggable runtime exception.
   *
   * @param cause the cause
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LoggableRuntimeException(Throwable cause, LogCategory category, String message, LogAttribute... attributes)
  {
    this(cause, DEFAULT_LOGLEVEL, category, message, attributes);
  }

  /**
   * Instantiates a new loggable runtime exception.
   *
   * @param cause the cause
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LoggableRuntimeException(Throwable cause, LogLevel loglevel, LogCategory category, String message,
      LogAttribute... attributes)
  {
    this(cause, loglevel, category, message, message, attributes);
  }

  /**
   * Instantiates a new loggable runtime exception.
   *
   * @param cause the cause
   * @param category the category
   * @param message the message
   * @param userMessage the user message
   * @param attributes the attributes
   */
  public LoggableRuntimeException(Throwable cause, LogCategory category, String message, String userMessage,
      LogAttribute... attributes)
  {
    this(cause, DEFAULT_LOGLEVEL, category, message, userMessage, attributes);
  }

  /**
   * Instantiates a new loggable runtime exception.
   *
   * @param cause the cause
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param userMessage the user message
   * @param attributes the attributes
   */
  public LoggableRuntimeException(Throwable cause, LogLevel loglevel, LogCategory category, String message,
      String userMessage,
      LogAttribute... attributes)
  {
    super(message, cause, true, attributes);
    this.loglevel = loglevel;
    this.category = category;
    this.userMessage = userMessage;
  }

  /**
   * schreibt die Logmeldung. FALSCH ist es diese Exception zu loggen und danach wieder (eingepackt) weiterzuwerfen
   * (wuerde zu Doppeltem Logging fuehren) ! Es ist in Ordnung sie einfach unbehandelt weiterzuwerfen, wenn nicht klar
   * ist, wie darauf reagiert werden kann.
   *
   * @param logging the logging
   * @param loglevel how well is the reaction. overwrites captured loglevel may also be null to not overwrite
   * @param category overwrites captured category (old is saved in an attribute) may also be null to not overwrite
   * @param message overwrites captured logmessage (old is saved in an attribute) may also be null to not overwrite
   * @param attributes additional attributes
   */
  public void writeLogMsg(Logging logging, LogLevel loglevel, LogCategory category, String message,
      LogAttribute... attributes)
  {
    // not the best solution - in theory two connected logmessages should be written to reflect the rootcause and the reaction
    int specialAttrs = 1;
    LogLevel logLoglevel = (loglevel != null) ? loglevel : getLoglevel();
    LogCategory logCategory = getCategory();
    LogAttribute rootCategoryAttr = null;
    if ((category != null) && (category.equals(logCategory) == false)) {
      rootCategoryAttr = new LogAttribute(GenomeAttributeType.RootLogCategory, logCategory.name());
      specialAttrs++;
      logCategory = category;
    }
    String logMessage = getMessage();
    LogAttribute rootMessageAttr = null;
    if ((message != null) && (message.equals(logMessage) == false)) {
      rootMessageAttr = new LogAttribute(GenomeAttributeType.RootLogMessage, logMessage); // TODO: umstellen auf MessageStackTrace
      specialAttrs++;
      logMessage = message;
    }

    Collection<LogAttribute> capturedAttrs = getLogAttributes();
    if (capturedAttrs == null) {
      capturedAttrs = Collections.emptyList();
    }
    LogAttribute[] attrArray = capturedAttrs
        .toArray(new LogAttribute[capturedAttrs.size() + attributes.length + specialAttrs]);
    int firstIndexAfterCapturedAttrs = capturedAttrs.size();
    int i;
    for (i = 0; i < attributes.length; ++i) {
      attrArray[firstIndexAfterCapturedAttrs + i] = attributes[i];
    }
    i += firstIndexAfterCapturedAttrs;
    if (rootCategoryAttr != null) {
      attrArray[i++] = rootCategoryAttr;
    }
    if (rootMessageAttr != null) {
      attrArray[i++] = rootMessageAttr;
    }
    attrArray[i++] = new LogExceptionAttribute(this);
    logging.logLwe(new LogWriteEntry(logLoglevel, logCategory.name(), logMessage, attrArray));
  }

  public String getUserMessage()
  {
    return userMessage;
  }

  public LogLevel getLoglevel()
  {
    return loglevel;
  }

  public LogCategory getCategory()
  {
    return category;
  }

  /**
   * short form.
   *
   * @param loglevel how well is the reaction. overwrites captured loglevel may also be null to not overwrite
   * @param category overwrites captured category (old is saved in an attribute) may also be null to not overwrite
   * @param msg the msg
   * @param attributes additional attributes
   */
  public void writeLogMsg(LogLevel loglevel, LogCategory category, String msg, LogAttribute... attributes)
  {
    writeLogMsg(LoggingServiceManager.get().getLogging(), loglevel, category, msg, attributes);
  }

  /**
   * shorter form.
   *
   * @param category overwrites captured category (old is saved in an attribute) may also be null to not overwrite
   * @param msg the msg
   * @param attributes additional attributes
   */
  public void writeLogMsg(LogCategory category, String msg, LogAttribute... attributes)
  {
    writeLogMsg(LoggingServiceManager.get().getLogging(), getLoglevel(), category, msg, attributes);
  }

  /**
   * logs captured attributes.
   */
  public void writeLogMsg()
  {
    writeLogMsg(LoggingServiceManager.get().getLogging(), null, null, null);
  }
}
