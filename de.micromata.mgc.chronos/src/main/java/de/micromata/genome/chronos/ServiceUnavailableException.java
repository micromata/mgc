/////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: ServiceUnavailableException.java,v $
//
// Project   jchronos
//
// Author    Wolfgang Jung (w.jung@micromata.de)
// Created   03.01.2007
// Copyright Micromata 03.01.2007
//
// $Id: ServiceUnavailableException.java,v 1.3 2007-12-30 11:59:44 roger Exp $
// $Revision: 1.3 $
// $Date: 2007-12-30 11:59:44 $
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

import de.micromata.genome.logging.LogAttribute;

/**
 * Thrown by job if a service backend interface (like remote services) are not available.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ServiceUnavailableException extends JobRetryException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6010861227600774448L;

  /**
   * Instantiates a new service unavailable exception.
   */
  public ServiceUnavailableException()
  {
    super();
  }

  /**
   * Instantiates a new service unavailable exception.
   *
   * @param message the message
   */
  public ServiceUnavailableException(final String message)
  {
    super(message);
  }

  /**
   * Instantiates a new service unavailable exception.
   *
   * @param message the message
   * @param silent the silent
   */
  public ServiceUnavailableException(final String message, boolean silent)
  {
    super(message, silent);
  }

  /**
   * Instantiates a new service unavailable exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public ServiceUnavailableException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new service unavailable exception.
   *
   * @param message the message
   * @param cause the cause
   * @param silent the silent
   */
  public ServiceUnavailableException(final String message, final Throwable cause, boolean silent)
  {
    super(message, cause, silent);
  }

  /**
   * Instantiates a new service unavailable exception.
   *
   * @param message the message
   * @param attrs the attrs
   */
  public ServiceUnavailableException(String message, LogAttribute... attrs)
  {
    super(message, attrs);
  }

  /**
   * Instantiates a new service unavailable exception.
   *
   * @param message the message
   * @param cause the cause
   * @param captureLogContext the capture log context
   * @param attrs the attrs
   */
  public ServiceUnavailableException(String message, Throwable cause, boolean captureLogContext, LogAttribute... attrs)
  {
    super(message, cause, captureLogContext, attrs);
  }

  /**
   * Instantiates a new service unavailable exception.
   *
   * @param cause the cause
   */
  public ServiceUnavailableException(Throwable cause)
  {
    super(cause);
  }

}
