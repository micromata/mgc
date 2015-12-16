package de.micromata.genome.jpa;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Mark an entity, witch stragegy should be used to copy content.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityCopy {

  /**
   * List of EntityCopier, which should be applied.
   *
   * @return the class<? extends entity copier>[]
   */
  Class<? extends EntityCopier>[] copier() default { PropertyEntityCopier.class };

  /**
   * If set to false, the property will not be copied.
   * 
   * @return
   */
  boolean noCopy() default false;

  /**
   * Mark n property if should be deep copied.
   * 
   * @return
   */
  boolean deepCopy() default false;
}
