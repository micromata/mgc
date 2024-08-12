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

package de.micromata.mgc.jpa.hibernatesearch;

import jakarta.persistence.EntityManager;

import de.micromata.genome.jpa.EmgrTx;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchEmgrFactory;
import de.micromata.mgc.jpa.hibernatesearch.impl.DefaultSearchEmgr;

public class HibernateSearchTestEmgrFactory extends SearchEmgrFactory<DefaultSearchEmgr>
{
  static HibernateSearchTestEmgrFactory INSTANCE;

  public static synchronized HibernateSearchTestEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new HibernateSearchTestEmgrFactory();
    return INSTANCE;
  }

  protected HibernateSearchTestEmgrFactory()
  {
    super("de.micromata.mgc.jpa.hibernatesearch.test");
  }

  @Override
  protected DefaultSearchEmgr createEmgr(EntityManager entityManager, EmgrTx<DefaultSearchEmgr> emgrTx)
  {
    return new DefaultSearchEmgr(entityManager, this, emgrTx);
  }
}
