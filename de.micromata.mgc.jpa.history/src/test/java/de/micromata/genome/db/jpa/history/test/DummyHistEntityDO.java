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

package de.micromata.genome.db.jpa.history.test;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import de.micromata.genome.db.jpa.history.api.WithHistory;
import de.micromata.genome.jpa.StdRecordDO;

/**
 * An Entity should write
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@WithHistory
@Entity()
@Table(name = "TST_DUMMY_HIST_ENTITY",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "longValue" },
            name = "IX_TST_DUMMY_HIST_ENTITY_LV") })
public class DummyHistEntityDO extends StdRecordDO<Long>
{

  private String stringValue;
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
}
