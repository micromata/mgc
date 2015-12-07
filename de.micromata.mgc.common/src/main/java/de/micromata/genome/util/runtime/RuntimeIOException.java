/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   24.09.2008
// Copyright Micromata 24.09.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.runtime;

import java.io.IOException;

/**
 * RuntimeException wrapper for IOException.
 *
 * @author roger@micromata.de
 */
public class RuntimeIOException extends RuntimeException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 9199669223884215271L;

  /**
   * Instantiates a new runtime io exception.
   */
  public RuntimeIOException()
  {
  }

  /**
   * Instantiates a new runtime io exception.
   *
   * @param message the message
   */
  public RuntimeIOException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new runtime io exception.
   *
   * @param cause the cause
   */
  public RuntimeIOException(IOException cause)
  {
    super(cause);
  }

  /**
   * Instantiates a new runtime io exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public RuntimeIOException(String message, IOException cause)
  {
    super(message, cause);
  }

}
