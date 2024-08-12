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

package de.micromata.genome.db.jpa.xmldump.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.MarshallingStrategy;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;
import com.thoughtworks.xstream.core.TreeUnmarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import de.micromata.genome.db.jpa.xmldump.api.JpaXmlBeforePersistListener;
import de.micromata.genome.db.jpa.xmldump.api.JpaXmlDumpService;
import de.micromata.genome.db.jpa.xmldump.api.JpaXmlPersist;
import de.micromata.genome.db.jpa.xmldump.api.XmlDumpRestoreContext;
import de.micromata.genome.jpa.Emgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.jpa.metainf.JpaMetadataRepostory;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.ClassUtils;
import de.micromata.genome.util.runtime.RuntimeIOException;

/**
 * The Class JpaXmlDumpServiceImpl.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JpaXmlDumpServiceImpl implements JpaXmlDumpService, XmlJpaPersistService
{
  private static final Logger LOG = Logger.getLogger(JpaXmlDumpServiceImpl.class);

  private List<JpaXmlBeforePersistListener> globalBeforeListener = new ArrayList<>();

  public JpaXmlDumpServiceImpl()
  {
    initGlobalListener();
  }

  protected void initGlobalListener()
  {
    for (JpaXmlBeforePersistListener sl : ServiceLoader.load(JpaXmlBeforePersistListener.class)) {
      globalBeforeListener.add(sl);
    }
  }

  @Override
  public String dumpToXml(EmgrFactory<?> fac)
  {
    StringWriter sout = new StringWriter();
    dumpToXml(fac, sout);
    return sout.getBuffer().toString();
  }

  @Override
  public void dumpToXml(EmgrFactory<?> fac, File outfile)
  {
    try (Writer out = new OutputStreamWriter(new FileOutputStream(outfile), Charset.forName("UTF-8"))) {
      dumpToXml(fac, out);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  @Override
  public void dumpToXml(EmgrFactory<?> fac, OutputStream out)
  {
    try (Writer wout = new OutputStreamWriter(out)) {
      dumpToXml(fac, wout);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  @Override
  public void dumpToXml(EmgrFactory<?> fac, Writer writer)
  {
    fac.runInTrans((emgr) -> {
      List<Object> all = getObjectsToDump((Emgr<?>) emgr);
      final XStream stream = initXStreamForMarshaling(false);
      // und schreiben
      try {
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
      } catch (final IOException ex) {
        // ignore, will fail on stream.marshal()
      }
      final MarshallingStrategy marshallingStrategy = new ProxyIdRefMarshallingStrategy();
      stream.setMarshallingStrategy(marshallingStrategy);
      stream.marshal(all, new PrettyPrintWriter(writer));
      LOG.info("Wrote " + all.size() + " objects");
      return null;
    });
  }

  private XStream initXStreamForMarshaling(final boolean nullifyPk)
  {

    // initalisierung des Objekts...

    //    defaultXStream.marshal(obj, new CompactWriter(new NullWriter()));

    XStream xstream = new XStream()
    {
      @Override
      protected MapperWrapper wrapMapper(final MapperWrapper next)
      {
        return new HibernateMapper(new HibernateCollectionsMapper(next));
      }
    };
    xstream.ignoreUnknownElements();
    // Converter für die Hibernate-Collections
    xstream.registerConverter(new HibernateCollectionConverter(xstream.getConverterLookup()));
    xstream.registerConverter(
        new HibernateProxyConverter(xstream.getMapper(), new PureJavaReflectionProvider(),
            xstream.getConverterLookup()),
        XStream.PRIORITY_VERY_HIGH);

    xstream.setMarshallingStrategy(new ProxyIdRefMarshallingStrategy());
    init(xstream);
    return xstream;
  }

  protected void init(XStream xstream)
  {

  }

  protected List<Object> getObjectsToDump(Emgr<?> emgr)
  {
    List<EntityMetadata> entities = emgr.getEmgrFactory().getMetadataRepository().getTableEntities();
    Collections.reverse(entities);
    List<Object> allEntities = new ArrayList<>();
    for (EntityMetadata entityClass : entities) {
      List<?> ents = emgr.selectAllAttached(entityClass.getJavaType());
      expandLazyLoading(emgr, entityClass, ents);
      allEntities.addAll(ents);

    }

    return allEntities;
  }

  private void expandLazyLoading(Emgr<?> emgr, EntityMetadata entityClass, List<?> ents)
  {
    for (Object ent : ents) {
      for (ColumnMetadata col : entityClass.getColumns().values()) {
        if (col.isAssociation() == true || col.isCollection() == true) {

          Object prop = col.getGetter().get(ent);
          if (col.isCollection() == true) {
            if (prop instanceof Collection) {
              Collection<?> lo = (Collection) prop;
              for (Object o : lo) {

              }
            }
          }
          if (prop instanceof Map) {
            Map<?, ?> mp = (Map<?, ?>) prop;
            for (Map.Entry<?, ?> me : mp.entrySet()) {
              me.getValue();
              me.getKey();
            }
          }
        }

      }
    }
  }

  @Override
  public int restoreDb(EmgrFactory<?> fac, File file, RestoreMode restoreMode)
  {
    try {
      return restoreDb(fac, new FileInputStream(file), restoreMode);
    } catch (FileNotFoundException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  @Override
  public int restoreDb(EmgrFactory<?> fac, InputStream inputStream, RestoreMode restoreMode)
  {
    XStream xstream = new XStream();
    xstream.setMarshallingStrategy(new ReferenceByIdMarshallingStrategy()
    {
      @Override
      protected TreeUnmarshaller createUnmarshallingContext(Object root, HierarchicalStreamReader reader,
          ConverterLookup converterLookup, Mapper mapper)
      {
        return new XStreamReferenceByIdUnmarshaller(root, reader, converterLookup, mapper);
      }
    });

    XStreamRecordConverter recorder = new XStreamRecordConverter(xstream, fac);
    xstream.registerConverter(recorder, 10);
    xstream.registerConverter(new SkippUnkownElementsCollectionConverter(xstream.getMapper()),
        XStream.PRIORITY_VERY_HIGH);

    List<Object> objects = new ArrayList<>();
    Object result = xstream.fromXML(inputStream, objects);
    objects = (List<Object>) result;
    List<Object> recObjects = recorder.getAllEnties();
    XmlDumpRestoreContext ctx = createRestoreContext(fac, recObjects);
    LOG.info("Readed object from xml: " + objects.size());
    if (restoreMode == RestoreMode.InsertAll) {
      insertAll(fac, ctx);
    } else {
      throw new UnsupportedOperationException("restoreMode " + restoreMode + " currently not supported");
    }
    LOG.info("Imported entities: " + objects.size());
    return objects.size();
  }

  protected XmlDumpRestoreContext createRestoreContext(EmgrFactory<?> fac, List<Object> objects)
  {
    XmlDumpRestoreContext ctx = new XmlDumpRestoreContext(objects, this);
    objects.forEach((ent) -> {
      EntityMetadata entm = fac.getMetadataRepository().getEntityMetadata(ent.getClass());
      List<Object> list = ctx.getAllEntitiesByEntityMetadata().get(entm);
      if (list == null) {
        list = new ArrayList<>();
        ctx.getAllEntitiesByEntityMetadata().put(entm, list);
      }
      list.add(ent);
      Object id = entm.getIdColumn().getGetter().get(ent);
      Map<Object, Object> pkm = ctx.getOldPkToEntities().get(entm.getJavaType());
      if (pkm == null) {
        pkm = new HashMap<>();
        ctx.getOldPkToEntities().put(entm.getJavaType(), pkm);
      }
      pkm.put(id, ent);
    });
    return ctx;
  }

  protected void insertAll(EmgrFactory<?> fac, XmlDumpRestoreContext ctx)
  {
    List<Object> objects = ctx.getAllEntities();
    objects.forEach((el) -> clearPks(fac, el));
    List<EntityMetadata> ttableEnts = buildSortedEnties(fac.getMetadataRepository());
    List<EntityMetadata> tableEnts = filterSortTableEntities(ttableEnts);

    insertEntities(fac, ctx, objects, tableEnts);
  }

  protected void insertEntities(EmgrFactory<?> fac, XmlDumpRestoreContext ctx, List<Object> objects,
      List<EntityMetadata> tableEnts)
  {
    fac.runInTrans((emgr) -> {
      ctx.setEmgr(emgr);

      insertEntitiesInTrans(ctx, objects, tableEnts, emgr);
      return null;
    });
  }

  protected void insertEntitiesInTrans(XmlDumpRestoreContext ctx, List<Object> objects, List<EntityMetadata> tableEnts,
      IEmgr<?> emgr)
  {
    for (EntityMetadata em : tableEnts) {
      List<Object> ents = objects.stream().filter((e) -> em.getJavaType().isAssignableFrom(e.getClass()))
          .collect(Collectors.toList());
      ents = orderPersist(emgr, em, ents);
      for (Object obj : ents) {
        persist(ctx, em, obj);
      }
    }
  }

  protected List<EntityMetadata> buildSortedEnties(JpaMetadataRepostory nrepo)
  {
    List<EntityMetadata> unsortedTables = nrepo.getTableEntities();
    Set<EntityMetadata> remainsings = new HashSet<>(unsortedTables);
    List<EntityMetadata> sortedTables = new ArrayList<>();
    for (EntityMetadata table : unsortedTables) {
      addDepsFirst(nrepo, table, remainsings, sortedTables);
    }
    return sortedTables;
  }

  protected void addDepsFirst(JpaMetadataRepostory nrepo, EntityMetadata table, Set<EntityMetadata> remainsings,
      List<EntityMetadata> sortedTable)
  {
    if (remainsings.contains(table) == false) {
      return;
    }
    remainsings.remove(table);
    for (JpaXmlPersist jp : ClassUtils.findClassAnnotations(table.getJavaType(), JpaXmlPersist.class)) {
      for (Class<?> cls : jp.persistAfter()) {
        addDepsFirst(nrepo, nrepo.getEntityMetadata(cls), remainsings, sortedTable);
      }
    }
    for (EntityMetadata nt : table.getReferencesTo()) {
      addDepsFirst(nrepo, nt, remainsings, sortedTable);
    }
    sortedTable.add(table);
  }

  protected List<EntityMetadata> filterSortTableEntities(List<EntityMetadata> tables)
  {
    tables = filterNoStoreEntities(tables);
    return tables;
  }

  private List<EntityMetadata> filterNoStoreEntities(List<EntityMetadata> tables)
  {
    List<EntityMetadata> ret = tables.stream().filter((emd) -> {
      JpaXmlPersist jpa = emd.getJavaType().getAnnotation(JpaXmlPersist.class);
      if (jpa == null) {
        return true;
      }
      boolean nostore = jpa.noStore();
      return nostore == false;
    }).collect(Collectors.toList());
    return ret;
  }

  protected List<Object> orderPersist(IEmgr<?> emgr, EntityMetadata entityMetadata, List<Object> datas)
  {
    return datas;
  }

  @Override
  public Object persist(XmlDumpRestoreContext ctx, Object data)
  {
    if (ctx.isPersisted(data) == true) {
      return data;
    }
    EntityMetadata entityMetadata = ctx.getEmgr().getEmgrFactory().getMetadataRepository()
        .getEntityMetadata(data.getClass());
    return persist(ctx, entityMetadata, data);
  }

  @Override
  public Object persist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object entity)
  {
    if (ctx.isPersisted(entity) == true) {
      return entity;
    }
    Object rentity = preparePersist(ctx, entityMetadata, entity);
    if (rentity != null) {
      return rentity;
    }
    return store(ctx, entityMetadata, entity);
  }

  @Override
  public Object store(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object entity)
  {

    if (ctx.isPersisted(entity) == true) {
      return entity;
    }
    EntityManager entityManager = ctx.getEmgr().getEntityManager();
    ColumnMetadata idcol = entityMetadata.getIdColumn();
    boolean forcePersist = false;
    if (hasGeneratedId(idcol) == true) {
      Object pk = entityMetadata.getIdColumn().getGetter().get(entity);

      if (pk != null) {

        Object ret = entityManager.find(entityMetadata.getJavaType(), pk);
        if (ret == null) {
          LOG.info("Cannot find entity " + entityMetadata.getJavaType() + "(" + pk + ") persist it");
          forcePersist = true;
        } else {
          LOG.info(
              "Already Persisted " + entity.getClass().getName() + "("
                  + entityMetadata.getIdColumn().getGetter().get(entity) + ")");
          return ret;
        }
      }
    }
    if (entityManager.contains(entity) == true) {
      entityManager.merge(entity);
    } else {
      entityManager.persist(entity);
    }
    ctx.getPersistedObjects().put(entity, entity);
    LOG.info(
        "Persisted " + entity.getClass().getName() + "(" + entityMetadata.getIdColumn().getGetter().get(entity) + ")");
    return entity;
  }

  @Override
  public void flush(XmlDumpRestoreContext ctx)
  {
    ctx.getEmgr().getEntityManager().flush();
  }

  @Override
  public Object preparePersist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object data)
  {
    for (JpaXmlBeforePersistListener gl : getGlobalBeforeListener()) {
      Object p = gl.preparePersist(entityMetadata, data, ctx);
      if (p != null) {
        return p;
      }
    }
    List<JpaXmlPersist> anots = ClassUtils.findClassAnnotations(entityMetadata.getJavaType(), JpaXmlPersist.class);
    for (JpaXmlPersist anot : anots) {
      for (Class<? extends JpaXmlBeforePersistListener> clzz : anot.beforePersistListener()) {
        JpaXmlBeforePersistListener listener = createInstance(clzz);
        Object p = listener.preparePersist(entityMetadata, data, ctx);
        if (p != null) {
          return p;
        }
      }
    }
    return null;
  }

  protected <T> T createInstance(Class<T> clazz)
  {
    return PrivateBeanUtils.createInstance(clazz);
  }

  private boolean hasGeneratedId(ColumnMetadata colm)
  {
    GeneratedValue cv = colm.findAnnoation(GeneratedValue.class);
    return cv != null;
  }

  protected void clearPks(EmgrFactory<?> fac, Object obj)
  {
    EntityMetadata md = fac.getMetadataRepository().findEntityMetadata(obj.getClass());
    if (md == null) {
      return;
    }
    if (md.isTableEntity() == false) {
      return;
    }
    ColumnMetadata colm = md.getIdColumn();
    if (hasGeneratedId(colm) == true) {
      colm.getSetter().set(obj, null);
    }
  }

  @Override
  public List<JpaXmlBeforePersistListener> getGlobalBeforeListener()
  {
    return globalBeforeListener;
  }

}
