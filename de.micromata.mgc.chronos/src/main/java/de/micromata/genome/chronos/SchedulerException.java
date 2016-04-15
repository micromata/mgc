/////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: SchedulerException.java,v $
//
// Project   chronos
//
// Author    Wolfgang Jung (w.jung@micromata.de)
// Created   26.12.2006
// Copyright Micromata 26.12.2006
//
// $Id: SchedulerException.java,v 1.1 2007/02/09 09:57:15 roger Exp $
// $Revision: 1.1 $
// $Date: 2007/02/09 09:57:15 $
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

/**
 * Something went wrong with the scheduler.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SchedulerException extends RuntimeException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6882560391682476652L;

  /**
   * Instantiates a new scheduler exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public SchedulerException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new scheduler exception.
   *
   * @param message the message
   */
  public SchedulerException(final String message)
  {
    super(message);
  }

  /**
   * Instantiates a new scheduler exception.
   *
   * @param cause the cause
   */
  public SchedulerException(final Throwable cause)
  {
    super(cause);
  }
}
