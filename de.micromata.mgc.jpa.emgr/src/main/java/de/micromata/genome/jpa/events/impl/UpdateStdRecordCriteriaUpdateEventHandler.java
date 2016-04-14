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

package de.micromata.genome.jpa.events.impl;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.StdRecord;
import de.micromata.genome.jpa.events.EmgrCriteriaUpdateEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;

/**
 * Check CriteriaUpdates for StdRecord and set modifiedAt/By.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class UpdateStdRecordCriteriaUpdateEventHandler implements EmgrEventHandler<EmgrCriteriaUpdateEvent>
{

  @Override
  public void onEvent(EmgrCriteriaUpdateEvent event)
  {
    CriteriaUpdate<?> update = event.getCriteriaUpdate();
    Class<?> clazz = update.getEntityClass();
    if (StdRecord.class.isAssignableFrom(clazz) == false) {
      return;
    }
    update.setExpression("updateCounter", update.getMasterAlias() + ".updateCounter + 1");
    update.set("modifiedAt", event.getEmgr().getEmgrFactory().getNow());
    update.set("modifiedBy", event.getEmgr().getEmgrFactory().getCurrentUserId());
  }

}
