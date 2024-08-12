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

package de.micromata.genome.jpa;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

import de.micromata.genome.util.types.Converter;

/**
 * Standard implementation of the a record with version information.
 * 
 * @author roger@micromata.de
 * 
 */
@MappedSuperclass
public abstract class StdRecordDO<PK extends Serializable>extends DbRecordDO<PK> implements StdRecord<PK>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 3891095023083295901L;

  /**
   * Version info. User created this entity.
   */
  protected String createdBy;

  /**
   * Version info. User modified this entity last time.
   */
  protected String modifiedBy;

  /**
   * Version info. Last timestamp this entity was modified.
   */
  protected Date modifiedAt;

  /**
   * Version info. timestamp this entity was created.
   */
  public final static String CREATE_AT_PROP = "createdAt";  
  protected Date createdAt;

  /**
   * Version info. Number of updates on this entity..
   */
  protected Integer updateCounter = 0;

  @Override
  public void setModifiedBy(final String modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  }

  @Transient
  public String getModifiedAtString()
  {
    if (this.modifiedAt == null) {
      return "";
    }
    return Converter.formatByIsoTimestampFormat(this.modifiedAt);
  }

  @Override
  public void setModifiedAt(final Date modifiedAt)
  {
    this.modifiedAt = modifiedAt;
  }

  @Transient
  public String getCreatedAtString()
  {
    if (this.createdAt == null) {
      return "";
    }
    return Converter.formatByIsoTimestampFormat(this.createdAt);
  }

  @Override
  public void setCreatedAt(final Date createdAt)
  {
    this.createdAt = createdAt;
  }

  @Override
  public void setUpdateCounter(final Integer updateCounter)
  {
    this.updateCounter = updateCounter;
  }

  @Override
  public void setCreatedBy(final String createdBy)
  {
    this.createdBy = createdBy;
  }

  // REMOVE ME

  // @Column(name = "MODIFIEDBY", columnDefinition = "VARCHAR DEFAULT USER", nullable = false)
  @Override
  @Column(name = "MODIFIEDBY", nullable = false, length = 60)
  public String getModifiedBy()
  {
    return this.modifiedBy;
  }

  @Override
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "MODIFIEDAT", nullable = false, columnDefinition = "TIMESTAMP")
  public Date getModifiedAt()
  {
    return this.modifiedAt;
  }

  @Override
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATEDAT", nullable = false, columnDefinition = "TIMESTAMP")
  public Date getCreatedAt()
  {
    return this.createdAt;
  }

  @Override
  @Column(name = "UPDATECOUNTER", nullable = false)
  @Version
  public Integer getUpdateCounter()
  {
    return this.updateCounter;
  }

  @Override
  @Column(name = "CREATEDBY", nullable = false, length = 60)
  public String getCreatedBy()
  {
    return this.createdBy;
  }

}
