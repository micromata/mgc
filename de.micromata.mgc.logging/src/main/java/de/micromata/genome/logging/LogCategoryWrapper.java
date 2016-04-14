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
 * Wrapps a Log Category.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogCategoryWrapper implements LogCategory
{

  /**
   * The name.
   */
  private String name;

  /**
   * The prefix.
   */
  private String prefix;

  /**
   * The fq name.
   */
  private String fqName;

  /**
   * Instantiates a new log category wrapper.
   */
  public LogCategoryWrapper()
  {

  }

  /**
   * Instantiates a new log category wrapper.
   *
   * @param prefix the prefix
   * @param name the name
   */
  public LogCategoryWrapper(String prefix, String name)
  {
    this.name = name;
    this.prefix = prefix;
    this.fqName = prefix + "." + name;
  }

  /**
   * Instantiates a new log category wrapper.
   *
   * @param other the other
   */
  public LogCategoryWrapper(LogCategory other)
  {
    this.name = other.name();
    this.fqName = other.getFqName();
    this.prefix = other.getPrefix();
  }

  @Override
  public String name()
  {
    return name;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public String getFqName()
  {
    return fqName;
  }

  public void setFqName(String fqName)
  {
    this.fqName = fqName;
  }

  @Override
  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }
}
