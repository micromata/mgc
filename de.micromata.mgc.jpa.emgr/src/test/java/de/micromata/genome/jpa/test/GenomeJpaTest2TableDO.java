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
import jakarta.persistence.UniqueConstraint;

import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Table(name = "TB_TEST_JPAT2", //
    uniqueConstraints = { //
        @UniqueConstraint(columnNames = { "firstName" }, name = "IX_TEST_JPAT2_FIRSTNAME") })
@SequenceGenerator(name = "SQ_TEST_JPAT2_PK", sequenceName = "SQ_TEST_JPAT2_PK")
public class GenomeJpaTest2TableDO extends StdRecordDO<Long>
{

  private static final long serialVersionUID = 7394333962220924260L;

  private Long pk;

  private String firstName;

  @Override
  @Id
  @Column(name = "TEST_JPAT2_PK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TEST_JPAT2_PK")
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

}
