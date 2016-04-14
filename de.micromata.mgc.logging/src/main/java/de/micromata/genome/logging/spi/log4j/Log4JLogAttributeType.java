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

import de.micromata.genome.logging.AttributeTypeDefaultFiller;
import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.LogAttributeRenderer;
import de.micromata.genome.logging.LogAttributeType;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public enum Log4JLogAttributeType implements LogAttributeType
{
  Log4JCategory,

  Log4JMDC,

  Log4JLocation;
  static {
    BaseLogging.registerLogAttributeType(values());
  }

  @Override
  public String columnName()
  {
    return null;
  }

  @Override
  public int maxValueSize()
  {
    return 0;
  }

  @Override
  public boolean isSearchKey()
  {
    return false;
  }

  @Override
  public AttributeTypeDefaultFiller getAttributeDefaultFiller()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public LogAttributeRenderer getRenderer()
  {
    return null;
  }

}
