// ///////////////////////////////////////////////////////////////////////////
//
// $RCSfile: State.java,v $
//
// Project jchronos
//
// Author Wolfgang Jung (w.jung@micromata.de)
// Created 17.01.2007
// Copyright Micromata 17.01.2007
//
// $Id: State.java,v 1.3 2007/03/11 18:05:26 roger Exp $
// $Revision: 1.3 $
// $Date: 2007/03/11 18:05:26 $
//
// ///////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos;

/**
 * The Enum State.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public enum State
{

  /**
   * Job is waiting for next execution.
   */
  WAIT,

  /**
   * Job is prepared to run.
   */
  SCHEDULED,

  /**
   * Job is running.
   */
  RUN,

  /**
   * Job is stopped.
   */
  STOP,

  /**
   * Job is finished.
   */
  FINISHED,

  /**
   * Job is in Retry.
   */
  RETRY, /**
          * Job Wurde geschlossen.
          */
  CLOSED;

  /**
   * From string.
   *
   * @param s the s
   * @return the state
   */
  public static State fromString(String s)
  {
    if (s == null) {
      return null;
    }
    for (State st : values()) {
      if (st.name().equals(s) == true) {
        return st;
      }
    }
    return null;
  }
}