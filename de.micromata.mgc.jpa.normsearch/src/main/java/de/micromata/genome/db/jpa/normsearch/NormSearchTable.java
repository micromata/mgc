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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.micromata.genome.db.jpa.normsearch.entities.NormSearchDO;

/**
 * Mark an Entity as NormSearchable.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NormSearchTable {
  /**
   * Which Entity should be used to store norm search.
   * 
   * @return
   */
  Class<? extends NormSearchDO> normSearchTable();

  /**
   * Provide an option creator for a NormSearch entry.
   * 
   * @return empty or size 1 element.
   */
  Class<? extends NormalizedSearchEntryCreator>[] creator() default {};

  /**
   * Which properties of the Entity should be stored in the normalized search.
   *
   * If not set, uses the NormSearchProperty annotation.
   * 
   * @return the string[]
   */
  String[] normSearchFields() default {
  };
}
