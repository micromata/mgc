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

package de.micromata.genome.db.jpa.history.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.db.jpa.history.api.DiffEntry;
import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryEntry;
import de.micromata.genome.db.jpa.history.api.HistoryProperty;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyProvider;
import de.micromata.genome.db.jpa.history.api.HistoryService;
import de.micromata.genome.db.jpa.history.api.NoHistory;
import de.micromata.genome.db.jpa.history.api.WithHistory;
import de.micromata.genome.db.jpa.history.entities.EntityOpType;
import de.micromata.genome.db.jpa.history.entities.HistoryAttrBaseDO;
import de.micromata.genome.db.jpa.history.entities.HistoryMasterBaseDO;
import de.micromata.genome.db.jpa.history.entities.HistoryMasterDO;
import de.micromata.genome.db.jpa.history.entities.PropertyOpType;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.ClassUtils;

/**
 * Standard Implementation for History.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class HistoryServiceImpl implements HistoryService
{
  private static final Logger LOG = Logger.getLogger(HistoryServiceImpl.class);
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
  public Class<? extends HistoryMasterBaseDO<?, ?>> getHistoryMasterClass()
  {
    return HistoryMasterDO.class;
  }

  @Override
  public void registerEmgrListener(EmgrFactory<?> emgrFactory)
  {
    //    emgrFactory.getEventFactory().registerEvent(new HistoryUpdateEventHandler());
    emgrFactory.getEventFactory().registerEvent(new HistoryUpdateCopyFilterEventListener());
    emgrFactory.getEventFactory().registerEvent(new HistoryEmgrAfterInsertedEventHandler());
    emgrFactory.getEventFactory().registerEvent(new HistoryEmgrMarkDeletedCriteriaUpdateFilterEventHandler());
  }

  @Override
  public void registerStandardHistoryPropertyConverter(EmgrFactory<?> emgrFactory)
  {
    Map<Class<?>, Object> serviceCustomAttributes = emgrFactory
        .getMetadataRepository().getServiceCustomAttributes();
    Map<Class<?>, HistoryPropertyConverter> registry = (Map<Class<?>, HistoryPropertyConverter>) serviceCustomAttributes
        .get(HistoryPropertyProvider.class);
    if (registry == null) {
      registry = new HashMap<>();
      serviceCustomAttributes.put(HistoryPropertyProvider.class, registry);
    }
    ToStringPropertyConverter toStringPropertyConverter = new ToStringPropertyConverter();
    registry.put(Locale.class, toStringPropertyConverter);
    registry.put(TimeZone.class, toStringPropertyConverter);
  }

  private Map<Class<?>, HistoryPropertyConverter> getHistoryPropertyRegistry(IEmgr<?> emgr)
  {
    Map<Class<?>, Object> serviceCustomAttributes = emgr.getEmgrFactory()
        .getMetadataRepository().getServiceCustomAttributes();

    Map<Class<?>, HistoryPropertyConverter> registry = (Map<Class<?>, HistoryPropertyConverter>) serviceCustomAttributes
        .get(HistoryPropertyProvider.class);
    if (registry != null) {
      return registry;
    }
    return Collections.emptyMap();
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
  public Set<String> getNoHistoryProperties(EmgrFactory<?> emgr, Class<?> entity)
  {
    Set<String> ret = new HashSet<>();
    EntityMetadata ent = emgr.getMetadataRepository().getEntityMetadata(entity);
    if (ent == null) {
      LOG.warn("Cannot return noHistoryProperties, Class is not registerd as entity: " + entity);
      return ret;
    }
    for (ColumnMetadata cm : ent.getColumns().values()) {
      if (cm.findAnnoation(NoHistory.class) != null) {
        ret.add(cm.getName());
      }
    }
    return ret;
  }

  protected HistoryMasterBaseDO<?, ?> createHistoryMaster()
  {
    return PrivateBeanUtils.createInstance(getHistoryMasterClass());
  }

  @Override
  public void internalOnUpdate(IEmgr<?> emgr, String entityName, Serializable entityPk, Map<String, HistProp> oldProps,
      Map<String, HistProp> newProps)
  {
    HistoryMasterBaseDO<?, ?> hm = createHistoryMaster();
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
    if (entityPk == null) {
      return null;
    }
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
    HistoryMasterBaseDO<?, ?> hm = createHistoryMaster();
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

  @Override
  public void internalOnMarkUnmarkDeleted(IEmgr<?> emgr, EntityOpType opType, List<WithHistory> whanots, String name,
      Serializable entPk, Object ent)
  {
    HistoryMasterBaseDO<?, ?> hm = createHistoryMaster();
    hm.setEntityOpType(opType);
    hm.setEntityId(castToLong(entPk));
    hm.setEntityName(name);
    DiffEntry de = new DiffEntry();
    de.setPropertyName("deleted");
    String oldVype = opType == EntityOpType.Deleted ? "false" : "true";
    String newVype = opType != EntityOpType.Deleted ? "false" : "true";

    de.setOldProp(new HistProp("deleted", "boolean", oldVype));
    de.setNewProp(new HistProp("deleted", "boolean", newVype));
    de.setPropertyOpType(PropertyOpType.Update);
    putHistProp(hm, de);
    insert(emgr, hm);
  }

  @Override
  public void insertManualEntry(IEmgr<?> emgr, EntityOpType opType, String entityName, Serializable entPk, String user,
      String property, String propertyType, String oldValue, String newValue)
  {
    HistoryMasterBaseDO<?, ?> hm = createHistoryMaster();
    hm.setEntityOpType(opType);
    hm.setEntityId(castToLong(entPk));
    hm.setEntityName(entityName);
    DiffEntry de = new DiffEntry();
    de.setPropertyName(property);

    de.setOldProp(new HistProp(property, propertyType, oldValue));
    de.setNewProp(new HistProp(property, propertyType, newValue));
    de.setPropertyOpType(PropertyOpType.Update);
    putHistProp(hm, de);
    insert(emgr, hm);
  }

  public static void putHistProp(HistoryMasterBaseDO<?, ?> hm, DiffEntry de)
  {
    hm.putAttribute(de.getPropertyName() + OP_SUFFIX, de.getPropertyOpType().name());
    HistoryAttrBaseDO<?, ?> row = (HistoryAttrBaseDO<?, ?>) hm.getAttributeRow(de.getPropertyName() + OP_SUFFIX);
    row.setPropertyTypeClass(de.getPropertyOpType().getClass().getName());
    if (de.getNewProp() != null) {
      putHistProp(hm, de.getPropertyName(), NEWVAL_SUFFIX, de.getPropertyOpType(), de.getNewProp());
    }
    if (de.getOldProp() != null) {
      putHistProp(hm, de.getPropertyName(), OLDVAL_SUFFIX, de.getPropertyOpType(), de.getOldProp());
    }
  }

  private static void putHistProp(HistoryMasterBaseDO<?, ?> hm, String property, String suffix,
      PropertyOpType propertyOpType,
      HistProp histprop)
  {
    String key = property + suffix;
    hm.putAttribute(key, histprop.getValue());
    HistoryAttrBaseDO<?, ?> row = (HistoryAttrBaseDO<?, ?>) hm.getAttributeRow(key);
    row.setPropertyTypeClass(histprop.getType());
  }

  /**
   * Insert.
   *
   * @param hm the hm
   */
  protected void insert(IEmgr<?> emgr, HistoryMasterBaseDO<?, ?> hm)
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
  public List<DiffEntry> getDiffEntriesForHistoryMaster(HistoryMasterBaseDO<?, ?> historyMasterDO)
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

  private HistProp readHistProp(HistoryMasterBaseDO<?, ?> historyMasterDO, String key, String suffix)
  {
    HistoryAttrBaseDO<?, ?> row = (HistoryAttrBaseDO<?, ?>) historyMasterDO.getAttributeRow(key + suffix);
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
    Class<? extends HistoryMasterBaseDO<?, ?>> masterClass = getHistoryMasterClass();
    return emgr.selectDetached(masterClass, "select h from " + masterClass.getName()
        + " h where h.entityName = :entityName and h.entityId = :entityId", "entityName", entityName, "entityId",
        extPk);

  }

  @Override
  public List<? extends HistoryEntry> getHistoryEntriesForEntityClass(IEmgr<?> emgr, Class<? extends DbRecord<?>> cls)
  {
    Class<? extends HistoryMasterBaseDO<?, ?>> masterClass = getHistoryMasterClass();
    return emgr.selectDetached(masterClass,
        "select h from " + masterClass.getName() + " h where h.entityName = :entityName", "entityName",
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
    Class<? extends HistoryMasterBaseDO<?, ?>> masterClass = getHistoryMasterClass();
    return emgr.deleteFromQuery(masterClass,
        "select h from " + masterClass.getName() + " h where h.entityName = :entityName", "entityName",
        cls.getName());

  }

  private HistoryPropertyConverter findPropertyConverterFromRegistered(IEmgr<?> emgr, Object entity, ColumnMetadata pd)
  {
    Class<?> propClass = pd.getJavaType();
    Map<Class<?>, HistoryPropertyConverter> regmap = getHistoryPropertyRegistry(emgr);
    HistoryPropertyConverter conv = regmap.get(propClass);
    if (conv != null) {
      return conv;
    }
    for (Map.Entry<Class<?>, HistoryPropertyConverter> me : regmap.entrySet()) {
      if (me.getKey().isAssignableFrom(propClass) == true) {
        return me.getValue();
      }
    }
    return null;
  }

  @Override
  public HistoryPropertyConverter getPropertyConverter(IEmgr<?> emgr, Object entity, ColumnMetadata pd)
  {
    HistoryProperty annot = pd.findAnnoation(HistoryProperty.class);
    if (annot != null) {
      return PrivateBeanUtils.createInstance(annot.converter());
    }
    HistoryPropertyConverter conv = findPropertyConverterFromRegistered(emgr, entity, pd);
    if (conv != null) {
      return conv;
    }
    Class<?> pclazz = pd.getJavaType();

    if (DbRecord.class.isAssignableFrom(pclazz) == true) {
      return new DbRecordPropertyConverter();
    } else if (Map.class.isAssignableFrom(pclazz) == true) {
      LOG.fatal("Currenty not supported Map for History: " + entity.getClass() + "." + pd.getName());
    } else if (Collection.class.isAssignableFrom(pclazz) == true) {
      return new CollectionPropertyConverter();
    }

    return new SimplePropertyConverter();
  }

  @Override
  public boolean hasHistory(Class<?> entityClass)
  {
    List<WithHistory> whl = ClassUtils.findClassAnnotations(entityClass, WithHistory.class);
    return whl.isEmpty() == false;
  }
}
