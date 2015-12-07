package de.micromata.genome.db.jpa.history.entities;

/**
 * Type of operation on the entity.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public enum EntityOpType
{

  /**
   * The Insert.
   */
  Insert,

  /**
   * The Update.
   */
  Update,

  /**
   * The Deleted.
   */
  Deleted,

  /**
   * The Mark deleted.
   */
  MarkDeleted,

  /**
   * The Umark deleted.
   */
  UmarkDeleted
}
