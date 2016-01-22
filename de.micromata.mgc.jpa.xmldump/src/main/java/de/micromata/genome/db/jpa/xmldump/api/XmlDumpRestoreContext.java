package de.micromata.genome.db.jpa.xmldump.api;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.micromata.genome.db.jpa.xmldump.impl.XmlJpaPersistService;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * The Class XmlDumpRestoreContext.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class XmlDumpRestoreContext
{
  private static final Logger LOG = Logger.getLogger(XmlDumpRestoreContext.class);
  private List<Object> allEntities;

  private Map<EntityMetadata, List<Object>> allEntitiesByEntityMetadata = new HashMap<>();

  private Map<Object, Object> persistedObjects = new IdentityHashMap<>();
  private Map<Object, Object> oldPkToEntities = new HashMap<>();
  private IEmgr<?> emgr;
  private XmlJpaPersistService persistService;

  public XmlJpaPersistService getPersistService()
  {
    return persistService;
  }

  public XmlDumpRestoreContext(List<Object> allEntities, XmlJpaPersistService persistService)
  {
    this.allEntities = allEntities;
    this.persistService = persistService;
  }

  public boolean isPersisted(Object entity)
  {
    return persistedObjects.containsKey(entity);
  }

  public List<Object> getAllEntities()
  {
    return allEntities;
  }

  public Map<Object, Object> getPersistedObjects()
  {
    return persistedObjects;
  }

  public Map<Object, Object> getOldPkToEntities()
  {
    return oldPkToEntities;
  }

  public Map<EntityMetadata, List<Object>> getAllEntitiesByEntityMetadata()
  {
    return allEntitiesByEntityMetadata;
  }

  public IEmgr<?> getEmgr()
  {
    return emgr;
  }

  public void setEmgr(IEmgr<?> emgr)
  {
    this.emgr = emgr;
  }

}
