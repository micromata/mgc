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

  /**
   * The listed classes should be persisted before this class.
   * 
   * @return
   */
  Class<?>[] persistAfter() default {};
}
