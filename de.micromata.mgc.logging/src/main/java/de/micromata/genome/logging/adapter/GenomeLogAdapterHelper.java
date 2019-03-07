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

package de.micromata.genome.logging.adapter;

import java.util.List;

import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogCategoryWrapper;
import de.micromata.genome.util.types.Converter;

/**
 * Helper for writing adapters for existing logging frameworks to log into genome.
 * 
 * @author wolle
 *
 */
public class GenomeLogAdapterHelper
{

  /**
   * The recursive guard.
   */
  private static ThreadLocal<Boolean> recursiveGuard = new ThreadLocal<Boolean>() {

    @Override
    protected Boolean initialValue()
    {
      return Boolean.FALSE;
    }

  };

  /**
   * The strip namespace list.
   */
  private List<String> stripNamespaceList;

  /**
   * The ignore namespace list.
   */
  private List<String> ignoreNamespaceList;

  /**
   * List of namespaces to ignore.
   * 
   * @param stripNamespaces the stripNamespaces
   */
  public void setStripNamespaces(String stripNamespaces)
  {
    if (stripNamespaces != null) {
      stripNamespaceList = Converter.parseStringTokens(stripNamespaces, ", ", false);
    }
  }

  public List<String> getStripNamespaceList()
  {
    return stripNamespaceList;
  }

  public void setIgnoreNamespaces(String ignoreNamespaces)
  {
    ignoreNamespaceList = Converter.parseStringTokens(ignoreNamespaces, ", ", false);
  }

  public List<String> getIgnoreNamespaceList()
  {
    return ignoreNamespaceList;
  }

  public static ThreadLocal<Boolean> getRecursiveGuard()
  {
    return recursiveGuard;
  }

  /**
   * Creates the cat.
   *
   * @param logName the log name
   * @return the log category
   */
  public LogCategory createCat(String logName)
  {
    // String fqCat = logName;
    if (stripNamespaceList != null) {
      for (String sn : stripNamespaceList) {
        if (logName.startsWith(sn) == true) {
          logName = logName.substring(sn.length());
          break;
        }
      }
    }

    LogCategoryWrapper cat = new LogCategoryWrapper("JUL", logName);

    return cat;
  }

  /**
   * Ignore cat.
   *
   * @param logName the log name
   * @return true, if successful
   */
  public boolean ignoreCat(String logName)
  {
    if (ignoreNamespaceList == null) {
      return false;
    }
    for (String ign : ignoreNamespaceList) {
      if (logName.startsWith(ign) == true) {
        return true;
      }
    }
    return false;
  }

}
