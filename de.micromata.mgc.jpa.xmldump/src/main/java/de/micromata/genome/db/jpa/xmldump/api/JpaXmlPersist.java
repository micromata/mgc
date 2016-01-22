package de.micromata.genome.db.jpa.xmldump.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation of a class with customized JpaXmlPerist
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JpaXmlPersist {

  Class<? extends JpaXmlBeforePersistListener>[] beforePersistListener() default {};
}
