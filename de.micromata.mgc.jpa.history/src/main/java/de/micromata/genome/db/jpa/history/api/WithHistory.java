package de.micromata.genome.db.jpa.history.api;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.micromata.genome.db.jpa.history.impl.DefaultHistoryPropertyProvider;

/**
 * Mark entity, which should be has History.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })

public @interface WithHistory {

  /**
   * Which code should be used to retrieve properties.
   *
   * @return the class<? extends history property provider>[]
   */
  Class<? extends HistoryPropertyProvider>[]propertyProvider() default { DefaultHistoryPropertyProvider.class };

  /**
   * List of properties names not create history entries.
   *
   * @return the string[]
   */
  String[]noHistoryProperties() default {};

  /**
   * List of properties, which should make history, even it is in noHistoryProperties.
   *
   * @return the string[]
   */
  String[]forceHistoryProperties() default {};

}
