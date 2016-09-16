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

package de.micromata.genome.db.jpa.logging;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import de.micromata.genome.db.jpa.logging.entities.BaseLogAttributeDO;
import de.micromata.genome.db.jpa.logging.entities.BaseLogMasterDO;
import de.micromata.genome.db.jpa.logging.entities.EntityLogSearchAttribute;
import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrCallable;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.logging.EndOfSearch;
import de.micromata.genome.logging.FallbackLogging;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogEntryCallback;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.types.Converter;
import de.micromata.genome.util.types.Pair;

/**
 * Logging implementation based on JPA.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class BaseJpaLoggingImpl<M extends BaseLogMasterDO<?>>extends FallbackLogging
{

  /**
   * The colsize.
   */
  public static final int COLSIZE = 3990;

  /**
   * The colnum.
   */
  public static final int COLNUM = 1;

  /**
   * The Constant log.
   */
  private static final Logger log = Logger.getLogger(BaseJpaLoggingImpl.class);

  /**
   * The Constant propertyNamesByColumnNames.
   */
  private static final Map<String, String> propertyNamesByColumnNames;

  static {
    propertyNamesByColumnNames = new HashMap<String, String>();
    propertyNamesByColumnNames.put("CREATEDAT", "createdAt");
    propertyNamesByColumnNames.put("MODIFIEDAT", "modifiedAt");
    propertyNamesByColumnNames.put("TA_LOG_MASTER", "pk");
    propertyNamesByColumnNames.put("CATEGORY", "category");
    propertyNamesByColumnNames.put("LOGLEVEL", "loglevel");
  }

  public static class SearchColumnDesc
  {
    PropertyDescriptor pdesc;
    int maxLength;

    public SearchColumnDesc(PropertyDescriptor pdesc, int maxLength)
    {
      this.pdesc = pdesc;
      this.maxLength = maxLength;
    }

  }

  /**
   * The searchable attribute properties.
   *
   */
  private Map<String, SearchColumnDesc> searchableAttributeProperties = new HashMap<>();

  /**
   * Entity class of LogMaster.
   *
   * @return
   */
  protected abstract Class<M> getMasterClass();

  /**
   * Create a new empty imstance of the Master Entity.
   *
   * @return
   */
  protected abstract M createNewMaster();

  /**
   * The entity manager to persist the logging.
   *
   * @return
   */
  protected abstract EmgrFactory<DefaultEmgr> getEmgrFactory();

  /**
   * {@inheritDoc}
   *
   */

  @Override
  protected void doCustomInitialization()
  {

  }

  /**
   * Inits the.
   */
  protected void initProps()
  {
    final BeanInfo bi;
    try {
      bi = Introspector.getBeanInfo(getMasterClass());
    } catch (IntrospectionException e) {
      log.error("unable to introspect hibernate DO for logging -> no searchable fields will be available", e);
      return;
    }

    for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
      if (pd.getReadMethod() == null || pd.getWriteMethod() == null) {
        continue;
      }
      EntityLogSearchAttribute ent = pd.getReadMethod().getAnnotation(EntityLogSearchAttribute.class);
      if (ent == null) {
        continue;
      }
      Column col = pd.getReadMethod().getAnnotation(Column.class);
      if (col == null) {
        log.warn("Found EntityLogSearchAttribute but no Column: " + pd);
        continue;
      }
      for (String en : ent.enumName()) {
        searchableAttributeProperties.put(en, new SearchColumnDesc(pd, col.length()));
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void doLogImplWithFallback(LogWriteEntry lwe)
  {
    final M master = createNewMaster();
    copyMasterFields(master, lwe);
    EmgrFactory<DefaultEmgr> mgrfac = getEmgrFactory();
    mgrfac.tx().go(mgr -> {
      mgr.insert(master);
      return null;
    });
  }

  /**
   * Split cols string.
   *
   * @param content the content
   * @param cols the cols
   * @return the string
   */
  public static String splitColsString(String content, List<String> cols)
  {

    if (content == null) {
      return null;
    }

    for (int i = 0; i < COLNUM && content.length() > 0; ++i) {
      String t = Converter.trimUtf8(content, COLSIZE);
      content = StringUtils.substring(content, t.length());
      cols.add(t);
      // alt aber gehend
      // int byteL = COLSIZE;
      // String t = StringUtils.substring(content, 0, byteL);
      //
      // byte[] contentBytes = Converter.bytesFromString(t);
      // if (contentBytes.length > COLSIZE) {
      // int dif = (contentBytes.length - COLSIZE);
      // byteL = t.length() - dif;
      // t = StringUtils.substring(content, 0, byteL);
      // }
      // cols.add(t);

    }
    return content;
  }

  /**
   * Split cols.
   *
   * @param ccontent the ccontent
   * @param defaultInserts the default inserts
   * @return the list
   */
  public static List<List<Object>> splitCols(String ccontent, List<Object> defaultInserts)
  {
    List<List<Object>> il = new ArrayList<List<Object>>();
    int rowNum = 0;
    String c = ccontent;

    while (StringUtils.isNotEmpty(c) == true) {
      List<Object> l = new ArrayList<Object>();
      l.addAll(defaultInserts);
      l.add(rowNum);
      List<String> cols = new ArrayList<String>();
      c = splitColsString(c, cols);
      int i = 1;
      for (; i <= cols.size(); ++i) {
        l.add(cols.get(i - 1));
      }
      for (; i <= COLNUM; ++i) {
        l.add("");
      }
      il.add(l);
      ++rowNum;
    }
    return il;
  }

  private String shortendToCol(SearchColumnDesc colDesc, String value)
  {
    if (StringUtils.length(value) <= colDesc.maxLength) {
      return value;
    }
    return value.substring(0, colDesc.maxLength);
  }

  /**
   * Copy master fields.
   *
   * @param master the master
   * @param lwe the lwe
   */
  private void copyMasterFields(M master, LogWriteEntry lwe)
  {
    master.setPk((Long) lwe.getLogEntryIndex());
    master.setCategory(lwe.getCategory());
    master.setLoglevel((short) lwe.getLevel().getLevel());
    master.setMessage(lwe.getMessage());
    master.setShortmessage(lwe.getMessage());
    if (lwe.getTimestamp() != 0) {
      master.setCreatedAt(new Date(lwe.getTimestamp()));
    }
    if (lwe.getAttributes() == null) {
      return;
    }
    for (LogAttribute attr : lwe.getAttributes()) {
      boolean handled = false;

      final String normalizedValue = getLengthNormalizedValue(lwe, attr);
      if (attr.getType().isSearchKey() == true) {
        SearchColumnDesc colDesc = searchableAttributeProperties.get(attr.getType().name());
        if (colDesc != null) {
          String val = shortendToCol(colDesc, normalizedValue);
          try {
            colDesc.pdesc.getWriteMethod().invoke(master, val);
            handled = true;
          } catch (Exception e) {
            log.warn("Exception while setting searchable attribute '" + colDesc.pdesc.getName() + "'", e);
          }
        } else {
          log.warn("JpaLogging; No column found for searchable attribute: " + attr.getType().name());
        }
      }
      if (handled == false) {
        List<Object> deflist = new ArrayList<Object>();
        List<List<Object>> list = splitCols(normalizedValue, deflist);
        long partNr = 0;
        for (List<Object> la : list) {

          // for (Object contentPartO : la) {
          String contentPart = (String) la.get(1);
          BaseLogAttributeDO<?> logAttr = master.createAddAttribute();
          // IdClass id = logAttr.new IdClass();
          logAttr.setBaseLogAttribute(attr.getType().name());
          logAttr.setDatarow(partNr++);
          logAttr.setDatacol1(contentPart);
        }
      }
    }

  }

  /**
   * Copy master fields.
   *
   * @param lwe the lwe
   * @param master the master
   * @param masterOnly the master only
   */
  private void copyMasterFields(LogEntry lwe, M master, boolean masterOnly)
  {
    lwe.setCategory(master.getCategory());
    lwe.setLogLevel(LogLevel.getLevelFrom(master.getLoglevel()));
    lwe.setMessage(master.getMessage());
    lwe.setTimestamp(master.getCreatedAt().getTime());
    lwe.setLogEntryIndex(master.getPk());
    List<LogAttribute> attributes = new ArrayList<LogAttribute>();
    lwe.setAttributes(attributes);

    for (Map.Entry<String, SearchColumnDesc> pe : searchableAttributeProperties.entrySet()) {
      try {
        String value = (String) pe.getValue().pdesc.getReadMethod().invoke(master,
            ArrayUtils.EMPTY_OBJECT_ARRAY);
        if (value != null) {
          lwe.getAttributes().add(new LogAttribute(getAttributeTypeByString(pe.getKey()), value));
        }
      } catch (Exception e) {
        log.error("Exception while reading searchable LogAttribute '" + pe.getValue().pdesc.getName() + "'; "
            + e.getMessage());
      }
    }
    if (masterOnly == true) {
      return;
    }
    appendLogAttributes(lwe, master);
  }

  private void appendLogAttributes(LogEntry lwe, M master)
  {
    Collection<BaseLogAttributeDO<M>> attrs = (Collection) master.getAttributes();
    List<BaseLogAttributeDO<M>> sortedAttrs = new ArrayList<>();
    sortedAttrs.addAll(attrs);
    sortedAttrs.sort((first, second) -> {
      int c = first.getBaseLogAttribute().compareTo(second.getBaseLogAttribute());
      if (c != 0) {
        return c;
      }
      c = ObjectUtils.compare(first.getDatarow(), second.getDatarow());
      return c;
    });
    String lastAttr = null;
    LogAttribute lastLogAttr = null;
    String notFoundAttrType = null;
    StringBuilder sb = new StringBuilder();
    for (BaseLogAttributeDO<M> attrDo : sortedAttrs) {
      if (StringUtils.equals(notFoundAttrType, attrDo.getBaseLogAttribute()) == true) {
        continue;
      }
      if (StringUtils.equals(lastAttr, attrDo.getBaseLogAttribute()) == false) {
        lastAttr = attrDo.getBaseLogAttribute();
        if (lastLogAttr != null) {
          lastLogAttr.setValue(sb.toString());

        }
        LogAttributeType type = getAttributeTypeByString(attrDo.getBaseLogAttribute());
        if (type == null) {
          log.warn("LogAttributeType '" + attrDo.getBaseLogAttribute() + "' is not registered");
          notFoundAttrType = attrDo.getBaseLogAttribute();
          continue;
        }

        lastLogAttr = new LogAttribute(type, "");
        lwe.getAttributes().add(lastLogAttr);
        sb.setLength(0);
      }
      sb.append(attrDo.getDatacol1());

    }
    if (lastLogAttr != null) {
      lastLogAttr.setValue(sb.toString());
      lastLogAttr = null;
    }
  }

  /**
   * Gets the length normalized value.
   *
   * @param lwe the lwe
   * @param la the la
   * @return the length normalized value
   */
  protected String getLengthNormalizedValue(LogWriteEntry lwe, LogAttribute la)
  {
    String value = la.getValueToWrite(lwe);
    if (la.getType().maxValueSize() > 0) {
      return LogAttribute.shorten(value, la.getType().maxValueSize());
    }
    return value;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  protected void selectLogsImpl(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, final int startRow, final int maxRow, List<OrderBy> orderBy,
      final boolean masterOnly, final LogEntryCallback callback) throws EndOfSearch
  {

    final StopWatch sw = new StopWatch();
    sw.start();

    final StringBuilder queryStringBuilder = new StringBuilder(
        "select lm from " + getMasterClass().getName() + "  as lm");
    if (masterOnly == false) {
      queryStringBuilder.append(" left outer join fetch lm.attributes");
    }

    boolean firstWhere = true;
    // final List<Object> args = new ArrayList<Object>();
    Map<String, Object> argmap = new HashMap<>();
    if (start != null) {
      firstWhere = addWhere(queryStringBuilder, firstWhere, "lm.createdAt >= :createdAtMin");
      argmap.put("createdAtMin", start);
    }
    if (end != null) {
      firstWhere = addWhere(queryStringBuilder, firstWhere, "lm.createdAt <= :createdAtMax");
      argmap.put("createdAtMax", end);
    }
    if (loglevel != null) {
      firstWhere = addWhere(queryStringBuilder, firstWhere, "lm.loglevel >= :logLevelMin");
      argmap.put("logLevelMin", new Short(loglevel.shortValue()));
    }
    if (StringUtils.isNotBlank(category) == true) {
      firstWhere = addWhere(queryStringBuilder, firstWhere, "lm.category = :category");
      argmap.put("category", category);
    }
    if (StringUtils.isNotBlank(msg) == true) {
      firstWhere = addWhere(queryStringBuilder, firstWhere, "lm.shortmessage like :message");
      argmap.put("message", msg + "%");
    }
    if (logAttributes != null) {
      int attrNum = 0;
      for (Pair<String, String> pat : logAttributes) {
        ++attrNum;
        LogAttributeType at = getAttributeTypeByString(pat.getFirst());
        if (at == null) {
          GLog.warn(GenomeLogCategory.Configuration, "SelLogs; Cannot find LogAttribute: " + pat.getFirst());
          continue;
        }
        if (at.isSearchKey() == false) {
          GLog.warn(GenomeLogCategory.Configuration,
              "SelLogs; LogAttribute not searchable: " + pat.getFirst());
          continue;
        }
        if (StringUtils.contains(pat.getSecond(), "%") == false) {
          firstWhere = addWhere(queryStringBuilder, firstWhere, at.columnName(), " = :attr" + attrNum);
        } else {
          firstWhere = addWhere(queryStringBuilder, firstWhere, at.columnName(), " like :attr" + attrNum);
        }

        argmap.put("attr" + attrNum, pat.getSecond());
      }
    }

    if (CollectionUtils.isEmpty(orderBy) == false) {
      queryStringBuilder.append(" order by ");
      boolean first = true;
      for (OrderBy order : orderBy) {
        if (first == true) {
          first = false;
        } else {
          queryStringBuilder.append(", ");
        }
        String propertyName = convertColumnNameToPropertyName(order.getColumn());
        queryStringBuilder.append("lm.").append((propertyName != null) ? propertyName : order.getColumn()); // eventually
        // requires
        // translation
        // to
        // propertynames
        queryStringBuilder.append(order.isDescending() ? " desc" : " asc");
      }
    }

    EmgrFactory<DefaultEmgr> mgrfac = getEmgrFactory();
    mgrfac.runInTrans(new EmgrCallable<Void, DefaultEmgr>()
    {
      @Override
      public Void call(DefaultEmgr mgr)
      {

        Query query = mgr.createQuery(queryStringBuilder.toString());
        for (Map.Entry<String, Object> arg : argmap.entrySet()) {
          query.setParameter(arg.getKey(), arg.getValue());
        }
        query.setFirstResult(startRow);
        if (masterOnly == true) {
          query.setMaxResults(maxRow);
        } else {
          query.setMaxResults(maxRow * 10); // pessimistic assumption:
          // 10 attributes per
          // master entry
        }

        List<M> res = query.getResultList();

        for (M lmDo : res) {
          LogEntry le = new LogEntry();
          copyMasterFields(le, lmDo, masterOnly);
          try {
            callback.onRow(le);
          } catch (EndOfSearch eos) {
            break;
          }
        }
        return null;
      }
    });
  }

  /**
   * Convert column name to property name.
   *
   * @param column the column
   * @return the string
   */
  private String convertColumnNameToPropertyName(String column)
  {
    final String rVal = propertyNamesByColumnNames.get(column);
    if (rVal != null) {
      return rVal;
    }
    return column;//not to make .toLowerCase();
  }

  /**
   * Adds the where.
   *
   * @param sb the sb
   * @param firstWhere the first where
   * @param strings the strings
   * @return true, if successful
   */
  private boolean addWhere(StringBuilder sb, boolean firstWhere, String... strings)
  {
    if (firstWhere == true) {
      sb.append(" where ");
    } else {
      sb.append(" and ");
    }
    for (String s : strings) {
      sb.append(s);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  protected void selectLogsImpl(List<Object> logIds, final boolean masterOnly, LogEntryCallback callback)
      throws EndOfSearch
  {
    EmgrFactory<DefaultEmgr> mgrfac = getEmgrFactory();
    for (final Object logId : logIds) {
      LogEntry le = mgrfac.runWoTrans(new EmgrCallable<LogEntry, DefaultEmgr>()
      {
        @Override
        public LogEntry call(DefaultEmgr mgr)
        {
          Query query;
          if (masterOnly == true) {
            query = mgr
                .createQuery("select lm from " + getMasterClass().getName() + " as lm where lm.pk = :pk");
          } else {
            query = mgr.createQuery("select lm from " + getMasterClass().getName()
                + " as lm left outer join fetch lm.attributes where lm.pk = :pk");
          }
          query.setParameter("pk", logId);
          LogEntry le = new LogEntry();
          M lmDo = (M) query.getSingleResult();
          copyMasterFields(le, lmDo, masterOnly);
          return le;

        }
      });
      callback.onRow(le);
    }
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String formatLogId(Object logId)
  {
    return ((Long) logId).toString();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Object parseLogId(String logId)
  {
    return Long.valueOf(logId);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean supportsFulltextSearch()
  {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean supportsSearch()
  {
    return true;
  }

}