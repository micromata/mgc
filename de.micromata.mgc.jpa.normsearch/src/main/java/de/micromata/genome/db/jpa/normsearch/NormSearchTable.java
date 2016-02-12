package de.micromata.genome.db.jpa.normsearch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.micromata.genome.db.jpa.normsearch.entities.NormSearchDO;

/**
 * Mark an Entity as NormSearchable.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NormSearchTable {
  /**
   * Which Entity should be used to store norm search.
   * 
   * @return
   */
  Class<? extends NormSearchDO> normSearchTable();

  /**
   * Provide an option creator for a NormSearch entry.
   * 
   * @return empty or size 1 element.
   */
  Class<? extends NormalizedSearchEntryCreator>[] creator() default {};

  /**
   * Which properties of the Entity should be stored in the normalized search.
   *
   * If not set, uses the NormSearchProperty annotation.
   * 
   * @return the string[]
   */
  String[] normSearchFields() default {
  };
}
