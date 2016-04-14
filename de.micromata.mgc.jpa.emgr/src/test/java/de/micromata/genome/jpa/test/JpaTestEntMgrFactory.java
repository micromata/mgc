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

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * Just a manager factory for testing.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class JpaTestEntMgrFactory extends EmgrFactory<JpaTestEntMgr>
{
  // An EmgrFactory has to be singlton
  static JpaTestEntMgrFactory INSTANCE;

  // because this method is synchronized hold the factory in you service
  public static synchronized JpaTestEntMgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new JpaTestEntMgrFactory();
    return INSTANCE;
  }

  protected JpaTestEntMgrFactory()
  {
    // the name of the persistence context
    super("de.micromata.genome.jpa.test");
  }

  /**
   * Create an IEmgr instance. If you don't want to create your own type, you can also use DefaultEmgr {@inheritDoc}
   *
   */
  @Override
  protected JpaTestEntMgr createEmgr(EntityManager entitManager, EmgrTx<JpaTestEntMgr> emgrTx)
  {
    return new JpaTestEntMgr(entitManager, this, emgrTx);
  }
}
