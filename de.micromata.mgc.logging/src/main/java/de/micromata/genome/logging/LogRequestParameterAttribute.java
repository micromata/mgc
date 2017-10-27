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

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Dump the HTTPRequest.
 *
 * @author roger@micromata.de
 */
public class LogRequestParameterAttribute extends LogAttribute
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1200175792217777415L;

  /**
   * Fuer Logging gibt alle requestparameter in einer Zeile aus.
   *
   * @param request the request
   * @param excludingKeys the excluding keys
   * @return the request for logging
   */
  public static String getRequestForLogging(HttpServletRequest request, String... excludingKeys)
  {
    if (request == null || request.getParameterMap().isEmpty() == true) {
      return "<no params>";
    }

    StringBuilder sb = new StringBuilder();
    for (Enumeration< ? > en = request.getParameterNames(); en.hasMoreElements() == true;) {
      String s = (String) en.nextElement();
      String[] values = request.getParameterValues(s);
      for (String v : values) {
        for (String exKey : excludingKeys) {
          if (StringUtils.equals(s, exKey) == true) {
            v = "****";
            break;
          }
        } // for
        sb.append(s).append("=").append(v).append("\n");
      }
    }
    return sb.toString();
  }

  /**
   * Gets the request for logging.
   *
   * @param request the request
   * @param excludingKeys the excluding keys
   * @return the request for logging
   */
  public static String getRequestForLogging(Map<String, Object> request, String... excludingKeys)
  {
    if (request == null || request.isEmpty() == true) {
      return "<no params>";
    }
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, Object> me : request.entrySet()) {
      final String key = me.getKey();
      String value = ObjectUtils.toString(me.getValue());
      for (String exKey : excludingKeys) {
        if (StringUtils.equals(key, exKey) == true) {
          value = "****";
          break;
        }
      } // for

      sb.append(key).append("=").append(value).append("\n");
    }
    return sb.toString();
  }

  /**
   * Builds the value.
   *
   * @param req the req
   * @param excludingKeys the excluding keys
   */
  private void buildValue(HttpServletRequest req, String... excludingKeys)
  {
    setType(GenomeAttributeType.RequestParams);
    if (req == null || req.getParameterMap().isEmpty() == true) {
      setValue("<no parameter>");
      return;
    }
    String v = getRequestForLogging(req, excludingKeys);
    setValue(v);
  }

  /**
   * Instantiates a new log request parameter attribute.
   *
   * @param req the req
   * @param excludingKeys the excluding keys
   */
  @SuppressWarnings("unchecked")
  public LogRequestParameterAttribute(HttpServletRequest req, String... excludingKeys)
  {
    setType(GenomeAttributeType.RequestParams);
    if (req == null) {
      setValue("no request");
      return;
    }
    buildValue(req);
  }

  /**
   * Loggen einer Map.
   * 
   * @param reqmap Die Map
   * @param excludingKeys Keys in der Map, deren Werte nicht geloggt werden sollen wie beispielsweise Passworte, Kundennummern etc.
   */
  public LogRequestParameterAttribute(Map<String, Object> reqmap, String... excludingKeys)
  {
    setType(GenomeAttributeType.RequestParams);
    if (reqmap == null) {
      setValue("no request");
      return;
    }
    String v = getRequestForLogging(reqmap, excludingKeys);
    setValue(v);
  }
}
