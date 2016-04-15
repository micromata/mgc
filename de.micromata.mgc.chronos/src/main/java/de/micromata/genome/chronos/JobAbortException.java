/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   09.03.2007
// Copyright Micromata 09.03.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

/**
 * Exception to abort the current running job.
 * 
 * @author roger
 * 
 */
public class JobAbortException extends JobControlException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1268094324172864470L;

  /**
   * Instantiates a new job abort exception.
   */
  public JobAbortException()
  {
    super();
  }

  /**
   * Instantiates a new job abort exception.
   *
   * @param cause the cause
   */
  public JobAbortException(Throwable cause)
  {
    super(cause);
  }

  /**
   * Instantiates a new job abort exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public JobAbortException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new job abort exception.
   *
   * @param message the message
   */
  public JobAbortException(final String message)
  {
    super(message);
  }
}
