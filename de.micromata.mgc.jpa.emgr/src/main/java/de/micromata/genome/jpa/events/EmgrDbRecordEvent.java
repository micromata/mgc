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

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;

/**
 * Base Event type with an DbRecord as argument.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrDbRecordEvent extends EmgrEvent
{

  /**
   * The record.
   */
  protected DbRecord<?> record;

  public DbRecord<?> getRecord()
  {
    return record;
  }

  public void setRecord(DbRecord<?> record)
  {
    this.record = record;
  }

  /**
   * Instantiates a new emgr db record event.
   *
   * @param emgr the emgr
   * @param record the record
   */
  public EmgrDbRecordEvent(IEmgr<?> emgr, DbRecord<?> record)
  {
    super(emgr);
    this.record = record;
  }

}
