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

package de.micromata.genome.util.runtime;

/**
 * The Interface CallableX1.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <V> the value type
 * @param <ARG1> the generic type
 * @param <EX> the generic type
 */
public interface CallableX1<V, ARG1, EX extends Throwable>
{

  /**
   * Call.
   *
   * @param arg1 the arg1
   * @return the v
   * @throws EX the ex
   */
  public V call(ARG1 arg1) throws EX;
}
