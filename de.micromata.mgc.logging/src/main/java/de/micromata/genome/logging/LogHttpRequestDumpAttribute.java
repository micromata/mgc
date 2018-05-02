//
// Copyright (C) 2010-2018 Micromata GmbH
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

import de.micromata.genome.logging.web.HttpRequestHeaderWriter;
import de.micromata.genome.logging.web.MultipleReadRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Logs all information for a Http Request
 */
public class LogHttpRequestDumpAttribute extends LogAttribute
{
  private HttpServletRequest request;
  private boolean logHttpHeader;
  private boolean logHttpBody;
  private boolean httpRequestDumpGenerated = false;
  private String generatedHttpRequestDump;

  /**
   * Instantiates a new log http request dump attribute.
   *
   * @param req the request
   */
  public LogHttpRequestDumpAttribute(HttpServletRequest req)
  {
    this(req, true, true);
  }

  /**
   * Instantiates a new log http request dump attribute.
   * @param req the request
   * @param logHttpHeader Indicates if the http header should be logged
   * @param logHttpBody Indicates if the http body should be logged
   */
  public LogHttpRequestDumpAttribute(HttpServletRequest req, boolean logHttpHeader, boolean logHttpBody)
  {
    super(GenomeAttributeType.HttpRequestDump, "");
    request = req;
    this.logHttpHeader = logHttpHeader;
    this.logHttpBody = logHttpBody;
  }

  @Override
  public String getValue()
  {
    if(httpRequestDumpGenerated == false){
      generatedHttpRequestDump = generateHttpRequestDump();
      httpRequestDumpGenerated = true;
    }
    return generatedHttpRequestDump;
  }

  /**
   * Generate http body dump.
   *
   * @return the dumped request body
   */
  public String generateHttpRequestDump()
  {
    try{
      StringBuilder dump = new StringBuilder();
      if(logHttpHeader){
        dump.append(generateHttpRequestHeaderDump());
      }

      if(logHttpHeader && logHttpBody){
        dump.append("\n");
      }

      if(logHttpBody){
        String body = generateHttpRequestBodyDump();
        if(body != null){
          dump.append(body);
        }
      }
      return dump.toString();
    }catch (Exception ex){ // NOSONAR by design
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      ex.printStackTrace(printWriter);
      return "Exception while dumping request:\n" + stringWriter.getBuffer().toString();
    }
  }

  private String generateHttpRequestHeaderDump()
  {
    HttpRequestHeaderWriter writer = new HttpRequestHeaderWriter(request);
    return writer.create();
  }

  private String generateHttpRequestBodyDump() throws IOException
  {
    MultipleReadRequestWrapper multiReadRequest = MultipleReadRequestWrapper.findMultipleReadRequestInWrappedRequests(request);
    if(multiReadRequest == null){
      return "Can not dump request body, because the request must be wrapped by a MultipleReadRequestWrapper";
    }
    return multiReadRequest.getCacheAsString();
  }
}
