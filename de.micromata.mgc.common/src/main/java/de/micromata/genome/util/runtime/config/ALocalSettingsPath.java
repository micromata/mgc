//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
