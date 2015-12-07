package de.micromata.genome.util.runtime;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnot {
  String value() default "";
}
