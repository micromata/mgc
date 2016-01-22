package de.micromata.genome.db.jpa.xmldump.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation of a class with customized JpaXmlPerist
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JpaXmlPersist {
  /**
   * A listener before an entity should be persistet.
   * 
   * @return
   */
  Class<? extends JpaXmlBeforePersistListener>[] beforePersistListener() default {};

  /**
   * If set to true, the entities will not store to database.
   * 
   * This may be usefull for 100% children.
   *
   * @return true, if successful
   */
  boolean noStore() default false;
}
