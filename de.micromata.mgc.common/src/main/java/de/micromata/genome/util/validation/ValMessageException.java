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

package de.micromata.genome.util.validation;

/**
 * Contains a validation message.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class ValMessageException extends ValStatusException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -8132019164228653852L;

  /**
   * The val message.
   */
  private ValMessage valMessage;

  /**
   * Instantiates a new val message exception.
   *
   * @param message the message
   * @param valMessage the val message
   */
  public ValMessageException(String message, ValMessage valMessage)
  {
    super(message);
    this.valMessage = valMessage;

  }

  @Override
  public ValMessage getWorstMessage()
  {
    return valMessage;
  }

}
