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

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.ComplexEntityVisitor;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.StdRecord;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrInitForUpdateEvent;

/**
 * Checkes Complex Entites and update StdRecord fields before update.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class InitUpdateStdRecordFieldsEventHandler implements EmgrEventHandler<EmgrInitForUpdateEvent>
{

  @Override
  public void onEvent(EmgrInitForUpdateEvent event)
  {
    updateRecordRecursive(event.getEmgr(), event.getRecord(), false);

  }

  /**
   * Update record recursive.
   *
   * @param emgr the emgr
   * @param rec the rec
   * @param isNew the is new
   */
  public static void updateRecordRecursive(final IEmgr<?> emgr, DbRecord rec, final boolean isNew)
  {
    final Date now = emgr.getEmgrFactory().getNow();
    if (rec instanceof ComplexEntity) {
      ComplexEntity ce = (ComplexEntity) rec;
      ce.visit(new ComplexEntityVisitor()
      {

        @Override
        public void visit(DbRecord rec)
        {
          if (rec instanceof StdRecord == false) {
            return;
          }
          StdRecord stdRec = (StdRecord) rec;
          InitUpdateStdRecordFieldsEventHandler.updateRecord(emgr, stdRec, isNew, now);
        }
      });
    } else {
      if (rec instanceof StdRecord == false) {
        return;
      }
      StdRecord stdRec = (StdRecord) rec;
      updateRecord(emgr, stdRec, isNew, now);
    }
  }

  /**
   * Update record.
   *
   * @param emgr the emgr
   * @param stdRec the std rec
   * @param isNew the is new
   * @param now the now
   */
  public static void updateRecord(IEmgr<?> emgr, StdRecord stdRec, boolean isNew, Date now)
  {
    if (emgr.getEmgrFactory().isHasUpdateTriggerForVersion() == false || isNew == true) {
      stdRec.setModifiedAt(now);
    }
    String user = emgr.getEmgrFactory().getCurrentUserId();
    stdRec.setModifiedBy(user);
    if (stdRec.getCreatedAt() == null && emgr.getEmgrFactory().isHasInsertTriggerForVersion() == false) {
      stdRec.setCreatedAt(now);
    }
    if (StringUtils.isEmpty(stdRec.getCreatedBy()) == true) {
      stdRec.setCreatedBy(user);
    }
  }
}
