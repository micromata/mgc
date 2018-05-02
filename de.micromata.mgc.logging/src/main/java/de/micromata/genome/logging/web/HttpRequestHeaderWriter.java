package de.micromata.genome.logging.web;

import org.apache.commons.lang3.Validate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class HttpRequestHeaderWriter
{
  private HttpServletRequest request;

  public HttpRequestHeaderWriter(HttpServletRequest request)
  {
    Validate.notNull(request);
    this.request = request;
  }

  public String create(){
    StringBuilder sb = new StringBuilder();
    sb.append(request.getMethod());
    sb.append(" ");
    sb.append(request.getRequestURI());
    sb.append(" ");
    sb.append(request.getProtocol());
    sb.append("\n");

    for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements();) {
      String name = headerNames.nextElement();
      for (Enumeration<String> headerValues = request.getHeaders(name); headerValues.hasMoreElements();) {
        String value = headerValues.nextElement();
        sb.append(name);
        sb.append(": ");
        sb.append(value);
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}
