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

package de.micromata.genome.db.jpa.history.test;

import jakarta.persistence.EntityManager;

import de.micromata.genome.db.jpa.history.api.HistoryServiceManager;
import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * A factory for creating HistoryTestEmgr objects.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class HistoryTestEmgrFactory extends EmgrFactory<DefaultEmgr>
{
  private static HistoryTestEmgrFactory INSTANCE;

  /**
   * Gets the.
   *
   * @return the jpa genome core ent mgr factory
   */
  public static synchronized HistoryTestEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new HistoryTestEmgrFactory();
    return INSTANCE;
  }

  /**
   * Instantiates a new history test emgr factory.
   */
  public HistoryTestEmgrFactory()
  {
    super("de.micromata.genome.db.jpa.history.test");
  }

  @Override
  protected void registerEvents()
  {
    super.registerEvents();
    HistoryServiceManager.get().getHistoryService().registerEmgrListener(this);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager, EmgrTx<DefaultEmgr> emgrTx)
  {
    return new DefaultEmgr(entityManager, this, emgrTx);
  }

}
