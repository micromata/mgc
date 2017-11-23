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

import org.apache.commons.lang3.StringUtils;

/**
 * Base class on ValMessage related exceptions.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class ValStatusException extends RuntimeException
{

  /**
   * Instantiates a new val status exception.
   *
   * @param message the message
   */
  public ValStatusException(String message)
  {
    super(message);
  }

  public ValState getValState()
  {
    return getWorstMessage().getValState();
  }

  public abstract ValMessage getWorstMessage();

  @Override
  public String getMessage()
  {
    StringBuilder sb = new StringBuilder();
    String smess = super.getMessage();
    if (StringUtils.isNotBlank(smess) == true) {
      sb.append(smess).append(": ");
    }
    ValMessage msg = getWorstMessage();
    if (msg.getMessage() != null) {
      sb.append(msg.getMessage());
    } else {
      sb.append(msg.getI18nkey());
    }
    if (msg.getReference() != null) {
      sb.append(" on ").append(msg.getReference().getClass().getSimpleName());
    }
    if (StringUtils.isNotBlank(msg.getProperty()) == true) {
      sb.append(".").append(msg.getReference());
    }
    return sb.toString();
  }
}
