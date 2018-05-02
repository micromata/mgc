package de.micromata.genome.logging;

import javax.servlet.http.HttpServletRequest;

/**
 * Logs the head of a Http Request
 */
public class LogHttpRequestHeaderDumpAttribute extends LogHttpRequestDumpAttribute
{
  /**
   * Instantiates a new log http request header dump attribute.
   * @param req the request
   */
  public LogHttpRequestHeaderDumpAttribute(HttpServletRequest req)
  {
    super(req, true, false);
  }
}
