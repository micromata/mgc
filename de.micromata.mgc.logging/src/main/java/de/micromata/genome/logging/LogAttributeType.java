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

package de.micromata.genome.logging;

/**
 * has to be synchronized with db.
 *
 * @author roger
 */
public interface LogAttributeType
{

  /**
   * The Root log message.
   */
  LogAttributeType RootLogMessage = null;

  /**
   * Name.
   *
   * @return enumeration name
   */
  public String name();

  /**
   * Will be ignored, if this LogAttributeType is not a searchKey.
   * 
   * @return db column name. Return null if no column name is given
   */
  public String columnName();

  /**
   * Max value size.
   *
   * @return maximal column width. Will be ignored, if this LogAttributeType is not a searchKey.
   */
  public int maxValueSize();

  /**
   * @return true if this attribute can be searched
   */
  public boolean isSearchKey();

  /**
   * 
   * @return null, if no defaultFiller is registered for this LogAttributeType
   */
  public AttributeTypeDefaultFiller getAttributeDefaultFiller();

  /**
   * Renderer for this Attribute Type. Return DefaultLogAttributeRenderer.INSTANCE if no customized renderer should be used.
   * 
   * @return the current {@link LogAttributeRenderer}
   */
  public LogAttributeRenderer getRenderer();
}