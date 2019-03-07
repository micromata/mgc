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

package de.micromata.genome.jpa.metainf;

import java.lang.annotation.Annotation;
import java.util.List;

import de.micromata.genome.util.bean.AttrGetter;
import de.micromata.genome.util.bean.AttrSetter;

/**
 * Provides Information for one JPA column. Also provides convience getter/setter and all annotation on fields and
 * getter methods.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface ColumnMetadata extends EmgrDbElement
{
  EntityMetadata getEntity();

  /**
   * Java Property name.
   * 
   * @return the name
   */
  String getName();

  /**
   * A short declaration.
   * 
   * @return the declaration
   */
  String getShortDeclaration();

  /**
   * Gets the max length.
   *
   * @return the max length
   */
  int getMaxLength();

  /**
   * Comes form Column anotation
   * 
   * @return if this unique
   */
  boolean isUnique();

  /**
   * Checks if is nullable.
   *
   * @return true, if is nullable
   */
  boolean isNullable();

  /**
   * Checks if is insertable.
   *
   * @return true, if is insertable
   */
  boolean isInsertable();

  /**
   * Checks if is updatable.
   *
   * @return true, if is updatable
   */
  boolean isUpdatable();

  /**
   * Gets the column definition.
   *
   * @return the column definition
   */
  String getColumnDefinition();

  /**
   * Gets the precision.
   *
   * @return the precision
   */
  int getPrecision();

  /**
   * Gets the scale.
   *
   * @return the scale
   */
  int getScale();

  /**
   * Checks if is association.
   *
   * @return true, if is association
   */
  boolean isAssociation();

  /**
   * Checks if is collection.
   *
   * @return true, if is collection
   */
  boolean isCollection();

  /**
   * Annotation from getter and field.
   *
   * @return the annotations
   */
  List<Annotation> getAnnotations();

  <T extends Annotation> T findAnnoation(Class<T> anotType);

  /**
   * The getter to column.
   *
   * @return the getter
   */
  AttrGetter<Object, Object> getGetter();

  /**
   * The setter to column.
   * 
   * @return the setter
   */
  AttrSetter<Object, Object> getSetter();

  /**
   * In relation, reference to target entity. May be null on simple type.
   * 
   * @return the entity
   */
  EntityMetadata getTargetEntity();

}
