package de.micromata.genome.db.jpa.xmldump.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.AttributeNode;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;

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
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.RuntimeIOException;

/**
 * The Class JpaXmlDumpServiceImpl.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JpaXmlDumpServiceImpl implements JpaXmlDumpService, XmlJpaPersistService
{
  private static final Logger LOG = Logger.getLogger(JpaXmlDumpServiceImpl.class);

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
  public void dumpToXml(EmgrFactory<?> fac, final Writer writer)
  {
    fac.runWoTrans((emgr) -> {
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
    // TODO Auto-generated method stub

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
  public void restoreDb(EmgrFactory<?> fac, File file, RestoreMode restoreMode)
  {
    try {
      restoreDb(fac, new FileInputStream(file), restoreMode);
    } catch (FileNotFoundException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  @Override
  public void restoreDb(EmgrFactory<?> fac, InputStream inputStream, RestoreMode restoreMode)
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

    List<Object> objects = new ArrayList<>();
    Object result = xstream.fromXML(inputStream, objects);
    objects = (List<Object>) result;
    XmlDumpRestoreContext ctx = createRestoreContext(fac, objects);
    LOG.info("Readed object from xml: " + objects.size());
    if (restoreMode == RestoreMode.InsertAll) {
      insertAll(fac, ctx);
    }

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
      ctx.getOldPkToEntities().put(id, ent);
    });
    return ctx;
  }

  protected void insertAll(EmgrFactory<?> fac, XmlDumpRestoreContext ctx)
  {
    List<Object> objects = ctx.getAllEntities();
    objects.forEach((el) -> clearPks(fac, el));
    List<EntityMetadata> tableEnts = fac.getMetadataRepository().getTableEntities();
    //    Collections.reverse(tableEnts);
    fac.runInTrans((emgr) -> {
      ctx.setEmgr(emgr);

      for (EntityMetadata em : tableEnts) {
        EntityGraph<?> entgraph = emgr.getEntityManager().createEntityGraph(em.getJavaType());
        List<AttributeNode<?>> nodes = entgraph.getAttributeNodes();
        List<Object> ents = objects.stream().filter((e) -> em.getJavaType().isAssignableFrom(e.getClass()))
            .collect(Collectors.toList());
        ents = orderPersist(emgr, em, ents);
        for (Object obj : ents) {
          persist(ctx, em, obj);
        }
      }
      return null;
    });
  }

  protected List<Object> orderPersist(IEmgr<?> emgr, EntityMetadata entityMetadata, List<Object> datas)
  {
    return datas;
  }

  @Override
  public void persist(XmlDumpRestoreContext ctx, Object data)
  {
    if (ctx.isPersisted(data) == true) {
      return;
    }
    EntityMetadata entityMetadata = ctx.getEmgr().getEmgrFactory().getMetadataRepository()
        .getEntityMetadata(data.getClass());
    persist(ctx, entityMetadata, data);
  }

  @Override
  public void persist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object entity)
  {
    if (ctx.isPersisted(entity) == true) {
      return;
    }
    if (preparePersist(ctx, entityMetadata, entity) == false) {
      return;
    }

    if (ctx.isPersisted(entity) == true) {
      return;
    }
    EntityManager entityManager = ctx.getEmgr().getEntityManager();
    Object pk = entityMetadata.getIdColumn().getGetter().get(entity);
    if (pk != null) {
      LOG.info(
          "Already Persisted " + entity.getClass().getName() + "("
              + entityMetadata.getIdColumn().getGetter().get(entity) + ")");
      return;
    }
    if (entityManager.contains(entity) == true) {
      entityManager.merge(entity);
    } else {
      entityManager.persist(entity);
    }
    ctx.getPersistedObjects().put(entity, entity);
    LOG.info(
        "Persisted " + entity.getClass().getName() + "(" + entityMetadata.getIdColumn().getGetter().get(entity) + ")");

  }

  @Override
  public boolean preparePersist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object data)
  {
    JpaXmlPersist anot = entityMetadata.getJavaType().getAnnotation(JpaXmlPersist.class);
    if (anot == null) {
      return true;
    }
    for (Class<? extends JpaXmlBeforePersistListener> clzz : anot.beforePersistListener()) {
      JpaXmlBeforePersistListener listener = PrivateBeanUtils.createInstance(clzz);
      if (listener.preparePersist(entityMetadata, data, ctx) == false) {
        return false;
      }
    }
    return true;
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
    colm.getSetter().set(obj, null);
  }
}
