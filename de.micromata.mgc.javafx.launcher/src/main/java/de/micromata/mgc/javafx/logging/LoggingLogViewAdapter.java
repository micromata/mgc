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

package de.micromata.mgc.javafx.logging;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.eclipsesource.json.JsonArray;

import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.logging.loghtmlwindow.LogJsonUtils;
import de.micromata.genome.logging.spi.log4j.RoundList;
import de.micromata.genome.util.types.Pair;
import de.micromata.mgc.javafx.ControllerService;
import netscape.javascript.JSObject;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LoggingLogViewAdapter
{
  private static final Logger LOG = Logger.getLogger(LoggingLogViewAdapter.class);
  private RoundList<LogWriteEntry> logWriteEntries = new RoundList<>(1000);
  JSObject logView;
  public boolean supportsPoll = true;
  WeakReference<Logging> curLogging = new WeakReference<>(LoggingServiceManager.get().getLogging());

  public class LoggingConfiguration
  {
    public boolean supportsSearch;
    public boolean supportsFulltextSearch;

    public String[] loggingCategories;
    public String[] attributes;
    public String[] searchAttributes;
    public String threshold;
  }

  public boolean supportsSearch = false;
  public LoggingConfiguration loggingConfiguration = new LoggingConfiguration();

  public LoggingLogViewAdapter()
  {

  }

  public void init(JSObject logView)
  {
    this.logView = logView;
    initLogConfiguration();
  }

  public void initLogConfiguration()
  {
    Logging logging = LoggingServiceManager.get().getLogging();

    loggingConfiguration.supportsSearch = logging.supportsSearch();
    loggingConfiguration.supportsFulltextSearch = logging.supportsFulltextSearch();
    Collection<LogCategory> cats = logging.getRegisteredCategories();
    loggingConfiguration.loggingCategories = new String[cats.size()];
    int i = 0;
    for (LogCategory lc : cats) {
      loggingConfiguration.loggingCategories[i] = lc.getFqName();
      ++i;
    }

    Collection<LogAttributeType> attrs = logging.getRegisteredAttributes();
    loggingConfiguration.attributes = new String[attrs.size()];
    i = 0;
    for (LogAttributeType at : attrs) {
      loggingConfiguration.attributes[i] = at.name();
      ++i;
    }
    attrs = logging.getSearchAttributes();
    loggingConfiguration.searchAttributes = new String[attrs.size()];
    i = 0;
    for (LogAttributeType at : attrs) {
      loggingConfiguration.searchAttributes[i] = at.name();
      ++i;
    }

    LogConfigurationDAO logconfig = LoggingServiceManager.get().getLogConfigurationDAO();
    loggingConfiguration.threshold = logconfig.getThreshold().name();

  }

  public void refreshLogConfiguration()
  {
    ControllerService.get().runInToolkitThread(() -> {
      initLogConfiguration();
      if (logView != null) {
        logView.call("refreshForm");
      }
    });
  }

  List<LogWriteEntry> getLogEntries(long lastPollTime)
  {
    synchronized (logWriteEntries) {
      if (curLogging.get() != LoggingServiceManager.get().getLogging()) {
        curLogging = new WeakReference<Logging>(LoggingServiceManager.get().getLogging());
        initLogConfiguration();
      }
      List<LogWriteEntry> ret = new ArrayList<>();
      for (LogWriteEntry le : logWriteEntries) {
        if (le.getTimestamp() <= lastPollTime) {
          continue;
        }
        ret.add(le);
      }
      return ret;
    }
  }

  public void logPoll(long lastPollTime, JSObject callback)
  {
    List<LogWriteEntry> sicEntry = getLogEntries(lastPollTime);

    JsonArray ret = new JsonArray();
    for (LogWriteEntry le : sicEntry) {
      ret.add(LogJsonUtils.logEntryToJson(le));
    }
    String sret = ret.toString();
    callback.call("doCallback", sret);
  }

  public void logSelect(JSObject logFormData, JSObject callback)
  {
    String logLevel = (String) logFormData.getMember("logLevel");
    String logCategory = (String) logFormData.getMember("logCategory");
    String logMessage = (String) logFormData.getMember("logMessage");
    Integer startRow = (Integer) logFormData.getMember("startRow");
    Integer maxRow = (Integer) logFormData.getMember("maxRow");
    if (startRow == null) {
      startRow = 1;
    }
    if (maxRow == null) {
      maxRow = 30;
    }
    List<Pair<String, String>> logAttributes = null;
    String logAttribute1Type = (String) logFormData.getMember("logAttribute1Type");
    String logAttribute1Value = (String) logFormData.getMember("logAttribute1Value");
    String logAttribute2Type = (String) logFormData.getMember("logAttribute2Type");
    String logAttribute2Value = (String) logFormData.getMember("logAttribute2Value");
    if (StringUtils.isNotBlank(logAttribute1Type) && StringUtils.isNotBlank(logAttribute1Value)) {
      logAttributes = new ArrayList<>();
      logAttributes.add(Pair.make(logAttribute1Type, logAttribute1Value));
    }
    if (StringUtils.isNotBlank(logAttribute2Type) && StringUtils.isNotBlank(logAttribute2Value)) {
      if (logAttributes == null) {
        logAttributes = new ArrayList<>();
      }
      logAttributes.add(Pair.make(logAttribute2Type, logAttribute2Value));
    }
    LogLevel level = LogLevel.fromString(logLevel, LogLevel.Note);

    JsonArray ret = new JsonArray();
    Logging logging = LoggingServiceManager.get().getLogging();
    logging.selectLogs(null, null, level.getLevel(), logCategory, logMessage,
        logAttributes,
        startRow,
        maxRow, null, true, (le) -> {
          ret.add(LogJsonUtils.logEntryToJson(logging, le, false));
        });
    String sret = ret.toString();
    callback.call("doCallback", sret);
  }

  public void logSelectAttributes(String logId, JSObject callback)
  {
    Logging logging = LoggingServiceManager.get().getLogging();
    Object logid = logging.parseLogId(logId);
    List<Object> ids = Arrays.asList(logid);
    JsonArray arr = new JsonArray();
    logging.selectLogs(ids, false, row -> {
      arr.add(LogJsonUtils.logEntryToJson(logging, row, true));
    });
    String sret = arr.toString();
    callback.call("doCallback", sret);
  }

  public void addLogEntry(LogWriteEntry le)
  {
    synchronized (logWriteEntries) {
      logWriteEntries.add(le);
    }
  }

  public void addLogEntries(List<LogWriteEntry> logentries)
  {
    synchronized (logWriteEntries) {
      logWriteEntries.addAll(logentries);
    }
  }

  public boolean isSupportsPoll()
  {
    return supportsPoll;
  }

  public void setSupportsPoll(boolean supportsPoll)
  {
    this.supportsPoll = supportsPoll;
  }

  public boolean isSupportsSearch()
  {
    return supportsSearch;
  }

  public void setSupportsSearch(boolean supportsSearch)
  {
    this.supportsSearch = supportsSearch;
  }

  public LoggingConfiguration getLoggingConfiguration()
  {
    return loggingConfiguration;
  }

  public void setLoggingConfiguration(LoggingConfiguration loggingConfiguration)
  {
    this.loggingConfiguration = loggingConfiguration;
  }

  public void jsdebug(String message)
  {
    LOG.debug(message);
  }

  public void jserror(String message)
  {
    LOG.error(message);
  }

  public void jswarn(String message)
  {
    LOG.warn(message);
  }
}
