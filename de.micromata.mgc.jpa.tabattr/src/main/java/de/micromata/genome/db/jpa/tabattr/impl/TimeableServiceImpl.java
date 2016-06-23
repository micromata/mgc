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
import java.util.Date;

import de.micromata.genome.db.jpa.tabattr.api.AttrSchemaService;
import de.micromata.genome.db.jpa.tabattr.api.EntityWithTimeableAttr;
import de.micromata.genome.db.jpa.tabattr.api.TimeableAttrRow;
import de.micromata.genome.db.jpa.tabattr.api.TimeableService;

/**
 * Standard implementation for TimeableService.
 *
 * @author Roger Kommer (r.kommer.extern@micromata.de)
 */
public class TimeableServiceImpl implements TimeableService
{
  private AttrSchemaService attrSchemaService;

  /**
   * Validate timeable.
   *
   * @param ctx the ctx
   * @param list the list
   * @return true, if successful
   */
  //  public boolean validateTimeable(final FrontendValidationContext ctx, final List<TimeableBaseDO<?>> list)
  //  {
  //    if (list.isEmpty() == true) {
  //      return true;
  //    }
  //    list.sort(Comparator.comparing(TimeableBaseDO::getStartTime));
  //    final Stream<TimeableBaseDO<?>> noEndTime = list.stream().filter(a -> a.getEndTime() == null);
  //    if (noEndTime.count() > 1) {
  //      // TODO collect elements
  //      noEndTime.collect(Collectors.toList());
  //      ctx.addValidationMessage(ValidationState.Error, "TODO only one empty");
  //      return false;
  //    }
  //
  //    return true;
  //  }

  /**
   * @see org.projectforge.framework.persistence.attr.api.TimeableService#getRowForTime(java.util.Date,
   *      org.projectforge.framework.persistence.attr.api.EntityWithTimeableAttr)
   */
  @Override
  public <PK extends Serializable, T extends TimeableAttrRow<PK>> T getRowForTime(final Date date,
      final EntityWithTimeableAttr<PK, T> entity)
  {
    T lastRow = null;

    for (final T td : entity.getTimeableAttributes()) {
      if (td.getStartTime() == null || td.getStartTime().getTime() > date.getTime()) {
        continue;
      }
      lastRow = td;
      break;
    }
    return lastRow;
  }

  public <PK extends Serializable, R, T extends TimeableAttrRow<PK>> R getDefaultAttrValue(
      final EntityWithTimeableAttr<PK, T> entity,
      final String propertyName, final Class<R> expectedClass)
  {
    return attrSchemaService.getDefaultValue(entity.getAttrSchemaName(), propertyName, expectedClass);
  }

  /**
   * @see org.projectforge.framework.persistence.attr.api.TimeableService#getAttrValue(java.util.Date,
   *      org.projectforge.framework.persistence.attr.api.EntityWithTimeableAttr, java.lang.String, java.lang.Class)
   */
  @Override
  public <PK extends Serializable, R, T extends TimeableAttrRow<PK>> R getAttrValue(final Date date,
      final EntityWithTimeableAttr<PK, T> entity,
      final String propertyName, final Class<R> expectedClass)
  {
    final T row = getRowForTime(date, entity);
    if (row == null) {
      return getDefaultAttrValue(entity, propertyName, expectedClass);
    }
    final R ret = row.getAttribute(propertyName, expectedClass);
    if (ret == null) {
      return getDefaultAttrValue(entity, propertyName, expectedClass);
    }
    return ret;
  }

  @Override
  public <PK extends Serializable, R, T extends TimeableAttrRow<PK>> R getAttrValue(
      final EntityWithTimeableAttr<PK, T> entity,
      final String propertyName, final Class<R> expectedClass)
  {
    return getAttrValue(new Date(), entity, propertyName, expectedClass);
  }

  public AttrSchemaService getAttrSchemaService()
  {
    return attrSchemaService;
  }

  public void setAttrSchemaService(final AttrSchemaService attrSchemaService)
  {
    this.attrSchemaService = attrSchemaService;

  }
}
