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

package de.micromata.genome.logging.spi.log4j;

import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.LogCategory;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public enum Log4JLogCategory implements LogCategory
{
  Log4J;
  static {
    BaseLogging.registerLogCategories(values());
  }
  private String fqName;

  private Log4JLogCategory()
  {
    fqName = getPrefix() + "." + name();
  }

  @Override
  public String getFqName()
  {
    return fqName;
  }

  @Override
  public String getPrefix()
  {
    return "L4J";
  }

}
