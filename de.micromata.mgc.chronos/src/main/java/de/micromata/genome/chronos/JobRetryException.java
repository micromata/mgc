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
