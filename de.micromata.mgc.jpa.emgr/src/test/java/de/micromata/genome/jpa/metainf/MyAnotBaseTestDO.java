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

package de.micromata.genome.jpa.metainf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.micromata.genome.jpa.DbRecordDO;

@MappedSuperclass
public abstract class MyAnotBaseTestDO extends DbRecordDO<Long>
{
  private Long synPk1;
  private long synpk2;

  private Date myDate;

  public Long getSynPk1()
  {
    return synPk1;
  }

  public void setSynPk1(Long synPk1)
  {
    this.synPk1 = synPk1;
  }

  public long getSynpk2()
  {
    return synpk2;
  }

  public void setSynpk2(long synpk2)
  {
    this.synpk2 = synpk2;
  }

  @Column(name = "day")
  @Temporal(TemporalType.TIMESTAMP)
  public Date getMyDate()
  {
    return myDate;
  }

  public void setMyDate(Date myDate)
  {
    this.myDate = myDate;
  }

}
