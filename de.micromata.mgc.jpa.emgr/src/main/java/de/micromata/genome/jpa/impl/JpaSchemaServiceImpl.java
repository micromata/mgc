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
import de.micromata.genome.jpa.JpaSchemaService;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.jpa.metainf.EntityMetadata;

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
    if (useDirect == true) {
      clearDatabaseDirect();
      return;
    }
    emfac.runInTrans((emgr) -> {
      List<Object> allEntries = new ArrayList<Object>();

      List<EntityMetadata> sortedTables = emfac.getMetadataRepository().getTableEntities();
      getNoneChildrenTables(); //
      LOG.info("Delete from tables: " + sortedTables);
      EntityManager em = emgr.getEntityManager();
      for (EntityMetadata table : sortedTables) {
        //        selectDeleteTablesRec(em, table, tables, allEntries);
        List<Object> entList = em.createQuery("select e  from " + table.getJavaType().getName() + " e").getResultList();
        LOG.info("Delete " + table + ": " + entList.size());
        allEntries.addAll(entList);
      }
      for (Object ent : allEntries) {
        em.remove(ent);
      }
      em.flush();
      LOG.info("Delete overall: " + allEntries.size());
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

  public void clearDatabaseDirect()
  {
    emfac.runInTrans((emgr) -> {

      List<EntityMetadata> sortedTables = emfac.getMetadataRepository().getTableEntities();
      LOG.info("Delete from tables: " + sortedTables);
      EntityManager em = emgr.getEntityManager();
      int count = 0;
      for (EntityMetadata table : sortedTables) {
        Query query = em.createQuery("delete  from " + table.getJavaType().getName() + " e");
        int updated = query.executeUpdate();
        LOG.info("Delete " + table + ": " + updated);
        count += updated;
      }

      em.flush();
      LOG.info("Delete overall: " + count);
      return null;
    });
  }

}
