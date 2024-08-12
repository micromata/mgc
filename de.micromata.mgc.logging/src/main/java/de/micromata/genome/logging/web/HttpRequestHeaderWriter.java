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

import org.apache.commons.lang3.Validate;

import jakarta.servlet.http.HttpServletRequest;
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
