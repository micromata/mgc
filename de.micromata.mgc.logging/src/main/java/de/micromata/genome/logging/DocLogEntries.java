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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ObjectUtils;

/**
 * Java representation for a documented log entries.
 *
 * @author roger@micromata.de
 */
public class DocLogEntries
{

  /**
   * The entries.
   */
  Map<DocLogEntryKey, List<DocLogEntry>> entries = new HashMap<DocLogEntryKey, List<DocLogEntry>>();

  /**
   * Adds the comments.
   *
   * @param le the le
   */
  public void addComments(LogEntry le)
  {
    DocLogEntry dle = findDocEntry(le);
    if (dle == null) {
      return;
    }
    le.setDocLogEntry(dle);
  }

  /**
   * Find doc entry.
   *
   * @param le the le
   * @return the doc log entry
   */
  public DocLogEntry findDocEntry(LogEntry le)
  {
    DocLogEntryKey sdl = new DocLogEntryKey(le);
    return findDocEntry(sdl);

  }

  /**
   * Find doc entry.
   *
   * @param sdl the sdl
   * @return the doc log entry
   */
  public DocLogEntry findDocEntry(DocLogEntryKey sdl)
  {
    List<DocLogEntry> dlel = entries.get(sdl);
    if (dlel == null) {
      return null;
    }
    for (DocLogEntry dle : dlel) {
      if (sdl.getMessage().startsWith(dle.getConstMessage()) == true) {
        return dle;
      }
    }
    return null;
  }

  /**
   * The doc log entries.
   */
  // TODO genome move to LogConfigurationDAO
  private static DocLogEntries docLogEntries = null;

  /**
   * Load.
   *
   * @param properties the properties
   * @return the doc log entries
   */
  @SuppressWarnings("unchecked")
  public static DocLogEntries load(Map properties)
  {
    Map<String, Object> props = properties;
    DocLogEntries dle = new DocLogEntries();
    int i;
    for (i = 0; props.get("" + i + ".level") != null; ++i) {
      DocLogEntry dl = new DocLogEntry();
      dl.setLevel(ObjectUtils.toString(props.get("" + i + ".level")));
      dl.setDomain(ObjectUtils.toString(props.get("" + i + ".domain")));
      dl.setCategory(ObjectUtils.toString(props.get("" + i + ".category")));
      dl.setConstMessage(ObjectUtils.toString(props.get("" + i + ".message")));
      dl.setReason(ObjectUtils.toString(props.get("" + i + ".reason")));
      dl.setAction(ObjectUtils.toString(props.get("" + i + ".action")));
      // log4jlog.debug("dl: " + dl.hashCode() + ": " + dl.toString());
      DocLogEntryKey dlek = new DocLogEntryKey(dl);
      List<DocLogEntry> ldel = dle.entries.get(dlek);
      if (ldel == null) {
        ldel = new ArrayList<DocLogEntry>();
        dle.entries.put(dlek, ldel);
      } else {
        /**
         * @nologging
         * @reason Conflict with duplicated DocLogs
         * @action Concact Developer
         */
        //GLog.note(GenomeLogCategory.Configuration, "Duplicated doclog: " + dl.hashCode() + ": " + dl.toString());
        // log4jlog.debug("fdl: x" + dl.hashCode() + ": " + dl.toString());
        // ldel.add(dl);
      }

      ldel.add(dl);
    }
    return dle;
  }

  /**
   * Load.
   *
   * @param is the is
   * @return the doc log entries
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static DocLogEntries load(InputStream is) throws IOException
  {
    Properties props = new Properties();
    props.load(is);
    return load(props);
  }

  /**
   * Load.
   *
   * @return the doc log entries
   */
  private static DocLogEntries load()
  {
    DocLogEntries dle = new DocLogEntries();
    try {
      InputStream is = DocLogEntries.class.getClassLoader().getResourceAsStream("logentries.properties");
      if (is == null) {
        /**
         * @logging
         * @reason No logentries.properties can be found in class path
         * @action None
         */
        GLog.note(GenomeLogCategory.Configuration, "No embedded LogEntries can be found");
        return dle;
      }
      return load(is);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return dle;
  }

  /**
   * Gets the.
   *
   * @return the doc log entries
   */
  public static DocLogEntries get()
  {
    if (docLogEntries != null) {
      return docLogEntries;
    }
    synchronized (DocLogEntries.class) {
      docLogEntries = load();
      return docLogEntries;
    }

  }
}
