package de.micromata.genome.util.runtime.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used in LocalSettingsConfigModel beans to annotate fields
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ALocalSettingsPath {

  /**
   * Property name for local-settings.
   * 
   * if "<fieldName>" use the annotated fields name.
   * 
   * @return
   */
  String key() default "<fieldName>";

  /**
   * default value.
   * 
   * @return
   */
  String defaultValue() default "";

  /**
   * Used in persistence of properties to comment this key.
   * 
   * @return
   */
  String comment() default "";
}
