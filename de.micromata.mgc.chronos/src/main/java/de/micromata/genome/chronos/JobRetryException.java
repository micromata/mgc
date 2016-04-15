/////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: JobRetryException.java,v $
//
// Project   jchronos
//
// Author    Roger Kommer, Wolfgang Jung (w.jung@micromata.de)
// Created   03.01.2007
// Copyright Micromata 03.01.2007
//
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

import de.micromata.genome.logging.LogAttribute;

/**
 * Throwing from a job to retry later.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JobRetryException extends JobControlException
{

  /**
   * serialVersionUID.
   */
  private static final long serialVersionUID = -2814203354464893048L;

  /**
   * Instantiates a new job retry exception.
   *
   * @param message the message
   */
  public JobRetryException(final String message)
  {
    super(message);
  }

  /**
   * Instantiates a new job retry exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public JobRetryException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new job retry exception.
   */
  public JobRetryException()
  {
    super();
  }

  /**
   * Instantiates a new job retry exception.
   *
   * @param message the message
   * @param captureLogContext the capture log context
   */
  public JobRetryException(String message, boolean captureLogContext)
  {
    super(message, captureLogContext);

  }

  /**
   * Instantiates a new job retry exception.
   *
   * @param message the message
   * @param attrs the attrs
   */
  public JobRetryException(String message, LogAttribute... attrs)
  {
    super(message, attrs);

  }

  /**
   * Instantiates a new job retry exception.
   *
   * @param message the message
   * @param cause the cause
   * @param captureLogContext the capture log context
   * @param attrs the attrs
   */
  public JobRetryException(String message, Throwable cause, boolean captureLogContext, LogAttribute... attrs)
  {
    super(message, cause, captureLogContext, attrs);

  }

  /**
   * Instantiates a new job retry exception.
   *
   * @param cause the cause
   */
  public JobRetryException(Throwable cause)
  {
    super(cause);

  }

}
