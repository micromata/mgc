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
 * Can be used if no extended functions are needed in Emgr.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class DefaultEmgr extends Emgr<DefaultEmgr>
{

  /**
   * Instantiates a new default emgr.
   *
   * @param entityManager the entity manager
   * @param emgrFactory the emgr factory
   * @param emgrTx the entity manager transaction
   */
  public DefaultEmgr(EntityManager entityManager, EmgrFactory<DefaultEmgr> emgrFactory, EmgrTx<DefaultEmgr> emgrTx)
  {
    super(entityManager, emgrFactory, emgrTx);
  }

}
