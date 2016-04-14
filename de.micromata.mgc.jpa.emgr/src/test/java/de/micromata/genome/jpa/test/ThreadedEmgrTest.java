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

import org.junit.Test;

import de.micromata.genome.jpa.EmgrCallable;
import de.micromata.genome.util.runtime.RuntimeCallable;
import de.micromata.mgc.common.test.MgcTestCase;
import de.micromata.mgc.common.test.ThreadedRunner;

/**
 * A threaded test of the emgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class ThreadedEmgrTest extends MgcTestCase
{
  @Test
  public void testThreaded()
  {
    int loopCount = 100;
    int threadCount = 4;
    new ThreadedRunner(loopCount, threadCount).run(new RuntimeCallable()
    {

      @Override
      public void call()
      {
        final JpaTestEntMgrFactory mgr = JpaTestEntMgrFactory.get();
        mgr.runInTrans(new EmgrCallable<Void, JpaTestEntMgr>()
        {

          @Override
          public Void call(JpaTestEntMgr mgr)
          {
            final GenomeJpaTestTableDO m = new GenomeJpaTestTableDO();
            m.setFirstName("Roger");
            mgr.insertAttached(m);
            mgr.update(m);
            mgr.remove(m);
            return null;
          }
        });
      }
    });
  }
}
