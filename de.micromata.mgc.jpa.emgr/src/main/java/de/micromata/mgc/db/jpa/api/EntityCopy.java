package de.micromata.mgc.db.jpa.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import de.micromata.mgc.db.jpa.EntityCopier;
import de.micromata.mgc.db.jpa.PropertyEntityCopier;

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
  Class<? extends EntityCopier>[]copier() default { PropertyEntityCopier.class };
}
