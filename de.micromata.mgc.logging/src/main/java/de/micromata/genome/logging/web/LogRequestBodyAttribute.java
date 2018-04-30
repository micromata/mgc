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
package de.micromata.genome.logging.web;

import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.LogAttribute;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Logs already read body for a Request.
 */
public class LogRequestBodyAttribute extends LogAttribute
{
    /**
     * Instantiates a new log request body attribute.
     *
     * @param req the req
     */
    public LogRequestBodyAttribute(HttpServletRequest req)
    {
      super(GenomeAttributeType.HttpRequestBodyDump, generateHttpRequestDump(req));
    }

  /**
   * Generated http body dump.
   *
   * @param req the req
   * @return the string
   */
  public static String generateHttpRequestDump(HttpServletRequest req)
  {
    try{
      MultipleReadRequestWrapper multiReadRequest = MultipleReadRequestWrapper.findMultipleReadRequestInWrappedRequests(req);
      if(multiReadRequest == null){
        return "Can not dump request body, because the request must be wrapped by a MultipleReadRequestWrapper";
      }
      return multiReadRequest.getCacheAsString();
    }catch (Exception ex){ // NOSONAR by design
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      ex.printStackTrace(printWriter);
      return "Exception while dumping request body:\n" + stringWriter.getBuffer().toString();
    }
  }
}
