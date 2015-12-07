package de.micromata.genome.db.jpa.history.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import de.micromata.genome.db.jpa.history.impl.SimplePropertyConverter;

/**
 * Annotation to field or getter to transform property for history
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HistoryProperty {

  /**
   * Converter for the history properties.
   *
   * @return the class<? extends history property converter>
   */
  Class<? extends HistoryPropertyConverter>converter() default SimplePropertyConverter.class;
}
