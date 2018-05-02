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
