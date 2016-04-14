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

package de.micromata.genome.db.jpa.xmldump.api;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

import de.micromata.genome.jpa.EmgrFactory;

/**
 * The Interface JpaXmlDumpService.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface JpaXmlDumpService
{

  /**
   * Dump all table entities into xml.
   *
   * @param fac the fac
   * @return the string
   */
  String dumpToXml(EmgrFactory<?> fac);

  /**
   * Dump to xml.
   *
   * @param fac the fac
   * @param outfile the outfile
   */
  void dumpToXml(EmgrFactory<?> fac, File outfile);

  void dumpToXml(EmgrFactory<?> fac, OutputStream outfile);

  /**
   * Dump to xml.
   *
   * @param fac the fac
   * @param writer the writer
   */
  void dumpToXml(EmgrFactory<?> fac, Writer writer);

  /**
   * The Enum RestoreMode.
   */
  public static enum RestoreMode
  {

    /**
     * Asume Pks are stable, and only insert new entities.
     */
    InsertNew,

    /**
     * Asume Pks are stable merge.
     */
    OverWrite,
    /**
     * Insert entities. May fail, if constraints
     */
    InsertAll
  }

  /**
   * Restore db.
   *
   * @param fac the fac
   * @param inputStream the input stream
   * @param restoreMode the restore mode
   * @return the number of records are inside the xml
   */
  int restoreDb(EmgrFactory<?> fac, InputStream inputStream, RestoreMode restoreMode);

  /**
   * Restore db.
   *
   * @param fac the fac
   * @param file the file
   * @param restoreMode the restore mode
   * @return the number of records are inside the xml
   */
  int restoreDb(EmgrFactory<?> fac, File file, RestoreMode restoreMode);

  /**
   * Registed global listener. Modify this list only on initializion/constructor phase of the service, because list not
   * synchronized.
   * 
   * You can initialize the Listener with the JRE ServiceLoader mechanism.
   * 
   * @return
   */
  List<JpaXmlBeforePersistListener> getGlobalBeforeListener();
}
