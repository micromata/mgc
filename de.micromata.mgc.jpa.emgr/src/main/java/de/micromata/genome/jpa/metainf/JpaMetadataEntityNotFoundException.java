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

package de.micromata.genome.jpa.metainf;

/**
 * Entity was not found in the Metadata Repo.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaMetadataEntityNotFoundException extends JpaMetadataNotFoundException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 7503202640180469114L;

  /**
   * Instantiates a new jpa metadata entity not found exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public JpaMetadataEntityNotFoundException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new jpa metadata entity not found exception.
   *
   * @param message the message
   */
  public JpaMetadataEntityNotFoundException(String message)
  {
    super(message);
  }

  /**
   * Instantiates a new jpa metadata entity not found exception.
   *
   * @param cause the cause
   */
  public JpaMetadataEntityNotFoundException(Throwable cause)
  {
    super(cause);
  }

}
