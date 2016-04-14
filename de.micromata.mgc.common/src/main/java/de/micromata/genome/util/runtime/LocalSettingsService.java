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

package de.micromata.genome.util.runtime;

import java.util.List;
import java.util.Map;

import de.micromata.genome.util.types.Pair;

public interface LocalSettingsService
{
  static LocalSettingsService get()
  {
    return LocalSettings.getImpl();
  }

  public static void reset()
  {
    LocalSettings.resetImpl();
  }

  /**
   * alias to getProperty(key).
   * 
   * @param key the key
   * @return the property
   */
  String get(String key);

  /**
   * get resolved value for key
   * 
   * @param key
   * @return
   */
  default String getProperty(String key)
  {
    return get(key);
  }

  /**
   * get resolved value for key if exists. otherwise defaultValue
   * 
   * @param key
   * @param defaultValue
   * @return
   */
  default String getProperty(String key, String defaultValue)
  {
    return get(key, defaultValue);
  }

  String get(String key, String defaultValue);

  int getIntValue(String key, int defaultValue);

  long getLongValue(String key, long defaultValue);

  boolean getBooleanValue(String key, boolean defaultValue);

  /**
   * Resolve include ${}.
   * 
   * @param value the value
   * @return the string
   */
  public String resolve(String value);

  boolean containsKey(String key);

  /**
   * Gets the entries with prefix.
   * 
   * @param prefix the prefix
   * @return rest of key -> value
   */
  List<Pair<String, String>> getEntriesWithPrefix(String prefix);

  /**
   * Gets the keys prefix with infix.
   * 
   * @param start a.b
   * @param end c
   * @return finds a.b.x when a.b.x.c exists
   */
  List<String> getKeysPrefixWithInfix(String start, String end);

  /**
   * The loader this was loaded with.
   * 
   * @return
   */
  LocalSettingsLoader getLocalSettingsLoader();

  void copyInto(Map<String, String> map);

  Map<String, String> getMap();
}
