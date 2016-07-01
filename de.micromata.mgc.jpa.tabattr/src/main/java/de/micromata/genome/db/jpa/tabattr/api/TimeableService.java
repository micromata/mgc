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
import java.util.List;

/**
 * Methods to manage/validate Timeable DOs.
 */
public interface TimeableService<PK extends Serializable, T extends TimeableAttrRow<PK>>
{
  /**
   * Returns the attrRow for a given Date depending on the type of the group or null.
   *
   * @param attrRows The attrRows must be sorted by start time descending.
   * @param group    The group.
   * @param date     The date to find a row for.
   * @return The attrRow or null.
   */
  T getAttrRowForDate(final List<T> attrRows, final AttrGroup group, final Date date);

  /**
   * Returns the attrRows belonging to the given group.
   *
   * @param entity The entity with timeable attrRows
   * @param group  The group to find attrRows for.
   * @return The attrRows belonging to the given group.
   */
  List<T> getTimeableAttrRowsForGroup(final EntityWithTimeableAttr<PK, T> entity, final AttrGroup group);

  /**
   * Sorts the attrRows by start time descending, if the start time of a row is null, this row will the first element of the list.
   *
   * @param attrRows The TimeableAttrRows to sort.
   * @return The sorted TimeableAttrRow.
   */
  List<T> sortTimeableAttrRowsByDateDescending(List<T> attrRows);
}
