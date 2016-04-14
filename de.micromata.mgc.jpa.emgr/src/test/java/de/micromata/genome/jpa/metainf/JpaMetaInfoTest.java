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

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.jpa.test.JpaTestEntMgrFactory;
import de.micromata.mgc.common.test.MgcTestCase;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaMetaInfoTest extends MgcTestCase
{
  @Test
  public void testMember()
  {
    JpaTestEntMgrFactory fac = JpaTestEntMgrFactory.get();
    JpaMetadataRepostory repo = fac.getMetadataRepository();
    Set<Class<?>> keyset = repo.getEntities().keySet();

    int len = repo.getMaxLength(MyAnotTestDO.class, "testName");

    Assert.assertEquals(30, len);
    MyAnnotation foundanot = repo.findColumnAnnotation(MyAnotTestDO.class, "testName",
        MyAnnotation.class);
    Assert.assertNotNull(foundanot);

    ColumnMetadata colmeta = repo.findColumnMetadata(MyAnotTestDO.class, "testName");
    Assert.assertNotNull(colmeta);
    Assert.assertNotNull(colmeta.getGetter());
    Assert.assertNotNull(colmeta.getSetter());

    MyAnotTestDO myBean = new MyAnotTestDO();
    myBean.setTestName("Roger");
    String tn = (String) colmeta.getGetter().get(myBean);
    Assert.assertEquals("Roger", tn);
    colmeta.getSetter().set(myBean, "Kommer");
    Assert.assertEquals("Kommer", myBean.getTestName());
  }
}
