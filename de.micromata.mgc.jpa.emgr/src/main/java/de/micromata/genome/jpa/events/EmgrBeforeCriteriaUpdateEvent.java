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

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;

/**
 * Event will be invoked before an criteri update will be executed.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrBeforeCriteriaUpdateEvent extends EmgrCriteriaUpdateEvent
{

  /**
   * Instantiates a new emgr before criteria update event.
   *
   * @param emgr the emgr
   * @param criteriaUpdate the criteria update
   */
  public EmgrBeforeCriteriaUpdateEvent(IEmgr<?> emgr, CriteriaUpdate<?> criteriaUpdate)
  {
    super(emgr, criteriaUpdate);
  }

}
