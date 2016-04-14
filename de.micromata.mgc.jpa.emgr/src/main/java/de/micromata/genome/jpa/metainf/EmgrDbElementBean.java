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

/**
 * The Class EmgrDbElementBean.
 * 
 * equals/hashCode is the underlying javaType.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class EmgrDbElementBean implements EmgrDbElement
{

  /**
   * The java type.
   */
  private Class<?> javaType;

  /**
   * The database name.
   */
  private String databaseName;

  @Override
  public int hashCode()
  {
    return javaType.hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if ((obj instanceof EmgrDbElementBean) == false) {
      return false;
    }
    return javaType.equals(((EmgrDbElementBean) obj).javaType);
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public String toString()
  {

    return javaType == null ? super.toString() : javaType.getName();
  }

  @Override
  public Class<?> getJavaType()
  {
    return javaType;
  }

  /**
   * Sets the java type.
   *
   * @param javaType the new java type
   */
  public void setJavaType(Class<?> javaType)
  {
    this.javaType = javaType;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String getDatabaseName()
  {
    return databaseName;
  }

  /**
   * Sets the database name.
   *
   * @param databaseName the new database name
   */
  public void setDatabaseName(String databaseName)
  {
    this.databaseName = databaseName;
  }

}
