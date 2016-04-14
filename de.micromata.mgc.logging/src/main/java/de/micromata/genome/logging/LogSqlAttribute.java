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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Dumps a SQL prepared statement.
 *
 * @author roger@micromata.de
 */
public class LogSqlAttribute extends LogAttribute
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1141444817020107417L;

  /**
   * The sql.
   */
  private String sql;

  /**
   * The args.
   */
  private Object[] args;

  /**
   * Instantiates a new log sql attribute.
   *
   * @param sql the sql
   * @param args the args
   */
  public LogSqlAttribute(String sql, Object[] args)
  {
    super(GenomeAttributeType.SqlStatement, "");
    this.sql = sql;
    this.args = args;
  }

  @Override
  public String getValue()
  {
    if (StringUtils.isNotEmpty(super.getValue()) == true) {
      return super.getValue();
    }
    String formated = formatValue(sql, args);
    super.setValue(formated);
    return formated;
  }

  /**
   * The Constant sqlToFormat.
   */
  private static final DateFormat sqlToFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  /**
   * Format sql date.
   *
   * @param date the date
   * @return the string
   */
  public static String formatSqlDate(java.util.Date date)
  {
    String ts = sqlToFormat.format(date);
    return "TO_TIMESTAMP('" + ts + "', 'YYYY-MM-DD HH24:MI')";
  }

  /**
   * Format sql arg.
   *
   * @param arg the arg
   * @return the string
   */
  public static String formatSqlArg(Object arg)
  {
    if (arg == null) {
      return "NULL";
    }
    if (arg instanceof String) {
      return '\'' + StringEscapeUtils.escapeSql((String) arg) + '\'';
    }
    if (arg instanceof java.util.Date) {
      return formatSqlDate((java.util.Date) arg);
    }
    // TODO (Rx) rrk (low) format Date, Timestamp und BigDecimal fuer sql log anzeige
    return ObjectUtils.toString(arg);
  }

  /**
   * Format value.
   *
   * @param sql the sql
   * @param args the args
   * @return the string
   */
  public static String formatValue(String sql, Object[] args)
  {
    if (args == null || args.length == 0) {
      return sql;
    }

    StringBuilder sb = new StringBuilder();
    int argi = 0;
    String rest = sql;
    for (; argi < args.length; ++argi) {
      int idx = rest.indexOf('?');
      if (idx == -1) {
        break;
      }
      sb.append(rest.substring(0, idx));
      sb.append(formatSqlArg(args[argi]));
      rest = rest.substring(idx + 1);
    }
    sb.append(rest);
    return sb.toString();
  }
}
