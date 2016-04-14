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

import org.apache.commons.lang.ObjectUtils;

/**
 * Collection of methods to weak cast types.
 *
 * @author roger@micromata.de
 */
public class CoerceUtils
{

  /**
   * To double.
   *
   * @param o the o
   * @return the double
   */
  public static double toDouble(Object o)
  {
    if (o instanceof Number) {
      return ((Number) o).doubleValue();
    }
    if (o instanceof String) {
      return Double.parseDouble((String) o);
    }
    throw new RuntimeException("Cannot coerce value to double: " + ObjectUtils.toString(o));
  }

}
