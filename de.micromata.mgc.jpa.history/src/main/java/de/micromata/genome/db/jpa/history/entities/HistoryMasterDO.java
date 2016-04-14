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
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrBaseDO;
import de.micromata.genome.db.jpa.tabattr.entities.JpaTabAttrDataBaseDO;

/**
 * Stores History on changing entities.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Entity
@Table(name = "TB_BASE_GHISTORY", indexes = {
    @Index(name = "IX_BASE_GHISTORY_ENT", columnList = "ENTITY_ID,ENTITY_NAME"),
    @Index(name = "IX_BASE_GHISTORY_MOD", columnList = "MODIFIEDAT")
})
@SequenceGenerator(name = "SQ_BASE_GHISTORY_PK", sequenceName = "SQ_BASE_GHISTORY_PK")
public class HistoryMasterDO extends HistoryMasterBaseDO<HistoryMasterDO, Long>
{

  @Override
  @Id
  @Column(name = "BASE_GHISTORY")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_BASE_GHISTORY_PK")
  public Long getPk()
  {
    return pk;
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = HistoryAttrDO.class, orphanRemoval = true,
      fetch = FetchType.EAGER)
  @MapKey(name = "propertyName")
  @Override
  public Map<String, JpaTabAttrBaseDO<HistoryMasterDO, Long>> getAttributes()
  {
    return super.getAttributes();
  }

  @Override
  @Transient
  public Class<? extends JpaTabAttrBaseDO<HistoryMasterDO, ? extends Serializable>> getAttrEntityClass()
  {
    return HistoryAttrDO.class;
  }

  @Override
  @Transient
  public Class<? extends JpaTabAttrBaseDO<HistoryMasterDO, ? extends Serializable>> getAttrEntityWithDataClass()
  {
    return HistoryAttrWithDataDO.class;
  }

  @Override
  @Transient
  public Class<? extends JpaTabAttrDataBaseDO<? extends JpaTabAttrBaseDO<HistoryMasterDO, Long>, Long>> getAttrDataEntityClass()
  {
    return HistoryAttrDataDO.class;
  }

  @Override
  public JpaTabAttrBaseDO<HistoryMasterDO, Long> createAttrEntity(String key, char type, String value)
  {
    return new HistoryAttrDO(this, key, type, value);
  }

  @Override
  public JpaTabAttrBaseDO<HistoryMasterDO, Long> createAttrEntityWithData(String key, char type, String value)
  {
    return new HistoryAttrWithDataDO(this, key, type, value);
  }

}
