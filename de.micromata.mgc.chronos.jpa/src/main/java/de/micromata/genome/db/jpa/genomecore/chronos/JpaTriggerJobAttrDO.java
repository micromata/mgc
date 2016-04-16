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

package de.micromata.genome.db.jpa.genomecore.chronos;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;

/**
 * JPA entity for TB_TA_AMOUNT_ATTR
 *
 * @author lado
 */
@Entity
@Table(name = "TB_TA_CHRONOS_JOB_ATTR")
@SequenceGenerator(name = "SQ_TA_CHRONOS_JOB_ATTR_PK", sequenceName = "SQ_TA_CHRONOS_JOB_ATTR_PK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Table(//
    indexes = {//
    @Index(name = "IX_TA_CHRONOS_JOB_ATTR_MODAT", columnNames = { "MODIFIEDAT" }),//
        @Index(name = "IX_TA_CHRONOS_JOB_ATTR_MST_FK", columnNames = { "MASTER_FK" }),//
    }, appliesTo = "TB_TA_CHRONOS_JOB_ATTR")
@DiscriminatorColumn(name = "WITHDATA", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("0")
public class JpaTriggerJobAttrDO extends JpaTabAttrBaseDO<JpaTriggerJobDO, Long>
{

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -1018859767160883411L;

  /**
   * Instantiates a new Shipment attr dO.
   */
  public JpaTriggerJobAttrDO()
  {
    // Empty on purpose.
  }

  /**
   * Instantiates a new Shipment attr dO.
   *
   * @param parent the parent
   */
  public JpaTriggerJobAttrDO(JpaTriggerJobDO parent)
  {
    super(parent);
  }

  /**
   * Instantiates a new Shipment attr dO.
   *
   * @param parent the parent
   * @param propertyName the property name
   * @param type the type
   * @param value the value
   */
  public JpaTriggerJobAttrDO(JpaTriggerJobDO parent, String propertyName, char type, String value)
  {
    super(parent, propertyName, type, value);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public JpaTabAttrDataBaseDO<?, Long> createData(String data)
  {
	 return new JpaTriggerJobAttrDataDO(this, data);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  @Transient
  public int getMaxDataLength()
  {
    return JpaTabAttrDataBaseDO.DATA_MAXLENGTH;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  @Id
  @Column(name = "TA_CHRONOS_JOB_ATTR")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TA_CHRONOS_JOB_ATTR_PK")
  public Long getPk()
  {
    return pk;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  @ManyToOne(optional = false)
  @JoinColumn(name = "MASTER_FK", referencedColumnName = "TA_CHRONOS_JOB")
  public JpaTriggerJobDO getParent()
  {
    return super.getParent();
  }
}
