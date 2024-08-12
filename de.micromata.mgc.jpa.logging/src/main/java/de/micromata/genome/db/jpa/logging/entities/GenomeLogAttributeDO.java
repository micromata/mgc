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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;



/**
 * A logging Attribute entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Entity
@Table(name = "TB_TA_LOG_ATTRIBUTE", indexes = {
    @Index(name = "IX_TA_LOG_MASTER", columnList = "TA_LOG_MASTER"),
    @Index(name = "IX_TA_LOGATTR_SVALUE", columnList = "BASE_LOG_ATTRIBUTE,SHORT_VALUE"),
    @Index(name = "IX_TA_LOG_ATTRIBUTE_MODAT", columnList = "MODIFIEDAT")
})
@SequenceGenerator(name = "SQ_TA_LOG_ATTRIBUTE", sequenceName = "SQ_TA_LOG_ATTRIBUTE")
public class GenomeLogAttributeDO extends BaseLogAttributeDO<GenomeLogMasterDO>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -688188919513712393L;

  /**
   * {@inheritDoc}
   *
   */

  @Override
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TA_LOG_ATTRIBUTE")
  @Column(name = "TA_LOG_ATTRIBUTE")
  public Long getPk()
  {
    return pk;
  }

  /**
   * Gets the log master.
   *
   * @return the log master
   */

  @Override
  @ManyToOne
  @JoinColumn(name = "TA_LOG_MASTER", referencedColumnName = "TA_LOG_MASTER")
  public GenomeLogMasterDO getLogMaster()
  {
    return super.getLogMaster();
  }

}
