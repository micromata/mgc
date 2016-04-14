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

package de.micromata.mgc.jpa.xmldump.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.micromata.genome.db.jpa.xmldump.api.JpaXmlPersist;
import de.micromata.genome.jpa.StdRecordDO;

@Entity
@JpaXmlPersist(beforePersistListener = TestWithLazyRefBeforePeristListener.class)
public class TestWithLazyRef extends StdRecordDO<Long>
{
  private TestWithLazyRef parent;

  private TestByLazyRef other;

  @Override
  @Id
  @GeneratedValue
  public Long getPk()
  {
    return pk;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_fk")
  public TestWithLazyRef getParent()
  {
    return parent;
  }

  public void setParent(TestWithLazyRef parent)
  {
    this.parent = parent;
  }

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "other_fk")
  public TestByLazyRef getOther()
  {
    return other;
  }

  public void setOther(TestByLazyRef other)
  {
    this.other = other;
  }

}
