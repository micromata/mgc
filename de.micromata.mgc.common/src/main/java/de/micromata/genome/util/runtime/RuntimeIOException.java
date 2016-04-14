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
