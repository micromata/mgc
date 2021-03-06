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

import org.apache.commons.lang3.StringUtils;

/**
 * Escapes untrusted data for writing it safely into a log file.
 */
public class Escape
{
  /**
   * Escapes untrusted data for writing it safely into a log file.
   * Prevents Log forging/Log injection
   *
   * Log forging vulnerabilities occur when data from an untrusted source (ie. userinput)
   * is written to an application/system log file without escaping the.
   *
   * Used Example Code from here: http://www.baeldung.com/jvm-log-forging
   *
   * @param untrustedData the data we want to escape so we can trust it.
   * @return escaped data that could be safely written to a log file
   */
  public static String forLog(String untrustedData)
  {
    String escapedData = untrustedData;

    escapedData = StringUtils.replace(escapedData, "\n", "\\n");
    escapedData = StringUtils.replace(escapedData, "\r", "\\r");
    escapedData = StringUtils.replace(escapedData, "\t", "\\t");

    return escapedData;
  }


  /**
   * Replaces all Null-Bytes in the value
   * This is required, because i.e. Postgres fails with exception "invalid byte sequence 0x00"
   *
   * @param value the value where to replace the null values in
   * @return the modified value
   */
  public static String nullBytes(String value)
  {
    return StringUtils.replace(value, "\u0000", "\\0");
  }
}
