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

package de.micromata.genome.jpa;

import jakarta.persistence.EntityManager;

/**
 * The Class EntityManagerWrapper.
 *
 * @author Sebastian Hardt (s.hardt@micromata.de) Date: 10/14/13 Time: 4:13 PM
 */
public class EntityManagerWrapper
{

  /**
   * The entity manager.
   */
  final private EntityManager entityManager;

  /**
   * The in transaction.
   */
  private boolean inTransaction = false;

  /**
   * Instantiates a new entity manager wrapper.
   *
   * @param entityManager the entity manager
   */
  public EntityManagerWrapper(EntityManager entityManager)
  {
    this.entityManager = entityManager;
  }

  public boolean isInTransaction()
  {
    return inTransaction;
  }

  public void setInTransaction(boolean inTransaction)
  {
    this.inTransaction = inTransaction;
  }

  public EntityManager getEntityManager()
  {
    return entityManager;
  }
}
