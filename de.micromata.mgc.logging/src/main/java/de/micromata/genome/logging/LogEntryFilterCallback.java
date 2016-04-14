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
 * The Class LogEntryFilterCallback.
 *
 * @author roger
 */
public class LogEntryFilterCallback implements LogEntryCallback
{

  /**
   * The next.
   */
  private LogEntryCallback next;

  /**
   * throw away logEntries.
   */
  private int filteredLogEntries = 0;

  /**
   * not filtered away.
   */
  private int passedLogEntries = 0;

  /**
   * The log config dao.
   */
  private LogConfigurationDAO logConfigDAO;

  /**
   * The max rows.
   */
  private int maxRows;

  /**
   * Instantiates a new log entry filter callback.
   */
  public LogEntryFilterCallback()
  {

  }

  /**
   * Instantiates a new log entry filter callback.
   *
   * @param next the next
   * @param logConfigDAO the log config dao
   * @param maxRows the max rows
   */
  public LogEntryFilterCallback(LogEntryCallback next, LogConfigurationDAO logConfigDAO, int maxRows)
  {
    this.next = next;
    this.logConfigDAO = logConfigDAO;
    this.maxRows = maxRows;
  }

  @Override
  public void onRow(LogEntry le) throws EndOfSearch
  {
    ++filteredLogEntries;
    if (logConfigDAO.filterView(le) == false) {
      return;
    }
    ++passedLogEntries;
    next.onRow(le);
    if (passedLogEntries >= maxRows) {
      throw new EndOfSearch();
    }
  }

  @Override
  public LogEntry createLogEntry(LogEntry le)
  {
    return next.createLogEntry(le);
  }

  public LogEntryCallback getNext()
  {
    return next;
  }

  public void setNext(LogEntryCallback next)
  {
    this.next = next;
  }

  public int getFilteredLogEntries()
  {
    return filteredLogEntries;
  }

  public void setFilteredLogEntries(int filteredLogEntries)
  {
    this.filteredLogEntries = filteredLogEntries;
  }

  public int getPassedLogEntries()
  {
    return passedLogEntries;
  }

  public void setPassedLogEntries(int passedLogEntries)
  {
    this.passedLogEntries = passedLogEntries;
  }

  public int getMaxRows()
  {
    return maxRows;
  }

  public void setMaxRows(int maxRows)
  {
    this.maxRows = maxRows;
  }

  public LogConfigurationDAO getLogConfigDAO()
  {
    return logConfigDAO;
  }

  public void setLogConfigDAO(LogConfigurationDAO logConfigDAO)
  {
    this.logConfigDAO = logConfigDAO;
  }

}
