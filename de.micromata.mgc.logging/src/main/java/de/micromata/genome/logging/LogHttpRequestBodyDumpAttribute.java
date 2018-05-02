package de.micromata.genome.logging;

import javax.servlet.http.HttpServletRequest;

/**
 * Logs the body of a Http Request
 */
public class LogHttpRequestBodyDumpAttribute extends LogHttpRequestDumpAttribute
{
  /**
   * Instantiates a new log http request body dump attribute.
   * @param req the request
   */
  public LogHttpRequestBodyDumpAttribute(HttpServletRequest req)
  {
    super(req,false, true);
  }
}
