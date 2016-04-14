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

import javax.persistence.Transient;

import de.micromata.genome.db.jpa.normsearch.entities.NormSearchDO;
import de.micromata.genome.jpa.StdRecord;

/**
 * An Interface, which a table should implement to write its columns as normalized search values.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */

public interface NormSearchMasterTable extends StdRecord<Long>
{
  /**
   * Create a new row for searching
   * 
   * @param column the column to create normalized search
   * @param value the value to create normalized search
   * @return
   */
  public NormSearchDO createNormSearchEntry(String column, String value);

  /**
   * return the class to for the normalized search.
   *
   * @return the norm search table class
   */
  @Transient
  public Class<? extends NormSearchDO> getNormSearchTableClass();

  /**
   * The columns to search with values.
   *
   * @return never null
   */
  @Transient
  String[] getSearchPropertyNames();
}
