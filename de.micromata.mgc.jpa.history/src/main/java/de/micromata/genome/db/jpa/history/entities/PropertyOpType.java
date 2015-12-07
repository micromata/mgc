package de.micromata.genome.db.jpa.history.entities;

/**
 * Operation type on one entity.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public enum PropertyOpType
{

  /**
   * The Undefined.
   */
  Undefined,

  /**
   * The Insert.
   */
  Insert,

  /**
   * The Update.
   */
  Update,

  /**
   * The Delete.
   */
  Delete;

  /**
   * From string.
   *
   * @param key the key
   * @return the property op type
   */
  public static PropertyOpType fromString(String key)
  {
    for (PropertyOpType v : values()) {
      if (v.name().equals(key) == true) {
        return v;
      }
    }
    return Undefined;
  }
}
