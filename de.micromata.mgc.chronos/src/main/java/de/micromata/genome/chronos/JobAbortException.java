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
