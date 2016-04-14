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

package de.micromata.genome.db.jpa.logging;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * Entity Manager for AttrBaseDAOImpl.
 *
 * @author roger
 */
public class JpaLoggingEntMgrFactory extends EmgrFactory<DefaultEmgr>
{

  /**
   * The instance.
   */
  static JpaLoggingEntMgrFactory INSTANCE;

  /**
   * Gets the.
   *
   * @return the jpa logging ent mgr factory
   */
  public static synchronized JpaLoggingEntMgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new JpaLoggingEntMgrFactory();
    return INSTANCE;
  }

  /**
   * Instantiates a new jpa logging ent mgr factory.
   */
  protected JpaLoggingEntMgrFactory()
  {
    super("de.micromata.genome.db.jpa.logging");

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
