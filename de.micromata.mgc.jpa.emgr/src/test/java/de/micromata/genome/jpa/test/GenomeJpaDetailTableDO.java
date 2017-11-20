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

import de.micromata.genome.jpa.StdRecordDO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_TEST_DETAIL", indexes = {//
    @Index(name = "IX_TEST_DETAIL_MODAT", columnList = "MODIFIEDAT"),
    @Index(name = "IX_TEST_DETAIL_PARENT", columnList = "PARENT_PK")
})
@SequenceGenerator(name = "SQ_TEST_DETAIL_PK", sequenceName = "SQ_TEST_DETAIL_PK")
public class GenomeJpaDetailTableDO extends StdRecordDO
{

  private static final long serialVersionUID = 285376321479286358L;

  private Long pk;

  private GenomeJpaMasterTableDO parent;

  private String location;

  @Override
  @Id
  @Column(name = "TEST_DETAIL_PK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TEST_DETAIL_PK")
  public Long getPk()
  {
    return pk;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "PARENT_PK")
  public GenomeJpaMasterTableDO getParent()
  {
    return parent;
  }

  @Column(name = "LOCATION", nullable = false, length = 50)
  public String getLocation()
  {
    return location;
  }

  public void setLocation(String textField)
  {
    this.location = textField;
  }

  public void setPk(Long pk)
  {
    this.pk = pk;
  }

  public void setParent(GenomeJpaMasterTableDO parent)
  {
    this.parent = parent;
  }
}
