package de.micromata.genome.jpa.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.JpaSchemaService;
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
    emfac.runInTrans((emgr) -> {
      List<Object> allEntries = new ArrayList<Object>();
      List<EntityMetadata> sortedTables = emfac.getMetadataRepository().getTableEntities();
      //      Set<EntityMetadata> tables = new HashSet<>(emfac.getMetadataRepository().getTableEntities());
      //      Collections.reverse(tables);
      //      List<EntityMetadata> tablesToDelete = new ArrayList<>(tables);
      LOG.info("Delete from tables: " + sortedTables);
      EntityManager em = emgr.getEntityManager();
      for (EntityMetadata table : sortedTables) {
        //        selectDeleteTablesRec(em, table, tables, allEntries);
        List<Object> entList = em.createQuery("select e from " + table.getJavaType().getName() + " e").getResultList();
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

  private void selectDeleteTablesRec(EntityManager em, EntityMetadata table, Set<EntityMetadata> tables,
      List<Object> toDeleteEntities)
  {
    if (tables.contains(table) == false) {
      return;
    }
    tables.remove(table);
    for (EntityMetadata nt : table.getReferencedBy()) {
      selectDeleteTablesRec(em, nt, tables, toDeleteEntities);
    }
    toDeleteEntities.addAll(em.createQuery("select e from " + table.getJavaType().getName() + " e").getResultList());
  }
}
