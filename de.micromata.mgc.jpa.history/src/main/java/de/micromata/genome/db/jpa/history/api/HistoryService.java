package de.micromata.genome.db.jpa.history.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.micromata.genome.db.jpa.history.entities.HistoryMasterDO;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.IEmgr;

/**
 * The Interface HistoryService.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface HistoryService
{

  /**
   * Register the listener to Emgr.
   *
   * @param emgrFactory the emgr factory
   */
  void registerEmgrListener(EmgrFactory<?> emgrFactory);

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
  List<DiffEntry> getDiffEntriesForHistoryMaster(HistoryMasterDO historyMasterDO);

  List<? extends HistoryEntry> getHistoryEntries(IEmgr<?> emgr, DbRecord<?> stdRecord);

  /**
   * Gets the history entries.
   *
   * @param entityName the entity name
   * @param entityId the entity id
   * @return the history entries
   */
  List<? extends HistoryEntry> getHistoryEntries(IEmgr<?> emgr, String entityName, Serializable entityId);

  /**
   * Find all history entries for given table.
   *
   * @param cls the cls
   * @return the history entries for entity class
   */
  List<? extends HistoryEntry> getHistoryEntriesForEntityClass(IEmgr<?> emgr, Class<? extends DbRecord<?>> cls);

  /**
   * Removes all history entry for given table.
   *
   * @param cls the cls
   * @return the count deleted
   */
  int clearHistoryForEntityClass(IEmgr<?> emgr, Class<? extends DbRecord<?>> cls);

}
