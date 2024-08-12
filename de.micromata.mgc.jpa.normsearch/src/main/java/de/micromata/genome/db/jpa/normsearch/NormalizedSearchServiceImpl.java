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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.db.jpa.normsearch.entities.NormSearchDO;
import de.micromata.genome.db.jpa.normsearch.eventlistener.NormSearchAfterDeleteListener;
import de.micromata.genome.db.jpa.normsearch.eventlistener.NormSearchAfterInsertEventListener;
import de.micromata.genome.db.jpa.normsearch.eventlistener.NormSearchAfterUpdateEventListener;
import de.micromata.genome.jpa.ComplexEntity;
import de.micromata.genome.jpa.ComplexEntityVisitor;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.events.EmgrEventRegistry;
import de.micromata.genome.util.bean.FieldMatchers;
import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * Standard implementation of NormalizedSearchDAO.
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class NormalizedSearchServiceImpl implements NormalizedSearchService
{
  @Override
  public void registerEmgrListener(EmgrFactory<?> emgrFactory)
  {
    EmgrEventRegistry ef = emgrFactory.getEventFactory();
    ef.registerEvent(new NormSearchAfterUpdateEventListener());
    ef.registerEvent(new NormSearchAfterInsertEventListener());
    ef.registerEvent(new NormSearchAfterDeleteListener());

  }

  // beispiel select
  // select * from TB_TST_ATTRMASTER n where n.TST_ATTRMASTER in (
  // select distinct m.TST_ATTRMASTER as x
  // from TB_TST_ATTRMASTER_NSEARCH t, TB_TST_ATTRMASTER_NSEARCH s, TB_TST_ATTRMASTER m
  // where t.value like 'KOM%' and s.value like 'KOM%' and t.parent = s.parent
  // and t.parent = m.TST_ATTRMASTER
  // )
  // order by n.modifiedat desc
  //
  // ;
  @Override
  public List<Long> search(IEmgr<?> emgr, final Class<? extends NormSearchDO> clazz, final String expression)
  {
    if (StringUtils.isBlank(expression) == true) {
      return Collections.emptyList();
    }
    String[] tks = StringUtils.split(expression, " ");
    StringBuilder sb = new StringBuilder();
    Map<String, Object> args = new HashMap<String, Object>();
    sb.append("select distinct(m.parent) from ").append(clazz.getSimpleName()).append(" m where ");
    int num;
    for (int i = 0; i < tks.length; ++i) {
      if (i > 0) {
        sb.append(" or ");
      }
      sb.append("m.value like :a" + i);
      args.put("a" + i, normalize(tks[i]) + "%");
    }
    return emgr.selectAttached(Long.class, sb.toString(), args);

  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.db.jpa.normsearch.NormalizedSearchDAO#normalize(java.lang.String)
   */
  @Override
  public String normalize(String input)
  {
    return input.toUpperCase();
  }

  @Override
  public String[] tokenize(String input)
  {
    if (StringUtils.isEmpty(input) == true) {
      return new String[] {};
    }
    // String t = input.toUpperCase();
    String[] els = StringUtils.split(input, " \t\n");
    for (int i = 0; i < els.length; ++i) {
      els[i] = normalize(els[i]);
    }
    return els;
  }

  /**
   * Read.
   *
   * @param rec the rec
   * @param fields the fields
   * @return the list
   */
  protected List<NormSearchDO> read(NormSearchMasterTable rec, String... fields)
  {
    List<NormSearchDO> entries = new ArrayList<NormSearchDO>();
    for (String field : fields) {
      Object val = PrivateBeanUtils.readField(rec, field);
      if (val == null) {
        continue;
      }
      String[] tokens = tokenize(val.toString());
      for (String tk : tokens) {
        NormSearchDO sd = rec.createNormSearchEntry(field, tk);
        entries.add(sd);
      }
    }
    return entries;
  }

  //  @Override
  //  public void update(IEmgr<?> emgr, final NormSearchMasterTable rec, final String... fields)
  //  {
  //    delete(emgr, rec, fields);
  //    insert(emgr, rec, fields);
  //  }
  //
  //  @Override
  //  public void insert(IEmgr<?> emgr, NormSearchMasterTable rec, String... fields)
  //  {
  //    final List<NormSearchDO> entries = read(rec, fields);
  //    for (NormSearchDO ns : entries) {
  //      emgr.insertDetached(ns);
  //    }
  //
  //  }
  //
  //  @Override
  //  public void delete(IEmgr<?> emgr, final NormSearchMasterTable rec, final String... fields)
  //  {
  //    Query q = emgr.createUntypedQuery(
  //        "delete from " + rec.getNormSearchTableClass().getCanonicalName() + " d where d.parent = :parent", "parent",
  //        rec.getPk());
  //    q.executeUpdate();
  //  }

  /**
   * Gets the search fields.
   *
   * @param nsanot the nsanot
   * @param entity the entity
   * @return the search fields
   */
  private List<Field> getSearchFields(NormSearchTable nsanot, Object entity)
  {
    if (nsanot.normSearchFields().length != 0) {
      return Arrays.asList(nsanot.normSearchFields()).stream().map((e) -> PrivateBeanUtils.findField(entity, e))
          .collect(Collectors.toList());
    }
    List<Field> allFields = PrivateBeanUtils.findAllFields(entity.getClass(),
        FieldMatchers.hasAnnotation(NormSearchProperty.class));

    return allFields;
  }

  private Long fkToLong(Serializable ipk)
  {
    if ((ipk instanceof Number) == false) {
      throw new IllegalArgumentException("NormSearch requires a number as PK");
    }
    return ((Number) ipk).longValue();
  }

  /**
   * Gets the norm search entries.
   *
   * @param tableClass the table class
   * @param rec the rec
   * @param fields the fields
   * @return the norm search entries
   */
  protected List<NormSearchDO> getNormSearchEntries(Class<? extends NormSearchDO> tableClass, DbRecord rec,
      List<Field> fields)
  {
    List<NormSearchDO> entries = new ArrayList<NormSearchDO>();
    for (Field field : fields) {
      Object val = PrivateBeanUtils.readField(rec, field);
      if (val == null) {
        continue;
      }
      String[] tokens = tokenize(val.toString());
      for (String tk : tokens) {
        NormSearchDO sd = createDefaultNormSearchEntry(rec, tableClass);
        sd.setColName(field.getName());
        sd.setValue(tk);
        entries.add(sd);
      }
    }
    return entries;
  }

  protected boolean isForNormSearch(DbRecord<?> entity)
  {
    if (entity instanceof NormSearchTable) {
      return true;
    }
    NormSearchTable nsanot = entity.getClass().getAnnotation(NormSearchTable.class);
    return nsanot != null;
  }

  @Override
  public void onUpdate(IEmgr<?> emgr, DbRecord<?> entity)
  {
    if (isForNormSearch(entity) == false) {
      return;
    }
    onDelete(emgr, entity);
    onInsert(emgr, entity);
  }

  @Override
  public void onInsert(IEmgr<?> emgr, DbRecord<?> entity)
  {

    if (entity instanceof ComplexEntity == false) {
      insertImpl(emgr, entity);
      return;
    }
    ComplexEntity ce = (ComplexEntity) entity;
    ce.visit(new ComplexEntityVisitor()
    {
      @Override
      public void visit(DbRecord rec)
      {
        insertImpl(emgr, rec);
      }
    });
  }

  protected void insertImpl(IEmgr<?> emgr, DbRecord<?> entity)
  {
    NormSearchTable nsanot = entity.getClass().getAnnotation(NormSearchTable.class);
    if (nsanot != null) {
      Class<? extends NormSearchDO> table = nsanot.normSearchTable();
      List<Field> fields = getSearchFields(nsanot, entity);
      List<NormSearchDO> nes = getNormSearchEntries(table, entity, fields);
      for (NormSearchDO ns : nes) {
        emgr.insertDetached(ns);
      }
    }
    if (entity instanceof NormSearchMasterTable) {
      insertImpl(emgr, entity, (NormSearchMasterTable) entity);
    }
  }

  protected void insertImpl(IEmgr<?> emgr, DbRecord<?> entity, NormSearchMasterTable normSearchTable)
  {
    List<NormSearchDO> entries = read(normSearchTable, normSearchTable.getSearchPropertyNames());
    for (NormSearchDO ns : entries) {
      emgr.insertDetached(ns);
    }
  }

  @Override
  public void onDelete(IEmgr<?> emgr, DbRecord<?> entity)
  {
    if (entity instanceof ComplexEntity == false) {
      deleteImpl(emgr, entity);
      return;
    }
    ComplexEntity ce = (ComplexEntity) entity;
    ce.visit(new ComplexEntityVisitor()
    {
      @Override
      public void visit(DbRecord rec)
      {
        deleteImpl(emgr, rec);
      }
    });

  }

  protected void deleteImpl(IEmgr<?> emgr, DbRecord entity)
  {

    NormSearchTable nsanot = entity.getClass().getAnnotation(NormSearchTable.class);
    Class<? extends NormSearchDO> nstable;
    if (nsanot != null) {
      nstable = nsanot.normSearchTable();
    } else if (entity instanceof NormSearchMasterTable) {
      nstable = ((NormSearchMasterTable) entity).getNormSearchTableClass();
    } else {
      return;
    }

    Query q = emgr.createUntypedQuery(
        "delete from " + nstable.getName() + " d where d.parent = :parent", "parent",
        entity.getPk());
    q.executeUpdate();
  }

  @Override
  public <T extends NormSearchDO> T createDefaultNormSearchEntry(DbRecord<?> entity, Class<T> normSearchEntity)
  {
    T sd = PrivateBeanUtils.createInstance(normSearchEntity);
    sd.setParent(fkToLong(entity.getPk()));
    return sd;
  }

}
