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

package de.micromata.genome.db.jpa.normsearch;

import java.util.List;

import de.micromata.genome.db.jpa.normsearch.entities.NormSearchDO;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.IEmgr;

/**
 * Store fields of a master table into a tokenized search table.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public interface NormalizedSearchService
{
  /**
   * Register the listener to Emgr.
   *
   * @param emgrFactory the emgr factory
   */
  void registerEmgrListener(EmgrFactory<?> emgrFactory);

  /**
   * Converts a string into normalized form.
   *
   * @param input unnormalized string. Must not be null.
   * @return the normalized string
   */
  public String normalize(String input);

  /**
   * split a input string into token.
   *
   * @param input the input
   * @return the string[]
   */
  public String[] tokenize(String input);

  /**
   * Search.
   *
   * @param emgr the emgr
   * @param clazz the clazz
   * @param expression the expression
   * @return the list
   */
  public List<Long> search(IEmgr<?> emgr, Class<? extends NormSearchDO> clazz, String expression);

  /**
   * Insert using the NormSearchTable annotation.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public void onInsert(IEmgr<?> emgr, DbRecord<?> entity);

  /**
   * Update using the NormSearchTable annotation.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public void onUpdate(IEmgr<?> emgr, DbRecord<?> entity);

  /**
   * delete using the NormSearchTable annotation.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public void onDelete(IEmgr<?> emgr, DbRecord<?> entity);

  /**
   * Creates an default normsearch entity instance.
   * 
   * @param entity
   * @param normSearchEntity
   * @return
   */
  public <T extends NormSearchDO> T createDefaultNormSearchEntry(DbRecord<?> entity, Class<T> normSearchEntity);
}
