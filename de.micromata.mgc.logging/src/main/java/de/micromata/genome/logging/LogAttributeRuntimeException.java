/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   29.03.2008
// Copyright Micromata 29.03.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Runtime Exception which can transport LogAttributes to catcher of this exception.
 *
 * @author roger@micromata.de
 */
public class LogAttributeRuntimeException extends RuntimeException implements WithLogAttributes
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 3169674194091876222L;

  /**
   * The log attribute map.
   */
  private Map<String, LogAttribute> logAttributeMap = new HashMap<String, LogAttribute>();

  /**
   * Instantiates a new log attribute runtime exception.
   */
  public LogAttributeRuntimeException()
  {

  }

  /**
   * Instantiates a new log attribute runtime exception.
   *
   * @param message the message
   */
  public LogAttributeRuntimeException(String message)
  {
    this(message, null, true);
  }

  /**
   * Instantiates a new log attribute runtime exception.
   *
   * @param message the message
   * @param captureLogContext the capture log context
   */
  public LogAttributeRuntimeException(String message, boolean captureLogContext)
  {
    this(message, null, captureLogContext);
  }

  /**
   * Instantiates a new log attribute runtime exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public LogAttributeRuntimeException(String message, Throwable cause)
  {
    this(message, cause, true);
  }

  /**
   * Instantiates a new log attribute runtime exception.
   *
   * @param message the message
   * @param attrs the attrs
   */
  public LogAttributeRuntimeException(String message, LogAttribute... attrs)
  {
    this(message, null, true, attrs);
  }

  /**
   * Instantiates a new log attribute runtime exception.
   *
   * @param cause the cause
   */
  public LogAttributeRuntimeException(Throwable cause)
  {
    this(null, cause, true);
  }

  /**
   * Instantiates a new log attribute runtime exception.
   *
   * @param message the message
   * @param cause the cause
   * @param captureLogContext the capture log context
   * @param attrs the attrs
   */
  public LogAttributeRuntimeException(String message, Throwable cause, boolean captureLogContext, LogAttribute... attrs)
  {
    super(message, cause);
    for (LogAttribute la : attrs) {
      if (logAttributeMap.containsKey(la.getType().name()) == false) {
        logAttributeMap.put(la.getType().name(), la);
      }
    }
    if (cause instanceof WithLogAttributes) {
      Collection<LogAttribute> wattrs = ((WithLogAttributes) cause).getLogAttributes();
      if (wattrs != null) {
        for (LogAttribute la : wattrs) {
          if (logAttributeMap.containsKey(la.getType().name()) == false) {
            logAttributeMap.put(la.getType().name(), la);
          }
        }
      }
    }
    if (captureLogContext == true) {
      for (Map.Entry<LogAttributeType, LogAttribute> me : LoggingContext.getEnsureContext().getAttributes().entrySet()) {
        String k = me.getKey().name();
        if (logAttributeMap.containsKey(k) == false) {
          logAttributeMap.put(k, me.getValue());
        }
      }
    }
  }

  @Override
  public Collection<LogAttribute> getLogAttributes()
  {
    return logAttributeMap.values();
  }

  public Map<String, LogAttribute> getLogAttributeMap()
  {
    return logAttributeMap;
  }

  public void setLogAttributeMap(Map<String, LogAttribute> logAttributeMap)
  {
    this.logAttributeMap = logAttributeMap;
  }

}
