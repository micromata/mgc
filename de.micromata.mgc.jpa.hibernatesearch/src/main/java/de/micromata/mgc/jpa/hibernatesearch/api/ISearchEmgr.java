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

package de.micromata.mgc.jpa.hibernatesearch.api;

import java.util.List;

import org.hibernate.search.jpa.FullTextEntityManager;

import de.micromata.genome.jpa.IEmgr;

/**
 * Extension of Emgr, with Hibernate Search.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface ISearchEmgr<EMGR extends ISearchEmgr<?>>extends IEmgr<EMGR>
{

  <T> List<T> searchDetached(String expression, Class<T> type, String... fields);

  <T> List<T> searchAttached(String expression, Class<T> type, String... fields);

  <T> List<T> searchWildcardDetached(String expression, Class<T> type, String... fields);

  <T> List<T> searchWildcardAttached(String expression, Class<T> type, String... fields);

  //  org.hibernate.search.query.dsl.QueryBuilder getFullTexteSearchQueryBuilder();

  <T> List<T> searchAttached(org.apache.lucene.search.Query luceneQuery, Class<T> type);

  /**
   * Invokes indexing on given entity.
   * 
   * Events: SearchEmgrReindexEventFilterEvent
   * 
   * @param entity the entity to reindex for the search
   */
  void reindex(Object entity);

  /**
   * Gets the full text entity manager.
   *
   * @return the full text entity manager
   */
  FullTextEntityManager getFullTextEntityManager();

}
