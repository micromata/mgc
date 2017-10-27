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

package de.micromata.genome.db.jdbc.trace;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import de.micromata.genome.util.types.Converter;
import org.apache.commons.lang3.StringUtils;

/**
 * Rendern eines SQL als Literal.
 *
 * @author roger
 */
public class SqlLiteralArgRenderer implements SqlArgRenderer
{

  /**
   * The Constant sqlDateFormatter.
   */
  public static final ThreadLocal<SimpleDateFormat> sqlDateFormatter = new ThreadLocal<SimpleDateFormat>()
  {

    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd"); // TODO what is standard format?
    }
  };

  /**
   * The Constant sqlTimeFormatter.
   */
  public static final ThreadLocal<SimpleDateFormat> sqlTimeFormatter = new ThreadLocal<SimpleDateFormat>()
  {

    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("HH:mm"); // TODO what is standard format?
    }
  };

  /**
   * The Constant sqlTimestampFormatter.
   */
  public static final ThreadLocal<SimpleDateFormat> sqlTimestampFormatter = new ThreadLocal<SimpleDateFormat>()
  {

    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"); // TODO what is standard format?
    }
  };

  @Override
  public String renderSqlArg(Object arg)
  {
    if (arg instanceof String) {
      // TODO replace with proper SQL escaping
      return "'" + StringUtils.replace((String) arg, "'", "''") + "'";
    }
    if (arg instanceof Integer || arg instanceof Long || arg instanceof Short || arg instanceof Byte) {
      return ObjectUtils.toString(arg);
    }
    if (arg instanceof BigDecimal) {
      return ObjectUtils.toString(arg); // TODO
    }
    if (arg instanceof Date) {
      return "#" + sqlDateFormatter.get().format((Date) arg) + "#";
    }
    if (arg instanceof Time) {
      return "#" + sqlTimeFormatter.get().format((Time) arg) + '#';
    }
    if (arg instanceof Timestamp) {
      return "#" + sqlTimestampFormatter.get().format((Timestamp) arg) + '#';
    }
    if (arg instanceof java.util.Date) {
      return "#" + sqlTimestampFormatter.get().format((java.util.Date) arg) + '#';
    }
    // TODO andere typen unterstuetzen
    return ObjectUtils.toString(arg);
  }

  @Override
  public String renderLiteralStatement(String sql, Object[] args)
  {
    if (args == null) {
      return sql;
    }
    StringBuilder sb = new StringBuilder();
    List<String> tokenList = Converter.parseStringTokens(sql, "?", true);
    int argIndex = 0;
    for (String tk : tokenList) {
      if (tk.equals("?") == true) {
        if (argIndex < args.length) {
          sb.append(renderSqlArg(args[argIndex++]));
        } else {
          sb.append("?");
        }
      } else {
        sb.append(tk);
      }
    }
    return sb.toString();
  }
}
