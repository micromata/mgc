package de.micromata.genome.db.jpa.history.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.micromata.genome.db.jpa.history.entities.EntityOpType;
import de.micromata.genome.db.jpa.history.entities.HistoryMasterBaseDO;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;

/**
 * The Interface HistoryService.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface HistoryService
{

  /**
   * The entity class. The entity class must provide default constructor.
   *
   * @return the history master class
   */
  Class<? extends HistoryMasterBaseDO<?, ?>> getHistoryMasterClass();

  /**
   * Register the listener to Emgr. Should be called while initializing EmgrFactory in overwritten
   * de.micromata.genome.jpa.EmgrFactory.registerEvents().
   * 
   * @param emgrFactory the emgr factory
   */
  void registerEmgrListener(EmgrFactory<?> emgrFactory);

  /**
   * Should be called while initializing EmgrFactory in overwritten
   * de.micromata.genome.jpa.EmgrFactory.registerEvents().
   *
   * @param emgrFactory the emgr factory
   */
  void registerStandardHistoryPropertyConverter(EmgrFactory<?> emgrFactory);

  /**
   * Internal get properties for history.
   *
   * @param emgr the emgr
   * @param whanot the whanot
   * @param bean the bean
   * @return the map
   */
  Map<String, HistProp> internalGetPropertiesForHistory(IEmgr<?> emgr, List<WithHistory> whanot, Object bean);

  /**
   * Return a set of property names, which are marked with @NoHistory.
   *
   * @param entity the entity
   * @return the non history properties
   */
  Set<String> getNoHistoryProperties(EmgrFactory<?> emgr, Class<?> entity);

  /**
   * Internal on update.
   *
   * @param emgr the emgr
   * @param entityName the entity name
   * @param entityPk the entity pk
   * @param oldProps the old props
   * @param newProps the new props
   */
  void internalOnUpdate(IEmgr<?> emgr, String entityName, Serializable entityPk, Map<String, HistProp> oldProps,
      Map<String, HistProp> newProps);

  /**
   * On insert.
   *
   * @param emgr the emgr
   * @param whanot the whanot
   * @param entityName the entity name
   * @param entityPk the entity pk
   * @param ent the ent
   */
  void internalOnInsert(IEmgr<?> emgr, List<WithHistory> whanot, String entityName, Serializable entityPk, Object ent);

  /**
   * Internal on mark deleted.
   *
   * @param emgr the emgr
   * @param whanots the whanots
   * @param name the name
   * @param entPk the ent pk
   * @param ent the ent
   */
  void internalOnMarkUnmarkDeleted(IEmgr<?> emgr, EntityOpType opType, List<WithHistory> whanots, String name,
      Serializable entPk, Object ent);

  void insertManualEntry(IEmgr<?> emgr, EntityOpType opType, String entityName, Serializable entPk, String user,
      String property, String propertyType, String oldValue, String newValue);

  /**
   * Internal API to find entity with history. If return is empty, no history should be done for this entity.
   *
   * @param entity the entity
   * @return the list
   */
  List<WithHistory> internalFindWithHistoryEntity(Object entity);

  /**
   * Internal creates.
   *
   * @param historyMasterDO the history master do
   * @return the diff entries for history master
   */
  List<DiffEntry> getDiffEntriesForHistoryMaster(HistoryMasterBaseDO<?, ?> historyMasterDO);

  /**
   * Gets the history entries.
   *
   * @param emgr the emgr
   * @param stdRecord the std record
   * @return the history entries
   */
  List<? extends HistoryEntry> getHistoryEntries(IEmgr<?> emgr, DbRecord<?> stdRecord);

  /**
   * Gets the history entries.
   *
   * @param emgr the emgr
   * @param entityName the entity name
   * @param entityId the entity id
   * @return the history entries
   */
  List<? extends HistoryEntry> getHistoryEntries(IEmgr<?> emgr, String entityName, Serializable entityId);

  /**
   * Find all history entries for given table.
   *
   * @param emgr the emgr
   * @param cls the cls
   * @return the history entries for entity class
   */
  List<? extends HistoryEntry> getHistoryEntriesForEntityClass(IEmgr<?> emgr, Class<? extends DbRecord<?>> cls);

  /**
   * Removes all history entry for given table.
   *
   * @param emgr the emgr
   * @param cls the cls
   * @return the count deleted
   */
  int clearHistoryForEntityClass(IEmgr<?> emgr, Class<? extends DbRecord<?>> cls);

  /**
   * Gets the property converter.
   *
   * @param emgr the emgr
   * @param entity the entity
   * @param pd the pd
   * @return the property converter
   */
  HistoryPropertyConverter getPropertyConverter(IEmgr<?> emgr, Object entity, ColumnMetadata pd);

  /**
   * Checks if an entity has history.
   *
   * @param entityClass the entity class
   * @return true, if successful
   */
  boolean hasHistory(Class<?> entityClass);

}
