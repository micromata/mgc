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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

@Entity(name = "TB_BASE_MY_ANOT_TEST")
public class MyAnotTestDO extends MyAnotBaseTestDO
{
  @MyAnnotation
  private String testName;

  private MyAnotChildDO parent;

  private List<MyAnotChildDO> children = new ArrayList<>();

  private Map<String, MyAnotChildDO> keyedChilder = new HashMap<>();

  @Override
  @Id
  public Long getPk()
  {
    return pk;
  }

  @Column(name = "TEST_NAME", length = 30)
  public String getTestName()
  {
    return testName;
  }

  public void setTestName(String testName)
  {
    this.testName = testName;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = true)
  public MyAnotChildDO getParent()
  {
    return parent;
  }

  public void setParent(MyAnotChildDO parent)
  {
    this.parent = parent;
  }

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "parent")
  public List<MyAnotChildDO> getChildren()
  {
    return children;
  }

  public void setChildren(List<MyAnotChildDO> children)
  {
    this.children = children;
  }

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", targetEntity = MyAnotChildDO.class,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @MapKey(name = "propertyName")

  public Map<String, MyAnotChildDO> getKeyedChilder()
  {
    return keyedChilder;
  }

  public void setKeyedChilder(Map<String, MyAnotChildDO> keyedChilder)
  {
    this.keyedChilder = keyedChilder;
  }

}
