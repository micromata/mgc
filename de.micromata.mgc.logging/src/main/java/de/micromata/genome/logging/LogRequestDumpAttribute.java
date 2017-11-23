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

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Loggs all information for a Request.
 *
 * @author roger@micromata.de
 */
public class LogRequestDumpAttribute extends LogAttribute
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -2784719493213636303L;

  /**
   * Instantiates a new log request dump attribute.
   *
   * @param req the req
   */
  public LogRequestDumpAttribute(HttpServletRequest req)
  {
    super(GenomeAttributeType.HttpRequestDump, genHttpRequestDump(req));
  }

  /**
   * Gen http request dump.
   *
   * @param req the req
   * @return the string
   */
  @SuppressWarnings("unchecked")
  public static String genHttpRequestDump(HttpServletRequest req)
  {
    StringBuilder sb = new StringBuilder();

    sb.append("requestURL: ").append(req.getRequestURL()).append('\n')//
        .append("servletPath: ").append(req.getServletPath()).append('\n')//
        .append("requestURI: ").append(req.getRequestURI()).append('\n') //
        .append("queryString: ").append(req.getQueryString()).append('\n') //
        .append("pathInfo: ").append(req.getPathInfo()).append('\n')//
        .append("contextPath: ").append(req.getContextPath()).append('\n') //
        .append("characterEncoding: ").append(req.getCharacterEncoding()).append('\n') //
        .append("localName: ").append(req.getLocalName()).append('\n') //
        .append("contentLength: ").append(req.getContentLength()).append('\n') //
        ;
    sb.append("Header:\n");
    for (Enumeration<String> en = req.getHeaderNames(); en.hasMoreElements();) {
      String hn = en.nextElement();
      sb.append("  ").append(hn).append(": ").append(req.getHeader(hn)).append("\n");
    }
    sb.append("Attr: \n");
    Enumeration en = req.getAttributeNames();
    for (; en.hasMoreElements();) {
      String k = (String) en.nextElement();
      Object v = req.getAttribute(k);
      sb.append("  ").append(k).append(": ").append(Objects.toString(v, StringUtils.EMPTY)).append('\n');
    }

    return sb.toString();
  }
}
