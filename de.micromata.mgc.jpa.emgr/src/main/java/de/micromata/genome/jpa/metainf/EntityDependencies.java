package de.micromata.genome.jpa.metainf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Explicite marks dependendies. The Emgr Meta order the tables by dependencies
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityDependencies {
  /**
   * This entity references other entities.
   * 
   * @return
   */
  Class<?>[] references() default {};

  /**
   * This entity is referenced by other.
   * 
   * @return
   */
  Class<?>[] referencedBy() default {};
}
