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

package de.micromata.genome.jpa;

/**
 * provides utils to manage schema
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JpaSchemaService
{

  /**
   * Writes create script to file.
   *
   * @param fileName the file name
   */
  void exportCreateSchemaToFile(String fileName);

  /**
   * Export drop schema to file.
   *
   * @param fileName the file name
   */
  void exportDropSchemaToFile(String fileName);

  /**
   * Clear all tables. NOTE: Do this only in unittest.
   * 
   * The default implementation loads all entities into RAM.
   * 
   * If you need special handling, you can annotate entity class with ATableTruncater and implement a own table deleter.
   */
  void clearDatabase();
}
