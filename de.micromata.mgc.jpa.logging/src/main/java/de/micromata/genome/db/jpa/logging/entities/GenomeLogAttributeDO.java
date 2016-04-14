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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

/**
 * A logging Attribute entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Entity
@Table(name = "TB_TA_LOG_ATTRIBUTE")
@org.hibernate.annotations.Table(indexes = { //
    @Index(name = "IX_TA_LOGATTR_SVALUE", columnNames = { "BASE_LOG_ATTRIBUTE", "SHORT_VALUE" }), //
    @Index(name = "IX_TA_LOG_ATTRIBUTE_MODAT", columnNames = { "MODIFIEDAT" })
    //
}, appliesTo = "TB_TA_LOG_ATTRIBUTE")
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
