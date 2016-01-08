package de.micromata.genome.jpa.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
      for (EntityMetadata em : emfac.getMetadataRepository().getTableEntities()) {
        List<?> entl = emgr.selectAllAttached(em.getJavaType());
        LOG.info("Delete from " + em + ": " + entl.size());
        allEntries.addAll(entl);
      }
      for (Object ent : allEntries) {
        emgr.getEntityManager().remove(ent);
      }
      emgr.getEntityManager().flush();
      LOG.info("Delete overall: " + allEntries.size());
      return null;
    });
  }
}
