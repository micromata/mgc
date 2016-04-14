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
 * The category has to match in the data base.
 *
 * @author roger
 */
public interface LogCategory
{

  /**
   * Short Name, commonly name() of the enumeration.
   *
   * @return the string
   */
  public String name();

  /**
   * Fully Qualified name seperated with dots
   * 
   * Normally prefix + . + name is fq name
   * 
   * Common implementation is getClass.getSimpleName() + "." + getName()
   * 
   * @return
   */
  public String getFqName();

  /**
   * A short prefix
   * 
   * @return
   */
  public String getPrefix();

}
