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

package de.micromata.genome.jpa.test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import de.micromata.genome.jpa.MarkDeletableRecord;
import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Table(name = "TB_TEST_JPAT")
@SequenceGenerator(name = "SQ_TEST_JPAT_PK", sequenceName = "SQ_TEST_JPAT_PK")
public class GenomeJpaTestTableDO extends StdRecordDO<Long> implements MarkDeletableRecord<Long>
{

  private static final long serialVersionUID = 7394333962220924260L;

  private String firstName;
  private boolean deleted;

  @Override
  @Id
  @Column(name = "TEST_JPAT_PK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TEST_JPAT_PK")
  public Long getPk()
  {
    return pk;
  }

  @Column(name = "FIRSTNAME", nullable = false, length = 50)
  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String textField)
  {
    this.firstName = textField;
  }

  @Override
  public void setPk(Long pk)
  {
    this.pk = pk;
  }

  @Override
  public boolean isDeleted()
  {
    return deleted;
  }

  @Override
  public void setDeleted(boolean deleted)
  {
    this.deleted = deleted;
  }

}
