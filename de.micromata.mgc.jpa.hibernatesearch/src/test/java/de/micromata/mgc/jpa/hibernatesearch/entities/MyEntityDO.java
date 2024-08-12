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

package de.micromata.mgc.jpa.hibernatesearch.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.ComplexEntityVisitor;
import de.micromata.genome.jpa.StdRecordDO;

/**
 * To test.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Entity
// annotate for Hibernate Search.
@Indexed
public class MyEntityDO extends StdRecordDO<Long> implements ComplexEntity
{
  /**
   * Annotate Fields to index.
   */
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String name;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String loginName;

  private String notSearchable;

  @IndexedEmbedded(depth = 2, includePaths = { "nestedName", "nestedNested.nestedNestedComment" })
  private MyNestedEntity nested;

  @IndexedEmbedded(depth = 1)
  private Set<MyNestedEntity> assignedNested = new HashSet<>();

  @Override
  public void visit(ComplexEntityVisitor visitor)
  {
    visitor.visit(this);
    for (MyNestedEntity ne : assignedNested) {
      visitor.visit(ne);
    }
  }

  @Override
  @Id
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getLoginName()
  {
    return loginName;
  }

  public void setLoginName(String loginName)
  {
    this.loginName = loginName;
  }

  public String getNotSearchable()
  {
    return notSearchable;
  }

  public void setNotSearchable(String notSearchable)
  {
    this.notSearchable = notSearchable;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  public MyNestedEntity getNested()
  {
    return nested;
  }

  public void setNested(MyNestedEntity nested)
  {
    this.nested = nested;
  }

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = MyNestedEntity.class)
  public Set<MyNestedEntity> getAssignedNested()
  {
    return assignedNested;
  }

  public void setAssignedNested(Set<MyNestedEntity> assignedNested)
  {
    this.assignedNested = assignedNested;
  }

}
