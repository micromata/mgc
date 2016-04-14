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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import de.micromata.genome.util.types.Pair;

/**
 * Basic abstract class for Logging.
 *
 * @author roger@micromata.de
 */
public interface Logging
{
  public static class OrderBy
  {

    /**
     * The column.
     */
    private String column;

    /**
     * The descending.
     */
    private boolean descending = false;

    public OrderBy()
    {

    }

    public OrderBy(String column, boolean descending)
    {
      this.column = column;
      this.descending = descending;
    }

    public String getColumn()
    {
      return column;
    }

    public void setColumn(String column)
    {
      this.column = column;
    }

    public boolean isDescending()
    {
      return descending;
    }

    public void setDescending(boolean descending)
    {
      this.descending = descending;
    }
  }

  /**
   * Debug.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public void debug(LogCategory cat, String msg, LogAttribute... attributes);

  /**
   * Trace.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public void trace(LogCategory cat, String msg, LogAttribute... attributes);

  /**
   * Info.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public void info(LogCategory cat, String msg, LogAttribute... attributes);

  /**
   * Note.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public void note(LogCategory cat, String msg, LogAttribute... attributes);

  /**
   * Warn.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public void warn(LogCategory cat, String msg, LogAttribute... attributes);

  /**
   * Error.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public void error(LogCategory cat, String msg, LogAttribute... attributes);

  /**
   * Fatal.
   *
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public void fatal(LogCategory cat, String msg, LogAttribute... attributes);

  /**
   * Do log.
   *
   * @param ll the ll
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public void doLog(LogLevel ll, LogCategory cat, String msg, LogAttribute... attributes);

  /**
   * Internal implementation of writing a log entry.
   *
   * @param lwe must not be null
   */
  public void doLogImpl(final LogWriteEntry lwe);

  /**
   * If a Logging-Implementation (like DBLogging) is not already initialized, use this logging method.
   *
   * @param ll the ll
   * @param cat the cat
   * @param msg the msg
   * @param attributes the attributes
   */
  public void logPreStart(LogLevel ll, LogCategory cat, String msg, LogAttribute... attributes);

  /**
   * Minimal Logging threshold
   * 
   * @return
   */
  public LogLevel getConfigMinLogLevel();

  /**
   * 
   * @return all known Categories
   */
  public Collection<LogCategory> getRegisteredCategories();

  /**
   * @return all known AttributeType
   */
  public Collection<LogAttributeType> getRegisteredAttributes();

  /**
   * Supports search.
   *
   * @return true, if this implementation supports searching
   */
  public boolean supportsSearch();

  /**
   * Supports fulltext search.
   *
   * @return true if this implementation supports full text searching
   */
  public boolean supportsFulltextSearch();

  /**
   * 
   * @return a list of AttributeTypes which can be used for saeching
   */
  public Collection<LogAttributeType> getSearchAttributes();

  /**
   * Es werdem logs nach diversen Kriterien selektiert. Das Ergebnis beinhaltet die Einträge aus der log master Tabelle
   * und dazugehörige LogAttribute
   *
   * @param start the start
   * @param end the end
   * @param loglevel the loglevel
   * @param category the category
   * @param msg the msg
   * @param logAttributes the log attributes
   * @param startRow the start row
   * @param maxRow the max row
   * @param orderBy the order by
   * @param masterOnly the master only
   * @param callback the callback
   */
  public void selectLogs(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
      LogEntryCallback callback);

  /**
   * Select the given LogIds.
   *
   * @param logId List of Log-IDs. In case of database logging, this are PK of the log master table
   * @param masterOnly the master only
   * @param callback the callback
   */
  public void selectLogs(List<Object> logId, boolean masterOnly, LogEntryCallback callback);

  /**
   * Formattiert eine LogId zu einem String Z.B. Long.toString()
   *
   * @param logId the log id
   * @return the string
   */
  public String formatLogId(Object logId);

  /**
   * reverse op for formatLogId.
   *
   * @param logId the log id
   * @return the object
   */
  public Object parseLogId(String logId);

  /**
   * Schreibt ein Log.
   *
   * @param lwe wenn lwe.timestamp != 0 muss dieser uebernommen werden.
   */
  void logLwe(LogWriteEntry lwe);

}
