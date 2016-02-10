package de.micromata.genome.db.jpa.logging.entities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks Entity Columns in BaseLogMasterDO which should be used searchable attributes
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityLogSearchAttribute {
  /**
   * name of the LogAttributeType.
   * 
   * @return
   */
  String[] enumName();
}
