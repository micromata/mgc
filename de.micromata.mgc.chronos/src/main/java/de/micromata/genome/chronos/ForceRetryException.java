//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
