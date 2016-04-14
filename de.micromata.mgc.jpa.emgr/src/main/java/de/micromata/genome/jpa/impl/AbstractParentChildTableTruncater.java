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

package de.micromata.genome.jpa.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * In case a table has reference to itself as parent node..
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class AbstractParentChildTableTruncater<ENT> implements TableTruncater
{
  protected abstract Serializable getPk(ENT ent);

  protected abstract Serializable getParentPk(ENT ent);

  @Override
  public int truncateTable(IEmgr<?> emgr, EntityMetadata entity)
  {
    List<ENT> allents = emgr.selectAllAttached((Class<ENT>) entity.getJavaType());
    if (allents.isEmpty() == true) {
      return 0;
    }
    List<ENT> sorted = new ArrayList<>();
    Map<Serializable, ENT> alreadyDeleted = new HashMap<>();
    Map<Serializable, ENT> allEnts = new HashMap<>();
    allents.forEach((ent) -> allEnts.put(getPk(ent), ent));
    for (ENT skill : allents) {
      fillSortedList(skill, sorted, allEnts, alreadyDeleted);
    }
    EntityManager em = emgr.getEntityManager();
    for (ENT delent : sorted) {
      em.remove(delent);
    }
    return sorted.size();
  }

  private void fillSortedList(ENT ent, List<ENT> sorted, Map<Serializable, ENT> allEnts,
      Map<Serializable, ENT> alreadyDeleted)
  {
    Serializable pk = getPk(ent);
    if (alreadyDeleted.containsKey(pk) == true) {
      return;
    }
    alreadyDeleted.put(pk, ent);
    for (ENT pc : allEnts.values()) {
      Object parentpk = getParentPk(pc);
      if (parentpk != null && parentpk.equals(pk) == true) {
        fillSortedList(pc, sorted, allEnts, alreadyDeleted);
      }
    }
    sorted.add(ent);
  }
}
