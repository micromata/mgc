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

import de.micromata.genome.jpa.StdRecordDO;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

/**
 * to test.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Entity
@Indexed
public class MyNestedEntity extends StdRecordDO<Long>
{
  @ContainedIn
  private MyEntityDO parent;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String nestedName;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String nestedComment;

  @IndexedEmbedded(depth = 1)
  private MyNestedNestedEntity nestedNested;

  @Override
  public int hashCode()
  {
    return Objects.hashCode(nestedName);
  }

  @Override
  public boolean equals(Object obj)
  {
    if ((obj instanceof MyNestedEntity) == true) {
      return false;
    }
    MyNestedEntity other = (MyNestedEntity) obj;
    return Objects.equals(nestedName, other.getNestedName());
  }

  @Id
  @Override
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  @ManyToOne(optional = true)
  @JoinColumn(name = "parent", referencedColumnName = "pk")
  public MyEntityDO getParent()
  {
    return parent;
  }

  @OneToOne(optional = true)
  @JoinColumn(name = "nestednested", referencedColumnName = "pk")
  public MyNestedNestedEntity getNestedNested()
  {
    return nestedNested;
  }

  public void setNestedNested(MyNestedNestedEntity nestedNested)
  {
    this.nestedNested = nestedNested;
  }

  public void setParent(MyEntityDO parent)
  {
    this.parent = parent;
  }

  public String getNestedName()
  {
    return nestedName;
  }

  public void setNestedName(String name)
  {
    this.nestedName = name;
  }

  public String getNestedComment()
  {
    return nestedComment;
  }

  public void setNestedComment(String comment)
  {
    this.nestedComment = comment;
  }

}
