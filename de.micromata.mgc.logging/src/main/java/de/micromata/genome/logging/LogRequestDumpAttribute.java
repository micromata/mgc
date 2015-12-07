/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome Core
//
// Author    roger@micromata.de
// Created   19.03.2008
// Copyright Micromata 19.03.2008
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;

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

    sb.append("requestURL: ").append(req.getRequestURL()).append("\n")//
        .append("servletPath: ").append(req.getServletPath()).append("\n")//
        .append("requestURI: ").append(req.getRequestURI()).append("\n") //
        .append("queryString: ").append(req.getQueryString()).append("\n") //
        .append("pathInfo: ").append(req.getPathInfo()).append("\n")//
        .append("contextPath: ").append(req.getContextPath()).append("\n") //
        .append("characterEncoding: ").append(req.getCharacterEncoding()).append("\n") //
        .append("localName: ").append(req.getLocalName()).append("\n") //
        .append("contentLength: ").append(req.getContentLength()).append("\n") //
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
      sb.append("  ").append(k).append(": ").append(ObjectUtils.toString(v)).append("\n");
    }

    return sb.toString();
  }
}
