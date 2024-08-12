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

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import de.micromata.genome.jpa.StdRecordDO;

@Entity
@Indexed
public class MyNestedNestedEntity extends StdRecordDO<Long>
{
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String nestedNestedComment;

  @Id
  @Override
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  public String getNestedNestedComment()
  {
    return nestedNestedComment;
  }

  public void setNestedNestedComment(String nestedNestedComment)
  {
    this.nestedNestedComment = nestedNestedComment;
  }

}
