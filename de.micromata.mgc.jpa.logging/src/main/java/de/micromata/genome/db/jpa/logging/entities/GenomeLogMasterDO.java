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

package de.micromata.genome.db.jpa.logging.entities;


import java.util.Collection;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;



/**
 * The logging master entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Entity
@Table(name = "TB_TA_LOG_MASTER", indexes = {
    @Index(name = "IX_TA_LOG_MASTER_CAT_PK", columnList = "CATEGORY,TA_LOG_MASTER"),
    @Index(name = "IX_TA_LOG_MASTER_USERNAME", columnList = "USERNAME"),
    @Index(name = "IX_TA_LOG_MASTER_MODAT", columnList = "MODIFIEDAT"),
    @Index(name = "IX_TA_LOG_MASTER_CRTAT", columnList = "CREATEDAT"),
    @Index(name = "IX_TA_LOG_MASTER_SHORTMSG", columnList = "SHORTMESSAGE"),
    @Index(name = "IX_TA_LOG_MASTER_SESSID", columnList = "HTTPSESSIONID")
})
@SequenceGenerator(name = "SQ_TA_LOG_MASTER", sequenceName = "SQ_TA_LOG_MASTER")
public class GenomeLogMasterDO extends BaseLogMasterDO<GenomeLogMasterDO>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -21442208822313194L;

  /**
   * {@inheritDoc}
   *
   */

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TA_LOG_MASTER")
  @Column(name = "TA_LOG_MASTER")
  @Override
  public Long getPk()
  {
    return pk;
  }

  /**
   * Gets the attributes.
   *
   * @return the attributes
   */
  @Override
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "logMaster", targetEntity = GenomeLogAttributeDO.class,
      orphanRemoval = true, fetch = FetchType.LAZY)
  public Collection<BaseLogAttributeDO<GenomeLogMasterDO>> getAttributes()
  {
    return attributes;
  }

  @Override
  public BaseLogAttributeDO<GenomeLogMasterDO> createAddAttribute()
  {
    GenomeLogAttributeDO ret = new GenomeLogAttributeDO();
    ret.setLogMaster(this);
    getAttributes().add(ret);
    return ret;
  }
}
