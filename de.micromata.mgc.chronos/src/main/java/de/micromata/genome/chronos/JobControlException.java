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
import de.micromata.genome.logging.LogAttributeRuntimeException;

/**
 * Base class for Exception thrown by jobs.
 *
 * @author roger
 */
public class JobControlException extends LogAttributeRuntimeException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5339065645182169281L;

  /**
   * Wenn silent == true, wird vom jchronos die exception nicht mehr geloggt.
   */
  private boolean silent = false;

  /**
   * Instantiates a new job control exception.
   */
  public JobControlException()
  {
    super();
  }

  /**
   * Instantiates a new job control exception.
   *
   * @param message the message
   * @param captureLogContext the capture log context
   */
  public JobControlException(String message, boolean captureLogContext)
  {
    super(message, captureLogContext);
  }

  /**
   * Instantiates a new job control exception.
   *
   * @param message the message
   * @param attrs the attrs
   */
  public JobControlException(String message, LogAttribute... attrs)
  {
    super(message, attrs);
  }

  /**
   * Instantiates a new job control exception.
   *
   * @param message the message
   * @param cause the cause
   * @param captureLogContext the capture log context
   * @param attrs the attrs
   */
  public JobControlException(String message, Throwable cause, boolean captureLogContext, LogAttribute... attrs)
  {
    super(message, cause, captureLogContext, attrs);
  }

  /**
   * Instantiates a new job control exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public JobControlException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new job control exception.
   *
   * @param message the message
   */
  public JobControlException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new job control exception.
   *
   * @param cause the cause
   */
  public JobControlException(Throwable cause)
  {
    super(cause);
  }

  public boolean isSilent()
  {
    return silent;
  }

  /**
   * Sets the silent.
   *
   * @param silent the silent
   * @return the job control exception
   */
  public JobControlException setSilent(boolean silent)
  {
    this.silent = silent;
    return this;
  }

}
