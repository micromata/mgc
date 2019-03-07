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

package de.micromata.genome.db.jpa.history.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import de.micromata.genome.db.jpa.history.entities.EntityOpType;
import de.micromata.genome.jpa.DbRecord;

/**
 * An change for an Entity.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface HistoryEntry<PK extends Serializable>extends DbRecord<PK>
{

  /**
   * Gets the modified at.
   *
   * @return the modified at
   */
  Date getModifiedAt();

  /**
   * Gets the modified by.
   *
   * @return the modified by
   */
  String getModifiedBy();

  /**
   * alias to getModifiedBy.
   * 
   * @return the user which modified the entry
   */
  default String getUserName()
  {
    return getModifiedBy();
  }

  /**
   * Gets the diff entries.
   *
   * @return the diff entries
   */
  public List<DiffEntry> getDiffEntries();

  /**
   * Gets the entity op type.
   *
   * @return the entity op type
   */
  EntityOpType getEntityOpType();

  /**
   * Gets the entity name.
   *
   * @return the entity name
   */
  String getEntityName();

  /**
   * Gets the entity id.
   *
   * @return the entity id
   */
  Long getEntityId();

  /**
   * Gets the user comment.
   *
   * @return the user comment
   */
  String getUserComment();

  /**
   * Gets the transaction id.
   *
   * @return the transaction id
   */
  String getTransactionId();

}
