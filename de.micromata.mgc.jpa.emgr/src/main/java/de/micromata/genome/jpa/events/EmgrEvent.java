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

package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.IEmgr;

/**
 * Base classes to all Events invoked by Emgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrEvent
{

  /**
   * The emgr.
   */
  private final IEmgr<?> emgr;

  /**
   * Instantiates a new emgr event.
   *
   * @param emgr the emgr
   */
  public EmgrEvent(IEmgr<?> emgr)
  {
    this.emgr = emgr;
  }

  public IEmgr<?> getEmgr()
  {
    return emgr;
  }

  @Override
  public String toString()
  {
    return super.toString();// + ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
  }
}
