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

package de.micromata.genome.db.jpa.history.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import de.micromata.genome.db.jpa.history.api.DiffEntry;
import de.micromata.genome.db.jpa.history.api.HistoryEntry;
import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabMasterBaseDO;

/**
 * The Class HistoryMasterBaseDO.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
@MappedSuperclass
public abstract class HistoryMasterBaseDO<T extends HistoryMasterBaseDO<?, ?>, PK extends Serializable>
    extends JpaTabMasterBaseDO<T, PK> implements HistoryEntry<PK>
{

  /**
   * The Constant serialVersionUID.
   */

  private static final long serialVersionUID = 1687712634400467851L;

  /**
   * The entity op type.
   */
  private EntityOpType entityOpType;

  /**
   * Table/Entity name changing.
   */
  private String entityName;
  /**
   * PK of Record changing.
   */
  private Long entityId;

  /**
   * The user comment.
   */
  private String userComment;

  /**
   * Optional transactionId grouping varios modifications.
   */
  private String transactionId;

  /**
   * Gets the entity op type.
   *
   * @return the entity op type
   */
  @Override
  @Enumerated(EnumType.STRING)
  @Column(name = "ENTITY_OPTYPE", length = 32)
  public EntityOpType getEntityOpType()
  {
    return entityOpType;
  }

  /**
   * Sets the entity op type.
   *
   * @param entityOpType the new entity op type
   */
  public void setEntityOpType(EntityOpType entityOpType)
  {
    this.entityOpType = entityOpType;
  }

  /**
   * Gets the entity name.
   *
   * @return the entity name
   */
  @Override
  @Column(name = "ENTITY_NAME", length = 255, nullable = false)
  public String getEntityName()
  {
    return entityName;
  }

  /**
   * Sets the entity name.
   *
   * @param entityName the new entity name
   */
  public void setEntityName(String entityName)
  {
    this.entityName = entityName;
  }

  /**
   * Gets the entity id.
   *
   * @return the entity id
   */
  @Override
  @Column(name = "ENTITY_ID", nullable = false)
  public Long getEntityId()
  {
    return entityId;
  }

  /**
   * Sets the entity id.
   *
   * @param entityId the new entity id
   */
  public void setEntityId(Long entityId)
  {
    this.entityId = entityId;
  }

  /**
   * Gets the user comment.
   *
   * @return the user comment
   */
  @Override
  @Column(name = "USER_COMMENT", length = 2000)
  public String getUserComment()
  {
    return userComment;
  }

  /**
   * Sets the user comment.
   *
   * @param userComment the new user comment
   */
  public void setUserComment(String userComment)
  {
    this.userComment = userComment;
  }

  /**
   * Gets the transaction id.
   *
   * @return the transaction id
   */
  @Override
  @Column(name = "TRANSACTION_ID", length = 64)
  public String getTransactionId()
  {
    return transactionId;
  }

  /**
   * Sets the transaction id.
   *
   * @param transactionId the new transaction id
   */
  public void setTransactionId(String transactionId)
  {
    this.transactionId = transactionId;
  }

  /**
   * Gets the diff entries.
   *
   * @return the diff entries
   */
  @Override
  @Transient
  public List<DiffEntry> getDiffEntries()
  {
    return HistoryServiceManager.get().getHistoryService().getDiffEntriesForHistoryMaster(this);
  }

}
