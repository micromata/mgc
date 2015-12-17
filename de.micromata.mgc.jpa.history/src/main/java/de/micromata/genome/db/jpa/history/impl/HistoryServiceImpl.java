package de.micromata.genome.db.jpa.history.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.db.jpa.history.api.DiffEntry;
import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryEntry;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyProvider;
import de.micromata.genome.db.jpa.history.api.HistoryService;
import de.micromata.genome.db.jpa.history.api.WithHistory;
import de.micromata.genome.db.jpa.history.entities.EntityOpType;
import de.micromata.genome.db.jpa.history.entities.HistoryAttrDO;
import de.micromata.genome.db.jpa.history.entities.HistoryMasterDO;
import de.micromata.genome.db.jpa.history.entities.PropertyOpType;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.util.runtime.ClassUtils;

/**
 * Standard Implementation for History.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class HistoryServiceImpl implements HistoryService
{

  /**
   * The Constant OP_SUFFIX.
   */
  private static final String OP_SUFFIX = ":op";

  /**
   * The Constant OLDVAL_SUFFIX.
   */
  private static final String OLDVAL_SUFFIX = ":ov";

  /**
   * The Constant NEWVAL_SUFFIX.
   */
  private static final String NEWVAL_SUFFIX = ":nv";

  @Override
  public void registerEmgrListener(EmgrFactory<?> emgrFactory)
  {
    //    emgrFactory.getEventFactory().registerEvent(new HistoryUpdateEventHandler());
    emgrFactory.getEventFactory().registerEvent(new HistoryUpdateCopyFilterEventListener());
    emgrFactory.getEventFactory().registerEvent(new HistoryEmgrAfterInsertedEventHandler());

  }

  @Override
  public Map<String, HistProp> internalGetPropertiesForHistory(IEmgr<?> emgr, List<WithHistory> whanot, Object bean)
  {
    Map<String, HistProp> ret = new TreeMap<>();
    HistoryMetaInfo historyMetaInfo = createHistMetaInfo(whanot);
    for (WithHistory wh : whanot) {
      for (Class<? extends HistoryPropertyProvider> provider : wh.propertyProvider()) {
        HistoryPropertyProvider prov = createProvider(provider);
        prov.getProperties(emgr, historyMetaInfo, bean, ret);
      }
    }
    return ret;
  }

  @Override
  public void internalOnUpdate(IEmgr<?> emgr, String entityName, Serializable entityPk, Map<String, HistProp> oldProps,
      Map<String, HistProp> newProps)
  {
    HistoryMasterDO hm = new HistoryMasterDO();
    hm.setEntityOpType(EntityOpType.Update);
    hm.setEntityId(castToLong(entityPk));
    hm.setEntityName(entityName);

    List<DiffEntry> difflist = calculateDiff(oldProps, newProps);
    for (DiffEntry de : difflist) {
      putHistProp(hm, de);

    }
    if (hm.getAttributeKeys().isEmpty() == true) {
      return;
    }
    insert(emgr, hm);
  }

  /**
   * Creates the hist meta info.
   *
   * @param whanot the whanot
   * @return the history meta info
   */
  private HistoryMetaInfo createHistMetaInfo(List<WithHistory> whanot)
  {
    Set<String> ignoreProperties = new HashSet<>();
    for (WithHistory wh : whanot) {
      if (wh.noHistoryProperties() != null) {
        for (String nh : wh.noHistoryProperties()) {
          ignoreProperties.add(nh);
        }
      }
    }
    for (WithHistory wh : whanot) {
      if (wh.forceHistoryProperties() != null) {
        for (String nh : wh.forceHistoryProperties()) {
          ignoreProperties.remove(nh);
        }
      }
    }
    HistoryMetaInfo ninf = new HistoryMetaInfo()
    {
      @Override
      public boolean ignoreProperty(String property)
      {
        return ignoreProperties.contains(property);
      }

    };
    return ninf;
  }

  private Long castToLong(Serializable entityPk)
  {
    if (entityPk instanceof Long) {
      return (Long) entityPk;
    }
    if (entityPk instanceof Number) {
      return ((Number) entityPk).longValue();
    }
    throw new IllegalArgumentException("Pk is not a number: " + entityPk.getClass().getName());
  }

  @Override
  public void internalOnInsert(IEmgr<?> emgr, List<WithHistory> whanot, String entityName, Serializable entityPk,
      Object ent)
  {
    Map<String, HistProp> prevMap = new TreeMap<>();
    Map<String, HistProp> nextMap = new TreeMap<>();
    HistoryMetaInfo historyMetaInfo = createHistMetaInfo(whanot);
    for (WithHistory wh : whanot) {
      for (Class<? extends HistoryPropertyProvider> provider : wh.propertyProvider()) {
        HistoryPropertyProvider prov = createProvider(provider);
        prov.getProperties(emgr, historyMetaInfo, ent, nextMap);
      }

    }
    HistoryMasterDO hm = new HistoryMasterDO();
    hm.setEntityOpType(EntityOpType.Insert);
    hm.setEntityId(castToLong(entityPk));
    hm.setEntityName(entityName);

    List<DiffEntry> difflist = calculateDiff(prevMap, nextMap);
    for (DiffEntry de : difflist) {
      putHistProp(hm, de);

    }
    if (hm.getAttributeKeys().isEmpty() == true) {
      return;
    }
    insert(emgr, hm);
  }

  private void putHistProp(HistoryMasterDO hm, DiffEntry de)
  {
    hm.putAttribute(de.getPropertyName() + OP_SUFFIX, de.getPropertyOpType().name());
    HistoryAttrDO row = (HistoryAttrDO) hm.getAttributeRow(de.getPropertyName() + OP_SUFFIX);
    row.setPropertyTypeClass(de.getPropertyOpType().getClass().getName());
    if (de.getNewProp() != null) {
      putHistProp(hm, de.getPropertyName(), NEWVAL_SUFFIX, de.getPropertyOpType(), de.getNewProp());
    }
    if (de.getOldProp() != null) {
      putHistProp(hm, de.getPropertyName(), OLDVAL_SUFFIX, de.getPropertyOpType(), de.getOldProp());
    }
  }

  private void putHistProp(HistoryMasterDO hm, String property, String suffix, PropertyOpType propertyOpType,
      HistProp histprop)
  {
    String key = property + suffix;
    hm.putAttribute(key, histprop.getValue());
    HistoryAttrDO row = (HistoryAttrDO) hm.getAttributeRow(key);
    row.setPropertyTypeClass(histprop.getType());

  }

  /**
   * Insert.
   *
   * @param hm the hm
   */
  protected void insert(IEmgr<?> emgr, HistoryMasterDO hm)
  {
    emgr.insertDetached(hm);
  }

  /**
   * Calculate diff.
   *
   * @param prevMap the prev map
   * @param nextMap the next map
   * @return the list
   */
  protected List<DiffEntry> calculateDiff(Map<String, HistProp> prevMap, Map<String, HistProp> nextMap)
  {
    Set<String> allKeys = new TreeSet<>();
    allKeys.addAll(prevMap.keySet());
    allKeys.addAll(nextMap.keySet());
    List<DiffEntry> ret = new ArrayList<>();
    for (String key : allKeys) {
      HistProp oldVal = prevMap.get(key);
      HistProp newVal = nextMap.get(key);
      DiffEntry de = calculateDiff(oldVal, newVal);
      if (de != null) {
        de.setPropertyName(key);
        ret.add(de);
      }
    }
    return ret;

  }

  /**
   * Calculate diff.
   *
   * @param oldVal the old val
   * @param newVal the new val
   * @return the diff entry
   */
  protected DiffEntry calculateDiff(HistProp oldProp, HistProp newProp)
  {
    DiffEntry ret = new DiffEntry();
    String oldVal = oldProp == null ? null : oldProp.getValue();
    String newVal = newProp == null ? null : newProp.getValue();
    ret.setNewProp(newProp);
    ret.setOldProp(oldProp);
    if (oldVal == newVal) {
      return null;
    }
    // for dbs blank == leer == null.
    if (oldVal == null && StringUtils.isBlank(newVal) == true) {
      return null;
    }
    if (newVal == null && StringUtils.isBlank(oldVal) == true) {
      return null;
    }
    if (oldVal == null) {
      ret.setPropertyOpType(PropertyOpType.Insert);
      return ret;
    }
    if (newVal == null) {
      ret.setPropertyOpType(PropertyOpType.Delete);
      return ret;
    }
    String oldsval = oldVal;
    String newsval = newVal;
    if (StringUtils.equals(oldsval, newsval) == true) {
      return null;
    }
    ret.setPropertyOpType(PropertyOpType.Update);
    return ret;
  }

  /**
   * Creates the provider.
   *
   * @param provider the provider
   * @return the history property provider
   */
  private HistoryPropertyProvider createProvider(Class<? extends HistoryPropertyProvider> provider)
  {
    try {
      return provider.newInstance();
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new LoggedRuntimeException(LogLevel.Fatal, GenomeLogCategory.Coding,
          "Cannot instatiate HistoryPropertyProvider: " + ex.getMessage(), new LogExceptionAttribute(ex));
    }
  }

  @Override
  public List<DiffEntry> getDiffEntriesForHistoryMaster(HistoryMasterDO historyMasterDO)
  {
    List<DiffEntry> entries = new ArrayList<>();
    for (String key : historyMasterDO.getAttributeKeys()) {
      if (key.endsWith(OP_SUFFIX) == false) {
        continue;
      }
      DiffEntry ne = new DiffEntry();

      String propName = key.substring(0, key.length() - OP_SUFFIX.length());
      ne.setPropertyName(propName);
      ne.setPropertyOpType(PropertyOpType.fromString(historyMasterDO.getStringAttribute(key)));
      HistProp oldProp = readHistProp(historyMasterDO, propName, OLDVAL_SUFFIX);
      ne.setOldProp(oldProp);
      HistProp newProp = readHistProp(historyMasterDO, propName, NEWVAL_SUFFIX);
      ne.setNewProp(newProp);
      entries.add(ne);
    }
    return entries;
  }

  private HistProp readHistProp(HistoryMasterDO historyMasterDO, String key, String suffix)
  {
    HistoryAttrDO row = (HistoryAttrDO) historyMasterDO.getAttributeRow(key + suffix);
    if (row == null) {
      return null;
    }
    HistProp histprop = new HistProp();
    histprop.setName(key);
    histprop.setValue(row.getValue());
    histprop.setType(row.getPropertyTypeClass());
    return histprop;
  }

  @Override
  public List<? extends HistoryEntry> getHistoryEntries(IEmgr<?> emgr, DbRecord<?> stdRecord)
  {
    return getHistoryEntries(emgr, stdRecord.getClass().getName(), stdRecord.getPk());
  }

  @Override
  public List<? extends HistoryEntry> getHistoryEntries(IEmgr<?> emgr, String entityName, Serializable entityId)
  {
    Long extPk = castToLong(entityId);

    return emgr.selectDetached(HistoryMasterDO.class, "select h from " + HistoryMasterDO.class.getName()
        + " h where h.entityName = :entityName and h.entityId = :entityId", "entityName", entityName, "entityId",
        extPk);

  }

  @Override
  public List<? extends HistoryEntry> getHistoryEntriesForEntityClass(IEmgr<?> emgr, Class<? extends DbRecord<?>> cls)
  {
    return emgr.selectDetached(HistoryMasterDO.class,
        "select h from " + HistoryMasterDO.class.getName() + " h where h.entityName = :entityName", "entityName",
        cls.getName());

  }

  /**
   * return null if not an Hist
   * 
   * @param entity
   * @return
   */

  @Override
  public List<WithHistory> internalFindWithHistoryEntity(Object entity)
  {
    if ((entity instanceof DbRecord) == false) {
      return Collections.emptyList();
    }
    if (((DbRecord) entity).getPk() == null) {
      return Collections.emptyList();
    }
    Class<?> entClass = entity.getClass();
    List<WithHistory> whanots = ClassUtils.findClassAnnotations(entClass, WithHistory.class);
    return whanots;
  }

  @Override
  public int clearHistoryForEntityClass(IEmgr<?> emgr, Class<? extends DbRecord<?>> cls)
  {
    return emgr.deleteFromQuery(HistoryMasterDO.class,
        "select h from " + HistoryMasterDO.class.getName() + " h where h.entityName = :entityName", "entityName",
        cls.getName());

  }
}
