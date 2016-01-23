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

  /**
   * The Constant LOG.
   */
  private static final Logger LOG = Logger.getLogger(XmlDumpRestoreContext.class);

  /**
   * The all entities.
   */
  private List<Object> allEntities;

  /**
   * The all entities by entity metadata.
   */
  private Map<EntityMetadata, List<Object>> allEntitiesByEntityMetadata = new HashMap<>();

  /**
   * The persisted objects.
   */
  private Map<Object, Object> persistedObjects = new IdentityHashMap<>();

  /**
   * The old pk to entities.
   */
  private Map<Class<?>, Map<Object, Object>> oldPkToEntities = new HashMap<>();

  /**
   * The emgr.
   */
  private IEmgr<?> emgr;

  /**
   * The persist service.
   */
  private XmlJpaPersistService persistService;

  /**
   * Gets the persist service.
   *
   * @return the persist service
   */
  public XmlJpaPersistService getPersistService()
  {
    return persistService;
  }

  /**
   * Instantiates a new xml dump restore context.
   *
   * @param allEntities the all entities
   * @param persistService the persist service
   */
  public XmlDumpRestoreContext(List<Object> allEntities, XmlJpaPersistService persistService)
  {
    this.allEntities = allEntities;
    this.persistService = persistService;
  }

  /**
   * Checks if is persisted.
   *
   * @param entity the entity
   * @return true, if is persisted
   */
  public boolean isPersisted(Object entity)
  {
    return persistedObjects.containsKey(entity);
  }

  public <T> T findEntityByOldPk(Object oldPk, Class<T> entityClass)
  {
    Map<Object, Object> pkm = oldPkToEntities.get(entityClass);
    if (pkm == null) {
      return null;
    }
    return (T) pkm.get(oldPk);
  }

  public <PK> PK findNewPkForOldPk(Object oldPk, Class<?> entityClass, Class<PK> pktype)
  {
    Object entity = findEntityByOldPk(oldPk, entityClass);
    if (entity == null) {
      return null;
    }
    EntityMetadata em = getEmgr().getEmgrFactory().getMetadataRepository().getEntityMetadata(entityClass);
    Object id = em.getIdColumn().getGetter().get(entity);
    return (PK) id;
  }

  public EntityMetadata findEntityMetaData(Class<?> entityClazz)
  {
    return emgr.getEmgrFactory().getMetadataRepository().findEntityMetadata(entityClazz);
  }

  /**
   * Gets the all entities.
   *
   * @return the all entities
   */
  public List<Object> getAllEntities()
  {
    return allEntities;
  }

  /**
   * Gets the persisted objects.
   *
   * @return the persisted objects
   */
  public Map<Object, Object> getPersistedObjects()
  {
    return persistedObjects;
  }

  public Map<Class<?>, Map<Object, Object>> getOldPkToEntities()
  {
    return oldPkToEntities;
  }

  /**
   * Gets the all entities by entity metadata.
   *
   * @return the all entities by entity metadata
   */
  public Map<EntityMetadata, List<Object>> getAllEntitiesByEntityMetadata()
  {
    return allEntitiesByEntityMetadata;
  }

  /**
   * Gets the emgr.
   *
   * @return the emgr
   */
  public IEmgr<?> getEmgr()
  {
    return emgr;
  }

  /**
   * Sets the emgr.
   *
   * @param emgr the new emgr
   */
  public void setEmgr(IEmgr<?> emgr)
  {
    this.emgr = emgr;
  }

}
