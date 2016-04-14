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

package de.micromata.genome.util.types;

/**
 * Just a few constants for Time in milli seconds.
 *
 * @author roger@micromata.de
 */
public interface TimeInMillis
{

  /**
   * The Constant SECOND.
   */
  public static final long SECOND = 1000;

  /**
   * The Constant MINUTE.
   */
  public static final long MINUTE = SECOND * 60;

  /**
   * The Constant HOUR.
   */
  public static final long HOUR = MINUTE * 60;

  /**
   * The Constant DAY.
   */
  public static final long DAY = HOUR * 24;

  /**
   * The Constant WEEK.
   */
  public static final long WEEK = DAY * 7;

  /**
   * The Constant MAX_MONTH.
   */
  public static final long MAX_MONTH = DAY * 31;

  /**
   * The Constant YEAR.
   */
  public static final long YEAR = DAY * 365;
}
