/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   14.03.2007
// Copyright Micromata 14.03.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

import de.micromata.genome.logging.LogAttribute;

/**
 * Ein Retry wird erzwungen.
 * 
 * @author roger@micromata.de
 * 
 */
public class ForceRetryException extends JobRetryException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1388936786323502747L;

  /**
   * Instantiates a new force retry exception.
   */
  public ForceRetryException()
  {
    super();
  }

  /**
   * Instantiates a new force retry exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public ForceRetryException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new force retry exception.
   *
   * @param message the message
   * @param cause the cause
   * @param silent the silent
   */
  public ForceRetryException(String message, Throwable cause, boolean silent)
  {
    super(message, cause, silent);
  }

  /**
   * Instantiates a new force retry exception.
   *
   * @param silent the silent
   */
  public ForceRetryException(boolean silent)
  {
    super("", silent);
  }

  /**
   * Instantiates a new force retry exception.
   *
   * @param message the message
   * @param silent the silent
   */
  public ForceRetryException(String message, boolean silent)
  {
    super(message, silent);
  }

  /**
   * Instantiates a new force retry exception.
   *
   * @param message the message
   * @param attrs the attrs
   */
  public ForceRetryException(String message, LogAttribute... attrs)
  {
    super(message, attrs);
  }

  /**
   * Instantiates a new force retry exception.
   *
   * @param message the message
   * @param cause the cause
   * @param captureLogContext the capture log context
   * @param attrs the attrs
   */
  public ForceRetryException(String message, Throwable cause, boolean captureLogContext, LogAttribute... attrs)
  {
    super(message, cause, captureLogContext, attrs);
  }

  /**
   * Instantiates a new force retry exception.
   *
   * @param message the message
   */
  public ForceRetryException(String message)
  {
    super(message);

  }

  /**
   * Instantiates a new force retry exception.
   *
   * @param cause the cause
   */
  public ForceRetryException(Throwable cause)
  {
    super(cause);

  }

}
