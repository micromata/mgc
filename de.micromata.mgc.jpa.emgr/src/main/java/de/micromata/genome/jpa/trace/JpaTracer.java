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

package de.micromata.genome.jpa.trace;

import de.micromata.genome.util.runtime.CallableX;

/**
 * Implements a Tracer for JPA executions.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public interface JpaTracer
{

  /**
   * Execute a tracer Statement.
   * 
   * @param <T> the generic type
   * @param sql the sql
   * @param keyValues the key values
   * @param callback the callback
   * @return the return value of the callback.
   */
  <T> T execute(String sql, Object[] keyValues, CallableX<T, RuntimeException> callback);
}
