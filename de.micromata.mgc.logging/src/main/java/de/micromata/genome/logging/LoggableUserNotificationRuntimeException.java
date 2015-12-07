/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   17.01.2009
// Copyright Micromata 17.01.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

/**
 * * Exception, die strukturiert einmalig an dem Punkt geloggt werden sollte, wo man auf die Fehlersituation akkurat reagieren und daher
 * entscheiden kann, ob die Meldung wirklich geloggt werden muss.
 * 
 * TODO minor constructor wrong!
 * 
 * @author roger@micromata.de
 * 
 */
public class LoggableUserNotificationRuntimeException extends LoggableRuntimeException implements UserNotification
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -6615274738420055615L;

  /**
   * The i18 n key.
   */
  private String i18NKey;

  /**
   * The message args.
   */
  private String[] messageArgs;

  /**
   * The form name.
   */
  private String formName;

  /**
   * Instantiates a new loggable user notification runtime exception.
   *
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param attributes the attributes
   */
  public LoggableUserNotificationRuntimeException(LogLevel loglevel, LogCategory category, String message, LogAttribute... attributes)
  {
    super(loglevel, category, message, message, attributes);
  }

  /**
   * Instantiates a new loggable user notification runtime exception.
   *
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param userMessage the user message
   * @param attributes the attributes
   */
  public LoggableUserNotificationRuntimeException(LogLevel loglevel, LogCategory category, String message, String userMessage,
      LogAttribute... attributes)
  {
    super(loglevel, category, message, userMessage, attributes);
  }

  /**
   * Instantiates a new loggable user notification runtime exception.
   *
   * @param cause the cause
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param userMessage the user message
   * @param attributes the attributes
   */
  public LoggableUserNotificationRuntimeException(Throwable cause, LogLevel loglevel, LogCategory category, String message,
      String userMessage, LogAttribute... attributes)
  {
    super(cause, loglevel, category, message, userMessage, attributes);
  }

  /**
   * Instantiates a new loggable user notification runtime exception.
   *
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param i18nKey the i18n key
   * @param args the args
   * @param formLocation the form location
   * @param attributes the attributes
   */
  public LoggableUserNotificationRuntimeException(LogLevel loglevel, LogCategory category, String message, String i18nKey, String[] args,
      String formLocation, LogAttribute... attributes)
  {
    super(loglevel, category, message, attributes);
  }

  /**
   * Instantiates a new loggable user notification runtime exception.
   *
   * @param cause the cause
   * @param loglevel the loglevel
   * @param category the category
   * @param message the message
   * @param i18nKey the i18n key
   * @param args the args
   * @param formLocation the form location
   * @param attributes the attributes
   */
  public LoggableUserNotificationRuntimeException(Throwable cause, LogLevel loglevel, LogCategory category, String message, String i18nKey,
      String[] args, String formLocation, LogAttribute... attributes)
  {
    super(cause, loglevel, category, message, attributes);
    this.formName = formLocation;
    this.messageArgs = args;
    this.i18NKey = i18nKey;
  }

  @Override
  public String getDirectMessage()
  {
    return super.getUserMessage();
  }

  @Override
  public String getFormName()
  {
    return formName;
  }

  @Override
  public String getI18NKey()
  {
    return i18NKey;
  }

  @Override
  public String[] getMessageArgs()
  {
    return messageArgs;
  }

  public void setI18NKey(String key)
  {
    i18NKey = key;
  }

  public void setMessageArgs(String[] messageArgs)
  {
    this.messageArgs = messageArgs;
  }

  public void setFormName(String formName)
  {
    this.formName = formName;
  }
}
