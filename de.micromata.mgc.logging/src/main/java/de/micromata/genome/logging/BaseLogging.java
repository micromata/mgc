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

import de.micromata.genome.logging.events.LogRegisteredCategoryChangedEvent;
import de.micromata.genome.logging.events.LogRegisteredLogAttributesChangedEvent;
import de.micromata.genome.logging.events.LogWriteEntryEvent;
import de.micromata.genome.stats.Stats;
import de.micromata.genome.util.types.Pair;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceMap;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Common base implementation.
 *
 * @author roger
 */
public abstract class BaseLogging implements Logging
{

  /**
   * The is inited.
   */
  protected static boolean isInited = false;

  /**
   * will be set while initialization. avoid to call recursive
   */
  protected boolean inInitialization = false;

  /**
   * The pre start logs.
   */
  protected static List<LogWriteEntry> preStartLogs = new ArrayList<>();

  /**
   * do not call any modifable access to these. But constructes new copy and asign.
   */
  protected static Map<String, LogCategory> registerdLogCategories = new HashMap<>();

  /**
   * do not call any modifable access to these. But constructes new copy and asign.
   */
  protected static Map<String, LogAttributeType> registerdLogAttributes = new HashMap<>();

  /**
   * do not call any modifable access to these. But constructes new copy and asign.
   */
  protected static Map<String, LogAttributeType> defaultLogAttributes = new HashMap<>();

  /**
   * do not call any modifable access to these. But constructes new copy and asign.
   */
  protected static Map<String, LogAttributeType> searchLogAttributes = new HashMap<>();

  /**
   * Default Einstellung fuer Maximalen Logattribute 1MB.
   */
  public static final int DEFAULT_MAX_LOG_ATTR_LENGTH = 1024 * 1024;

  /**
   * Maximale Laenge eines Logattributes.
   */
  private int maxLogAttrLength = DEFAULT_MAX_LOG_ATTR_LENGTH;

  /**
   * Name des LogAttributeType zu maximaler Groesse des Logeintrags.
   *
   * Ueberschreibt maxLogAttrLength.
   */
  private Map<String, Integer> logAttributeLimitMap = new HashMap<String, Integer>();

  /**
   * filter. Modifiy only on construction time, because not synchronized.
   */
  private List<LogWriteFilter> writeFilters = new ArrayList<>();
  /**
   * Modifiy only on construction time, because not synchronized.
   */
  private List<LogFilter> readFilters = new ArrayList<>();

  protected static <V> Map<String, V> createNewCacheMap(Map<String, V> oldValues)
  {
    ReferenceMap<String, V> ret = new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.HARD, AbstractReferenceMap.ReferenceStrength.WEAK);
    ret.putAll(oldValues);
    return ret;
  }

  protected static <V> Map<String, V> createNewDisposableMap(Map<String, V> oldValues, Map<String, V> newValues)
  {
    Map<String, V> ret = createNewCacheMap(oldValues);
    ret.putAll(newValues);
    return ret;

  }

  protected static <V> Map<String, V> createNewMap(Map<String, V> oldValues, Map<String, V> newValues)
  {
    Map<String, V> ret = new HashMap<>(oldValues);
    ret.putAll(newValues);
    return ret;

  }

  /**
   * Um die zu Anwendung neu gekommene(z.B. durch ein Plugin) {@link LogCategory} s zu registreren.
   *
   * @param cats beliebige Menge an {@link LogCategory}
   */
  public static void registerLogCategories(LogCategory... cats)
  {
    Map<String, LogCategory> ncats = new HashMap<>(registerdLogCategories);
    for (LogCategory c : cats) {
      if (c.getFqName().length() > 30) {
        /**
         * @logging
         * @reason Eine LogCategory wurde registriert, wobei der Laenge des Bezeichers fuer die DB zu lang ist
         * @action Entwickler kontaktieren
         */
        throw new LoggedRuntimeException(LogLevel.Error, GenomeLogCategory.Configuration,
          "LogCategory to long (30 chars max): "
            + c.getFqName());
      }
      ncats.put(c.getFqName(), new LogCategoryWrapper(c));
    }

    registerdLogCategories = ncats;
    LoggingServiceManager.get().getLoggingEventListenerRegistryService()
      .submitEvent(new LogRegisteredCategoryChangedEvent(registerdLogCategories));
  }

  /**
   * Findet in den registrierten LogCategories eine {@link LogCategory} by name.
   *
   * @param catName the cat name
   * @return the category by string
   */
  public static LogCategory getCategoryByString(String catName)
  {
    return registerdLogCategories.get(catName);
  }

  /**
   * Registrierung neuer {@link LogAttribute}s.
   *
   * @param attTypes Beliebige Menge ans {@link LogAttribute}
   */
  public static void registerLogAttributeType(LogAttributeType... attTypes)
  {
    Map<String, LogAttributeType> nlogAttr = new HashMap<>();
    Map<String, LogAttributeType> nlogAttrFiller = new HashMap<>();

    Map<String, LogAttributeType> nsearchKeys = new HashMap<>();

    for (LogAttributeType c : attTypes) {
      if (c.name().length() > 30) {
        /**
         * @logging
         * @reason Eine LogAttributeType wurde registriert, wobei der Laenge des Bezeichers fuer die DB zu lang ist
         * @action Entwickler kontaktieren
         */
        throw new LoggedRuntimeException(LogLevel.Error, GenomeLogCategory.Configuration,
          "LogAttributeType name to long (30 chars max): "
            + c.name());
      }
      if (c.isSearchKey() == true) {
        LogAttributeTypeWrapper wat = new LogAttributeTypeWrapper(c, true);
        nsearchKeys.put(c.name(), wat);
      }
      if (c.getAttributeDefaultFiller() != null) {
        nlogAttrFiller.put(c.name(), c);
      }
      nlogAttr.put(c.name(), c);
    }
    if (nlogAttr.isEmpty() == false) {
      // the renderer will be used. so do directly add LogAttribute, but as weak reference
      registerdLogAttributes = createNewDisposableMap(registerdLogAttributes, nlogAttr);

    }
    if (nlogAttrFiller.isEmpty() == false) {
      // the filler will be used. so do directly add LogAttribute, but as weak reference
      defaultLogAttributes = createNewDisposableMap(defaultLogAttributes, nlogAttrFiller);
    }
    if (nsearchKeys.isEmpty() == false) {
      // this is stored as dump wrapper, no need to have disposable
      searchLogAttributes = createNewMap(searchLogAttributes, nsearchKeys);
    }
    LoggingServiceManager.get().getLoggingEventListenerRegistryService()
      .submitEvent(new LogRegisteredLogAttributesChangedEvent(registerdLogAttributes));
  }

  /**
   * Gets the attribute type by string.
   *
   * @param attrName the attr name
   * @return the attribute type by string
   */
  public static LogAttributeType getAttributeTypeByString(String attrName)
  {
    LogAttributeType ret = registerdLogAttributes.get(attrName);
    if (ret != null) {
      return ret;
    }
    LogAttributeTypeWrapper la = new LogAttributeTypeWrapper(attrName);
    return la;

  }

  /**
   * Adds the pre start log.
   *
   * @param le the le
   */
  public void addPreStartLog(LogWriteEntry le)
  {
    List<LogWriteEntry> tpreStartLogs = preStartLogs;
    if (preStartLogs == null) {
      tpreStartLogs = new ArrayList<LogWriteEntry>();
    }
    tpreStartLogs.add(le);
    preStartLogs = tpreStartLogs;

  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#logPreStart(de.micromata.genome.logging.LogLevel,
   * de.micromata.genome.logging.LogCategory, java.lang.String, de.micromata.genome.logging.LogAttribute[])
   */
  @Override
  public void logPreStart(LogLevel ll, LogCategory cat, String msg, LogAttribute... attributes)
  {
    LogWriteEntry lwe = new LogWriteEntry(ll, cat.getFqName(), msg, attributes);
    addPreStartLog(lwe);

  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#debug(de.micromata.genome.logging.LogCategory, java.lang.String,
   * de.micromata.genome.logging.LogAttribute[])
   */
  @Override
  public void debug(LogCategory cat, String msg, LogAttribute... attributes)
  {
    doLog(LogLevel.Debug, cat, msg, attributes);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#trace(de.micromata.genome.logging.LogCategory, java.lang.String,
   * de.micromata.genome.logging.LogAttribute[])
   */
  @Override
  public void trace(LogCategory cat, String msg, LogAttribute... attributes)
  {
    doLog(LogLevel.Trace, cat, msg, attributes);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#info(de.micromata.genome.logging.LogCategory, java.lang.String,
   * de.micromata.genome.logging.LogAttribute[])
   */
  @Override
  public void info(LogCategory cat, String msg, LogAttribute... attributes)
  {
    doLog(LogLevel.Info, cat, msg, attributes);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#note(de.micromata.genome.logging.LogCategory, java.lang.String,
   * de.micromata.genome.logging.LogAttribute[])
   */
  @Override
  public void note(LogCategory cat, String msg, LogAttribute... attributes)
  {
    doLog(LogLevel.Note, cat, msg, attributes);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#warn(de.micromata.genome.logging.LogCategory, java.lang.String,
   * de.micromata.genome.logging.LogAttribute[])
   */
  @Override
  public void warn(LogCategory cat, String msg, LogAttribute... attributes)
  {
    doLog(LogLevel.Warn, cat, msg, attributes);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#error(de.micromata.genome.logging.LogCategory, java.lang.String,
   * de.micromata.genome.logging.LogAttribute[])
   */
  @Override
  public void error(LogCategory cat, String msg, LogAttribute... attributes)
  {
    doLog(LogLevel.Error, cat, msg, attributes);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#fatal(de.micromata.genome.logging.LogCategory, java.lang.String,
   * de.micromata.genome.logging.LogAttribute[])
   */
  @Override
  public void fatal(LogCategory cat, String msg, LogAttribute... attributes)
  {
    doLog(LogLevel.Fatal, cat, msg, attributes);
  }

  /**
   * Do log.
   *
   * @param ll         the ll
   * @param cat        the cat
   * @param msg        the msg
   * @param attributes the attributes
   */
  public void doLog(LogLevel ll, LogCategory cat, String msg, List<LogAttribute> attributes)
  {
    LogAttribute[] a = new LogAttribute[] {};
    a = attributes.toArray(a);
    doLog(ll, cat, msg, a);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#doLog(de.micromata.genome.logging.LogLevel,
   * de.micromata.genome.logging.LogCategory, java.lang.String, de.micromata.genome.logging.LogAttribute[])
   */
  @Override
  public void doLog(LogLevel ll, LogCategory cat, String msg, LogAttribute... attributes)
  {
    doLog(ll, cat.getFqName(), msg, attributes);
  }

  /**
   * Unwrapp log exceptions.
   *
   * @param lwe the lwe
   */
  protected void unwrappLogExceptions(LogWriteEntry lwe)
  {
    List<LogAttribute> addLogs = null;
    for (LogAttribute la : lwe.getAttributes()) {
      if (la instanceof LogExceptionAttribute) {
        LogExceptionAttribute lea = (LogExceptionAttribute) la;
        if (lea.getException() != null && lea.getException() instanceof WithLogAttributes) {
          WithLogAttributes wa = (WithLogAttributes) lea.getException();
          if (addLogs == null) {
            addLogs = new ArrayList<LogAttribute>();
          }
          Collection<LogAttribute> wattrs = wa.getLogAttributes();
          addLogs.addAll(wattrs);
        }
      }
    }
    if (addLogs == null) {
      return;
    }
    for (LogAttribute la : addLogs) {
      pushAttribute(lwe.getAttributes(), la);
    }
  }

  /**
   * Push attribute.
   *
   * @param attributes the attributes
   * @param le         the le
   */
  public static void pushAttribute(List<LogAttribute> attributes, LogAttribute le)
  {
    if (le == null) {
      return;
    }
    for (LogAttribute a : attributes) {
      if (a.getType() == le.getType()) {
        return;
      }
    }
    attributes.add(le);
  }

  /**
   * Ensure an attribute list contains no duplicates with the same LogAttributeType Duplicates are removed for the list
   * starting at the head. Therefore were there is a duplicate the entry nearest the end of the list will survive.
   *
   * The algoritem has N^2 complexity and should only be used on short lists
   *
   * @param attributes the attributes
   */
  public static void ensureUniqueAttributes(List<LogAttribute> attributes)
  {
    for (int i = 0; i < attributes.size(); ) {
      LogAttribute la = attributes.get(i);
      boolean duplicate = false;
      for (int j = 0; j < i; ++j) {
        LogAttribute la2 = attributes.get(j);
        if (la2.getType() == la.getType()) {
          duplicate = true;
          break;
        }
      }
      if (duplicate == true) {
        attributes.remove(i);
      } else {
        ++i;
      }
    }
  }

  /**
   * Rework log.
   *
   * @param lwe the lwe
   */
  protected void reworkLog(LogWriteEntry lwe)
  {
    unwrappLogExceptions(lwe);

    pushContainedAttributes(lwe.getAttributes(), lwe.getAttributes());

    // zuerst explizit definierte Logattribute
    LoggingContext ctx = LoggingContext.getContext();
    if (ctx != null) {
      Map<LogAttributeType, LogAttribute> attrs = ctx.getAttributes();
      if (attrs != null) {
        for (LogAttributeType at : attrs.keySet()) {
          pushAttribute(lwe.getAttributes(), attrs.get(at));
        }
        pushContainedAttributes(lwe.getAttributes(), attrs.values());
      }
    }

    for (LogAttributeType da : defaultLogAttributes.values()) {
      String v = da.getAttributeDefaultFiller().getValue(lwe, ctx);
      if (StringUtils.isNotEmpty(v) == true) {
        pushAttribute(lwe.getAttributes(), new LogAttribute(da, v));
      }
    }
  }

  /**
   * Durchsucht <code>src</code> nach <code>WithLogAttributes</code> und fügt diese <code>dest</code> hinzu.
   *
   * @param dest the dest
   * @param src  Kann <code>null</code> sein.
   */
  protected void pushContainedAttributes(List<LogAttribute> dest, Collection<LogAttribute> src)
  {
    if (src == null) {
      return;
    }
    List<LogAttribute> addMe = new ArrayList<LogAttribute>();
    for (LogAttribute sa : src) {
      if (sa instanceof WithLogAttributes) {
        Collection<LogAttribute> wattrs = ((WithLogAttributes) sa).getLogAttributes();
        for (LogAttribute ctxa : wattrs) {
          addMe.add(ctxa);
        }
      }
    }
    for (LogAttribute la : addMe) {
      pushAttribute(dest, la);
    }
  }

  /**
   * @return a list of filters
   */
  @SuppressWarnings("unchecked")
  public List<LogWriteFilter> getWriteFilters()
  {
    return writeFilters;
  }

  public List<LogFilter> getReadFilters()
  {

    return readFilters;
  }

  public void setWriteFilters(List<LogWriteFilter> writeFilters)
  {
    this.writeFilters = writeFilters;
  }

  public void setReadFilters(List<LogFilter> readFilters)
  {
    this.readFilters = readFilters;
  }

  /**
   * Write pre start logs.
   *
   * @param clwe the clwe
   */
  private void writePreStartLogs(List<LogWriteEntry> clwe)
  {
    nextLogEntry:
    for (LogWriteEntry lwe : clwe) {
      reworkLog(lwe);
      List<LogWriteFilter> filters = getWriteFilters();
      if (filters != null) {
        for (LogWriteFilter filter : filters) {
          if (filter.match(lwe) == false) {
            continue nextLogEntry;
          }
        }
      }
      if (GLog.isLogEnabled(lwe.getLevel()) == false) {
        return;
      }
      ensureUniqueAttributes(lwe.getAttributes());
      doLogImpl(lwe);
    }
  }

  /**
   * Shorten log attribute.
   *
   * @param lwe the lwe
   * @param la  the la
   */
  protected void shortenLogAttribute(LogWriteEntry lwe, LogAttribute la)
  {
    String value = la.getValueToWrite(lwe);
    if (value == null) {
      return;
    }
    int size = la.getType().maxValueSize();
    if (size <= 0) {
      size = getMaxLogAttrLength();
      Integer maxi = getLogAttributeLimitMap().get(la.getType().name());
      if (maxi != null) {
        size = maxi;
      }
    }
    if (value.length() <= size) {
      return;
    }
    if (size > 0) {
      value = value.substring(0, size);
    }
    la.setValue(value);
  }

  /**
   * Shorten log attributes.
   *
   * @param lwe the lwe
   */
  protected void shortenLogAttributes(LogWriteEntry lwe)
  {
    for (LogAttribute la : lwe.getAttributes()) {
      shortenLogAttribute(lwe, la);
    }
  }

  /**
   * Do log.
   *
   * @param ll         the ll
   * @param cat        the cat
   * @param msg        the msg
   * @param attributes the attributes
   */
  public void doLog(LogLevel ll, String cat, String msg, LogAttribute... attributes)
  {
    LogWriteEntry lwe = new LogWriteEntry(ll, cat, msg, attributes);

    logLwe(lwe);
  }

  /**
   * Initialize logger.
   */
  private void init()
  {
    if (inInitialization == true) {
      return;
    }
    inInitialization = true;
    try {
      doCustomInitialization();
      isInited = true;
    } finally {
      inInitialization = false;
    }
  }

  /**
   * Do custom initialization.
   */
  protected void doCustomInitialization()
  {

  }

  /**
   * Writes a prepared LogWriteEntry
   */
  @Override
  public void logLwe(LogWriteEntry lwe)
  {
    if (isInited == false) {
      init();
    }
    if (inInitialization == true) {
      addPreStartLog(lwe);
      return;
    }
    if (preStartLogs != null) {
      List<LogWriteEntry> clwe = new ArrayList<LogWriteEntry>();
      clwe.addAll(preStartLogs);
      preStartLogs = null;
      writePreStartLogs(clwe);
    }
    reworkLog(lwe);

    List<LogWriteFilter> filters = getWriteFilters();
    if (filters != null) {
      for (LogWriteFilter filter : filters) {
        if (filter.match(lwe) == false) {
          return;
        }
      }
    }
    final LogLevel ll = lwe.getLevel();
    Stats.addLogging(lwe);
    boolean enabled = ll.getLevel() >= LogLevel.Note.getLevel();
    enabled = LoggingServiceManager.get().getLogConfigurationDAO().isLogEnabled(lwe.getLevel(), lwe.getCategory(),
      lwe.getMessage());
    if (enabled == false) {
      return;
    }
    ensureUniqueAttributes(lwe.getAttributes());
    shortenLogAttributes(lwe);
    LoggingServiceManager.get().getLoggingEventListenerRegistryService().filterEvent(
      new LogWriteEntryEvent(lwe),
      event -> doLogImpl(event.getResult()));

  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#doLogImpl(de.micromata.genome.logging.LogWriteEntry)
   */
  @Override
  public abstract void doLogImpl(final LogWriteEntry lwe);

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#selectLogs(java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer,
   * java.lang.String, java.lang.String, java.util.List, int, int, java.util.List,
   * de.micromata.genome.logging.LogEntryCallback)
   */
  @Override
  public void selectLogs(final Timestamp start, final Timestamp end, final Integer loglevel, final String category, final String msg,
    final List<Pair<String, String>> logAttributes, final int startRow, final int maxRow, final List<OrderBy> orderBy, final boolean masterOnly,
    final LogEntryCallback callback)
  {
    if (this.underlyingClientIsAsync() == false) {
      selectLogsSync(start, end, loglevel, category, msg, logAttributes, startRow, maxRow, orderBy, masterOnly, callback);
    } else {
      selectLogsAsync(start, end, loglevel, category, msg, logAttributes, startRow, maxRow, orderBy, masterOnly, callback);
    }

  }

  /**
   * When the {@link Logging#underlyingClientIsAsync()}  is false this is called
   *
   * @param start         start time
   * @param end           end time
   * @param loglevel      the log level to select
   * @param category      the category to select
   * @param msg           the message to filter on
   * @param logAttributes the log attribute to filter on
   * @param startRow      where to start from
   * @param maxRow        how many entries to select
   * @param orderBy       how to order the logs
   * @param masterOnly    if to select the master only
   * @param callback      the callback which contains the returning logs
   */
  public void selectLogsSync(final Timestamp start, final Timestamp end, final Integer loglevel, final String category, final String msg,
    final List<Pair<String, String>> logAttributes, final int startRow, final int maxRow, final List<OrderBy> orderBy, final boolean masterOnly,
    final LogEntryCallback callback)
  {
    final LogEntryFilterCallback filter = new LogEntryFilterCallback(callback,
      LoggingServiceManager.get().getLogConfigurationDAO(), maxRow);
    try {
      selectLogsImpl(start, end, loglevel, category, msg, logAttributes, startRow, maxRow, orderBy, masterOnly, filter);
    } catch (EndOfSearch ex) {
      // just terminate the search
    }
  }

  /**
   * When the {@link Logging#underlyingClientIsAsync()}  is true this is called
   *
   * @param start         start time
   * @param end           end time
   * @param loglevel      the log level to select
   * @param category      the category to select
   * @param msg           the message to filter on
   * @param logAttributes the log attribute to filter on
   * @param startRow      where to start from
   * @param maxRow        how many entries to select
   * @param orderBy       how to order the logs
   * @param masterOnly    if to select the master only
   * @param callback      the callback which contains the returning logs
   */
  public void selectLogsAsync(final Timestamp start, final Timestamp end, final Integer loglevel, final String category, final String msg,
    final List<Pair<String, String>> logAttributes, final int startRow, final int maxRow, final List<OrderBy> orderBy, final boolean masterOnly,
    final LogEntryCallback callback)
  {

    final LogEntryFilterAsyncCallback filter = new LogEntryFilterAsyncCallback(callback,
      LoggingServiceManager.get().getLogConfigurationDAO(), maxRow);

    try {
      selectLogsImpl(start, end, loglevel, category, msg, logAttributes, startRow, maxRow, orderBy, masterOnly, filter);
      filter.doGet();
    } catch (EndOfSearch  | InterruptedException  | ExecutionException ex) {
      // just terminate the search
    }
  }

  /**
   * Select logs impl.
   *
   * @param start         the start
   * @param end           the end
   * @param loglevel      the loglevel
   * @param category      the category
   * @param msg           the msg
   * @param logAttributes the log attributes
   * @param startRow      the start row
   * @param maxRow        the max row
   * @param orderBy       the order by
   * @param masterOnly    the master only
   * @param callback      the callback
   * @throws EndOfSearch the end of search
   */
  protected abstract void selectLogsImpl(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
    List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
    LogEntryCallback callback) throws EndOfSearch;

  @Override
  public void selectLogs(final List<Object> logId, final boolean masterOnly, final LogEntryCallback callback)
  {
    if (underlyingClientIsAsync() == false) {
      selectLogsSync(logId, masterOnly, callback);
    } else {
      selectLogsAsync(logId, masterOnly, callback);
    }
  }

  /**
   * When the {@link Logging#underlyingClientIsAsync()}  is false this is called
   * @param logId the ids of the log entries to select
   * @param masterOnly if to select the master only
   * @param callback the callback containing the log entries when done
   */
  public void selectLogsSync(final List<Object> logId, final boolean masterOnly, final LogEntryCallback callback)
  {
    final LogEntryFilterCallback filter = new LogEntryFilterCallback(callback,
      LoggingServiceManager.get().getLogConfigurationDAO(),
      Integer.MAX_VALUE);

    try {
      selectLogsImpl(logId, masterOnly, filter);
    } catch (EndOfSearch ex) {
      // just terminate the search
    }
  }

  /**
   * When the {@link Logging#underlyingClientIsAsync()}  is true this is called
   * @param logId the ids of the log entries to select
   * @param masterOnly if to select the master only
   * @param callback the callback containing the log entries when done
   */
  public void selectLogsAsync(final List<Object> logId, final boolean masterOnly, final LogEntryCallback callback)
  {

    final LogEntryFilterAsyncCallback filter = new LogEntryFilterAsyncCallback(callback,
      LoggingServiceManager.get().getLogConfigurationDAO(),
      Integer.MAX_VALUE);

    try {
      selectLogsImpl(logId, masterOnly, filter);
      filter.doGet();
    } catch (EndOfSearch | InterruptedException | ExecutionException ex) {
      // just terminate the search
    }
  }

  /**
   * Select logs impl.
   *
   * @param logId      the log id
   * @param masterOnly the master only
   * @param callback   the callback
   * @throws EndOfSearch the end of search
   */
  protected abstract void selectLogsImpl(List<Object> logId, boolean masterOnly, LogEntryCallback callback)
    throws EndOfSearch;

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#getConfigMinLogLevel()
   */
  @Override
  public LogLevel getConfigMinLogLevel()
  {
    return LoggingServiceManager.get().getLogConfigurationDAO().getThreshold();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#getRegisteredAttributes()
   */
  @Override
  public Collection<LogAttributeType> getRegisteredAttributes()
  {
    return BaseLogging.registerdLogAttributes.values();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#getRegisteredCategories()
   */
  @Override
  public Collection<LogCategory> getRegisteredCategories()
  {
    return BaseLogging.registerdLogCategories.values();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.micromata.genome.logging.Logging#getSearchAttributes()
   */
  @Override
  public Collection<LogAttributeType> getSearchAttributes()
  {
    return BaseLogging.searchLogAttributes.values();
  }

  public static boolean isInited()
  {
    return isInited;
  }

  public static void setInited(boolean isInited)
  {
    BaseLogging.isInited = isInited;
  }

  public static List<LogWriteEntry> getPreStartLogs()
  {
    return preStartLogs;
  }

  /**
   * Sets the pre start logs.
   *
   * @param preStartLogs the new pre start logs
   */
  public static void setPreStartLogs(List<LogWriteEntry> preStartLogs)
  {
    BaseLogging.preStartLogs = preStartLogs;
  }

  /**
   * Gets the registerd log categories.
   *
   * @return the registerd log categories
   */
  public static Map<String, LogCategory> getRegisterdLogCategories()
  {
    return registerdLogCategories;
  }

  public static void setRegisterdLogCategories(Map<String, LogCategory> registerdLogCategories)
  {
    BaseLogging.registerdLogCategories = registerdLogCategories;
  }

  public static Map<String, LogAttributeType> getRegisterdLogAttributes()
  {
    return registerdLogAttributes;
  }

  public static void setRegisterdLogAttributes(Map<String, LogAttributeType> registerdLogAttributes)
  {
    BaseLogging.registerdLogAttributes = registerdLogAttributes;
  }

  public int getMaxLogAttrLength()
  {
    return maxLogAttrLength;
  }

  public void setMaxLogAttrLength(int maxLogAttrLength)
  {
    this.maxLogAttrLength = maxLogAttrLength;
  }

  public Map<String, Integer> getLogAttributeLimitMap()
  {
    return logAttributeLimitMap;
  }

  public void setLogAttributeLimitMap(Map<String, Integer> logAttributeLimitMap)
  {
    this.logAttributeLimitMap = logAttributeLimitMap;
  }

  public static Map<String, LogAttributeType> getDefaultLogAttributes()
  {
    return defaultLogAttributes;
  }

  public static void setDefaultLogAttributes(Map<String, LogAttributeType> defaultLogAttributes)
  {
    BaseLogging.defaultLogAttributes = defaultLogAttributes;
  }

  public static Map<String, LogAttributeType> getSearchLogAttributes()
  {
    return searchLogAttributes;
  }

  public static void setSearchLogAttributes(Map<String, LogAttributeType> searchLogAttributes)
  {
    BaseLogging.searchLogAttributes = searchLogAttributes;
  }

}
