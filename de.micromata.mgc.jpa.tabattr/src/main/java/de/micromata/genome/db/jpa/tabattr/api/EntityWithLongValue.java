package de.micromata.genome.db.jpa.tabattr.api;

import java.io.Serializable;

/**
 * The Interface EntityWithLongValue.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */

public interface EntityWithLongValue<PK extends Serializable>
{

  /**
   * The PK of the attribute.
   *
   * @return may null if not stored in db.
   */
  PK getPk();

  /**
   * The PK of the attribute.
   *
   * @param pk the new pk
   */
  void setPk(PK pk);

  /**
   * The attribute value.
   *
   * @return the value
   */
  String getValue();

  /**
   * Set the String value.
   *
   * @param value the new value
   */
  void setValue(String value);

}
