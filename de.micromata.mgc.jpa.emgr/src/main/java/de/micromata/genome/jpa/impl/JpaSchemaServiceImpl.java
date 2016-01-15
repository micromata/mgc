package de.micromata.genome.jpa.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.JpaSchemaService;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * Standard implementation
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JpaSchemaServiceImpl implements JpaSchemaService
{
  private static final Logger LOG = Logger.getLogger(JpaSchemaServiceImpl.class);
  protected final EmgrFactory<?> emfac;

  public JpaSchemaServiceImpl(EmgrFactory<?> emfac)
  {
    this.emfac = emfac;
  }

  @Override
  public void exportCreateSchemaToFile(String fileName)
  {
    Map<String, String> props = new HashMap<>();
    props.put("javax.persistence.schema-generation.scripts.action", "create");
    props.put("javax.persistence.schema-generation.scripts.create-target", fileName);
    Persistence.generateSchema(emfac.getUnitName(), props);

  }

  @Override
  public void exportDropSchemaToFile(String fileName)
  {
    Map<String, String> props = new HashMap<>();
    props.put("javax.persistence.schema-generation.scripts.action", "drop");
    props.put("javax.persistence.schema-generation.scripts.drop-target", fileName);
    Persistence.generateSchema(emfac.getUnitName(), props);
  }

  @Override
  public void clearDatabase()
  {
    boolean useDirect = false;
    boolean useTruncate = false;
    if (useDirect == true) {
      clearDatabaseDirect();
      return;
    }
    if (useTruncate == true) {
      clearDatabaseWithTruncate();
      return;
    }
    List<EntityMetadata> sortedTables = emfac.getMetadataRepository().getTableEntities();
    //    try {
    emfac.runInTrans((emgr) -> {
      clearManyToManies(emgr, sortedTables);

      List<Object> allEntries = new ArrayList<Object>();

      //        getNoneChildrenTables(); //
      LOG.info("Delete from tables: " + sortedTables);
      EntityManager em = emgr.getEntityManager();
      for (EntityMetadata table : sortedTables) {
        //        selectDeleteTablesRec(em, table, tables, allEntries);
        ATableTruncater atrun = table.getJavaType().getAnnotation(ATableTruncater.class);
        if (atrun != null) {
          TableTruncater trun = PrivateBeanUtils.createInstance(atrun.value());
          int count = trun.truncateTable(emgr, table);
          if (count > 0) {
            LOG.info("Delete " + table + ": " + count);
          }
        } else {
          List<Object> entList = em.createQuery("select e  from " + table.getJavaType().getName() + " e")
              .getResultList();
          if (entList.isEmpty() == false) {
            LOG.info("Delete " + table + ": " + entList.size());
          }
          allEntries.addAll(entList);
        }
      }
      for (Object ent : allEntries) {
        em.remove(ent);

      }
      em.flush();
      LOG.info("Delete overall: " + allEntries.size());
      return null;
    });
    checkAllTablesEmpty(sortedTables);
    //    } catch (OptimisticLockException ex) {
    //      checkAllTablesEmpty(sortedTables);
    //    }
  }

  private void clearManyToManies(IEmgr<?> emgr, List<EntityMetadata> sortedTables)
  {

  }

  private void checkAllTablesEmpty(List<EntityMetadata> sortedTables)
  {
    emfac.runWoTrans((emgr) -> {
      long count = 0;
      for (EntityMetadata em : sortedTables) {
        long tableres = emgr
            .createQueryAttached(Long.class, "select count(*) from " + em.getJavaType().getName() + " e")
            .getSingleResult();
        if (tableres > 0) {
          LOG.warn("Table has still records: " + em.getJavaType() + "; count: " + tableres);
        }
        count += tableres;
      }
      return null;
    });

  }

  private boolean hasWithDeleteAssocation(EntityMetadata master, EntityMetadata detail)
  {
    for (ColumnMetadata cm : master.getColumns().values()) {
      if (cm.getTargetEntity() != detail) {
        continue;
      }
      if (cm.isAssociation() == false) {
        continue;
      }
      OneToMany om = cm.findAnnoation(OneToMany.class);
      if (om != null) {
        if (om.orphanRemoval() == true) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isStrictChildren(EntityMetadata table)
  {

    for (ColumnMetadata cm : table.getColumns().values()) {
      if (cm.isNullable() == true || cm.isAssociation() == false) {
        continue;
      }
      if (cm.isCollection() == true) {
        continue;
      }
      EntityMetadata ta = cm.getTargetEntity();
      if (hasWithDeleteAssocation(ta, table) == true) {
        return true;
      }
    }
    return false;
  }

  List<EntityMetadata> getNoneChildrenTables()
  {
    List<EntityMetadata> sortedTables = emfac.getMetadataRepository().getTableEntities();

    List<EntityMetadata> ret = new ArrayList<>();
    for (EntityMetadata em : sortedTables) {
      if (isStrictChildren(em) == true) {
        continue;
      }
      ret.add(em);
    }
    return ret;
  }

  public void clearDatabaseWithTruncate()
  {
    emfac.runInTrans((emgr) -> {

      List<EntityMetadata> sortedTables = emfac.getMetadataRepository().getTableEntities();

      EntityManager em = emgr.getEntityManager();
      for (EntityMetadata table : sortedTables) {
        em.createNativeQuery("truncate table " + table.getDatabaseName()).executeUpdate();
      }

      em.flush();
      LOG.info("trucated tables");
      return null;
    });
  }

  public void clearDatabaseDirect()
  {
    emfac.runInTrans((emgr) -> {

      List<EntityMetadata> sortedTables = emfac.getMetadataRepository().getTableEntities();

      EntityManager em = emgr.getEntityManager();
      int count = 0;
      for (EntityMetadata table : sortedTables) {
        Query query = em.createQuery("delete  from " + table.getJavaType().getName() + " e");
        int updated = query.executeUpdate();
        if (updated > 0) {
          LOG.info("Delete " + table + ": " + updated);
        }
        count += updated;
      }

      em.flush();
      LOG.info("Delete overall: " + count);
      return null;
    });
  }

}
