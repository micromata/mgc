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

import java.util.Date;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.MarkDeletableRecord;
import de.micromata.genome.jpa.StdRecord;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrMarkDeletedCriteriaUpdateFilterEvent;

/**
 * If an StdRecord was marked deleted, update modifiedAt/By.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrMarkDeletedCriteriaUpdateFilterEventHandler
    implements EmgrEventHandler<EmgrMarkDeletedCriteriaUpdateFilterEvent<?>>
{

  /**
   * Sets the modified.
   *
   * @param emgr the emgr
   * @param update the update
   * @param rec the rec
   */
  public static void setModified(IEmgr<?> emgr, CriteriaUpdate<?> update, StdRecord<?> rec)
  {
    String userid = emgr.getEmgrFactory().getCurrentUserId();
    Date now = emgr.getEmgrFactory().getNow();
    rec.setModifiedBy(userid);
    rec.setModifiedAt(now);
    update.set("modifiedBy", userid)
        .set("modifiedAt", now);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void onEvent(EmgrMarkDeletedCriteriaUpdateFilterEvent<?> event)
  {
    MarkDeletableRecord<?> ent = event.getEntity();
    if (ent instanceof StdRecord) {
      setModified(event.getEmgr(), event.getUpdate(), (StdRecord) ent);
    }
    event.nextFilter();
  }

}
