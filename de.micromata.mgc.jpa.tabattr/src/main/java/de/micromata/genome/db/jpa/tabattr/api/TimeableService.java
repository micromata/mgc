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

package de.micromata.genome.db.jpa.tabattr.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Methods to manage/validate Timeable DOs.
 *
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface TimeableService
{

  /**
   * Search the row, on given date.
   *
   * @param <T> the generic type
   * @param date must not be null
   * @param entity the entity
   * @return null if not found
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>> T getRowForTime(Date date,
      EntityWithTimeableAttr<PK, T> entity);

  /**
   * Gets the attr value.
   *
   * @param <R> the generic type
   * @param <T> the generic type
   * @param date the date
   * @param entity the entity
   * @param propertyName the property name
   * @param expectedClass the expected class
   * @return the attr value
   */
  <PK extends Serializable, R, T extends TimeableAttrRow<PK>> R getAttrValue(final Date date,
      final EntityWithTimeableAttr<PK, T> entity,
      final String propertyName, final Class<R> expectedClass);

  /**
   * Gets the attr value.
   *
   * @param <R> the generic type
   * @param <T> the generic type
   * @param entity the entity
   * @param propertyName the property name
   * @param expectedClass the expected class
   * @return the attr value
   */
  <PK extends Serializable, R, T extends TimeableAttrRow<PK>> R getAttrValue(final EntityWithTimeableAttr<PK, T> entity,
      final String propertyName, final Class<R> expectedClass);

}
