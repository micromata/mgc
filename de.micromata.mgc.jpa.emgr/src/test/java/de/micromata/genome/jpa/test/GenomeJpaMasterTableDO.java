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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.ComplexEntityVisitor;
import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Table(name = "TB_TEST_MASTER")
@SequenceGenerator(name = "SQ_TEST_MASTER_PK", sequenceName = "SQ_TEST_MASTER_PK")
public class GenomeJpaMasterTableDO extends StdRecordDO<Long> implements ComplexEntity
{

  private static final long serialVersionUID = 285376321479286358L;

  private Long pk;

  private String firstName;

  private List<GenomeJpaDetailTableDO> data = new ArrayList<GenomeJpaDetailTableDO>();

  @Override
  public void visit(ComplexEntityVisitor visitor)
  {
    visitor.visit(this);
    if (data != null) {
      for (GenomeJpaDetailTableDO d : data) {
        visitor.visit(d);
      }
    }

  }

  public GenomeJpaDetailTableDO createAddNewDetail()
  {
    GenomeJpaDetailTableDO ret = new GenomeJpaDetailTableDO();
    ret.setParent(this);
    data.add(ret);
    return ret;
  }

  @Override
  @Id
  @Column(name = "TEST_MASTER_PK")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TEST_MASTER_PK")
  public Long getPk()
  {
    return pk;
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = GenomeJpaDetailTableDO.class,
      orphanRemoval = true, fetch = FetchType.EAGER)
  public List<GenomeJpaDetailTableDO> getData()
  {
    return data;
  }

  public void setData(List<GenomeJpaDetailTableDO> data)
  {
    this.data = data;
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
