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

package de.micromata.genome.db.jpa.tabattr.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import de.micromata.genome.db.jpa.tabattr.api.AttrGroup;
import de.micromata.genome.db.jpa.tabattr.api.EntityWithTimeableAttr;
import de.micromata.genome.db.jpa.tabattr.api.TimeableAttrRow;
import de.micromata.genome.db.jpa.tabattr.api.TimeableService;
import de.micromata.genome.util.types.DateUtils;

/**
 * Standard implementation for TimeableService.
 */
public class TimeableServiceImpl implements TimeableService
{
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TimeableServiceImpl.class);

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowForDate(final EntityWithTimeableAttr<PK, T> entity, final String groupName, final Date date)
  {
    final List<T> timeableAttrRows = getTimeableAttrRowsForGroupName(entity, groupName);
    return getAttrRowForDate(timeableAttrRows, date);
  }

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowForDate(final List<T> attrRows, final Date date)
  {
    final List<T> rowsSorted = sortTimeableAttrRowsByDateDescending(attrRows);
    return rowsSorted
        .stream()
        // filter all attrRows without a start time and where the given date is equal or after the rows date
        .filter(row -> (row.getStartTime() == null || date.compareTo(row.getStartTime()) >= 0))
        .findFirst()
        .orElse(null);
  }

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowForDate(final List<T> attrRows, final AttrGroup group, final Date date)
  {
    switch (group.getType()) {
      case PERIOD:
        return getAttrRowForDate(attrRows, date);

      case INSTANT_OF_TIME:
        // do not select a row by default
        return null;

      default:
        throw new IllegalArgumentException("The Type " + group.getType() + " is not supported.");
    }
  }

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowForSameMonth(final List<T> attrRows, final Date dateToSelectAttrRow)
  {
    return attrRows
        .stream()
        .filter(row -> (row.getStartTime() != null && DateUtils.isSameMonth(row.getStartTime(), dateToSelectAttrRow)))
        .findFirst()
        .orElse(null);
  }

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowForSameMonth(final EntityWithTimeableAttr<PK, T> entity, final AttrGroup group,
      final Date dateToSelectAttrRow)
  {
    final List<T> timeableAttrRowsForGroup = getTimeableAttrRowsForGroup(entity, group);
    return getAttrRowForSameMonth(timeableAttrRowsForGroup, dateToSelectAttrRow);
  }

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowForSameMonth(final EntityWithTimeableAttr<PK, T> entity, final String groupName,
      final Date dateToSelectAttrRow)
  {
    final List<T> timeableAttrRowsForGroup = getTimeableAttrRowsForGroupName(entity, groupName);
    return getAttrRowForSameMonth(timeableAttrRowsForGroup, dateToSelectAttrRow);
  }

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<T> getTimeableAttrRowsForGroup(final EntityWithTimeableAttr<PK, T> entity, final AttrGroup group)
  {
    return getTimeableAttrRowsForGroupName(entity, group.getName());
  }

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<T> getTimeableAttrRowsForGroupName(final EntityWithTimeableAttr<PK, T> entity, final String groupName)
  {
    if (groupName == null) {
      return Collections.emptyList();
    }

    return entity
        .getTimeableAttributes()
        .stream()
        .filter(row -> groupName.equals(row.getGroupName()))
        .collect(Collectors.toList());
  }

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<T> sortTimeableAttrRowsByDateDescending(List<T> attrRows)
  {
    return attrRows
        .stream()
        .sorted((row1, row2) ->
            (row1.getStartTime() == null) ? -1 :
                (row2.getStartTime() == null) ? 1 :
                    row2.getStartTime().compareTo(row1.getStartTime()))
        .collect(Collectors.toList());
  }

  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<Integer> getAvailableStartTimeYears(final List<? extends EntityWithTimeableAttr<PK, T>> entityList)
  {
    return entityList
        .stream()
        .map(EntityWithTimeableAttr::getTimeableAttributes)
        .flatMap(List::stream) // flatten the stream of lists to a single stream
        .map(TimeableAttrRow::getStartTime)
        .map(date -> {
          final Calendar cal = new GregorianCalendar();
          cal.setTime(date);
          return cal.get(Calendar.YEAR);
        })
        .distinct()
        .sorted()
        .collect(Collectors.toList());
  }

}
