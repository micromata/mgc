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

package de.micromata.genome.db.jpa.history.api;

import jakarta.persistence.EntityManager;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

/**
 * A factory for creating JpaEntityManager objects.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JpaHistoryEntityManagerFactory extends EmgrFactory<DefaultEmgr>
{

  /**
   * The instance.
   */
  static JpaHistoryEntityManagerFactory INSTANCE;

  /**
   * Gets the.
   *
   * @return the jpa history entity manager factory
   */
  public static synchronized JpaHistoryEntityManagerFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new JpaHistoryEntityManagerFactory();
    return INSTANCE;
  }

  /**
   * Instantiates a new jpa entity manager factory.
   */
  public JpaHistoryEntityManagerFactory()
  {
    super("de.micromata.genome.db.jpa.history");
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
