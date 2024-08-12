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

import org.hibernate.search.jpa.FullTextEntityManager;

import de.micromata.genome.jpa.IEmgr;

/**
 * Little wrapper to Hibernate Search Fulltext wrapper.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class FullTextSearch
{
  public static FullTextEntityManager getFullTextEntityManager(IEmgr<?> emgr)
  {
    return getFullTextEntityManager(emgr.getEntityManager());
  }

  public static FullTextEntityManager getFullTextEntityManager(EntityManager entityManager)
  {
    FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
        .getFullTextEntityManager(entityManager);

    return fullTextEntityManager;
  }
}
