package de.micromata.genome.db.jpa.history.api;

import java.util.Date;
import java.util.List;

import de.micromata.genome.db.jpa.history.entities.EntityOpType;

/**
 * An change for an Entity.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface HistoryEntry
{

  /**
   * Gets the modified at.
   *
   * @return the modified at
   */
  Date getModifiedAt();

  /**
   * Gets the modified by.
   *
   * @return the modified by
   */
  String getModifiedBy();

  /**
   * alias to getModifiedBy.
   * 
   * @return
   */
  default String getUserName()
  {
    return getModifiedBy();
  }

  /**
   * Gets the diff entries.
   *
   * @return the diff entries
   */
  public List<DiffEntry> getDiffEntries();

  /**
   * Gets the entity op type.
   *
   * @return the entity op type
   */
  EntityOpType getEntityOpType();

  /**
   * Gets the entity name.
   *
   * @return the entity name
   */
  String getEntityName();

  /**
   * Gets the entity id.
   *
   * @return the entity id
   */
  Long getEntityId();

  /**
   * Gets the user comment.
   *
   * @return the user comment
   */
  String getUserComment();

  /**
   * Gets the transaction id.
   *
   * @return the transaction id
   */
  String getTransactionId();

}
