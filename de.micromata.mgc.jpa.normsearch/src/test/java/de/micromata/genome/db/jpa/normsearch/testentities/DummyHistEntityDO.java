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

package de.micromata.genome.db.jpa.normsearch.testentities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.micromata.genome.db.jpa.normsearch.NormSearchProperty;
import de.micromata.genome.db.jpa.normsearch.NormSearchTable;
import de.micromata.genome.jpa.StdRecordDO;

/**
 * An Entity should write
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Entity()
@Table(name = "TST_DUMMY_NSEARCHM_ENTITY",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "longValue" },
            name = "IX_TST_DUMMY_NSEARCHM_ENTITY_LV") })
@NormSearchTable(normSearchTable = TestTableMasterSearchDO.class)
public class DummyHistEntityDO extends StdRecordDO<Long>
{
  @NormSearchProperty
  private String stringValue;

  @NormSearchProperty
  private String comment;
  private Date dateValue;

  private int intValue;
  private long longValue;
  private boolean booleanValue;

  @Id
  @Column(name = "PK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Override
  public Long getPk()
  {
    return pk;
  }

  public String getStringValue()
  {
    return stringValue;
  }

  public void setStringValue(String stringValue)
  {
    this.stringValue = stringValue;
  }

  public Date getDateValue()
  {
    return dateValue;
  }

  public void setDateValue(Date dateValue)
  {
    this.dateValue = dateValue;
  }

  public int getIntValue()
  {
    return intValue;
  }

  public void setIntValue(int intValue)
  {
    this.intValue = intValue;
  }

  public long getLongValue()
  {
    return longValue;
  }

  public void setLongValue(long longValue)
  {
    this.longValue = longValue;
  }

  public boolean isBooleanValue()
  {
    return booleanValue;
  }

  public void setBooleanValue(boolean booleanValue)
  {
    this.booleanValue = booleanValue;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

}
