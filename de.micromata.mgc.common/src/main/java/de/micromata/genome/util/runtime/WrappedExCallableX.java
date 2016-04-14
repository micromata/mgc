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
 * The Class WrappedExCallableX.
 *
 * @author roger
 * @param <V> the value type
 * @param <DECLEX> the generic type
 * @param <THROWEX> the generic type
 */
public abstract class WrappedExCallableX<V, DECLEX extends Throwable, THROWEX extends Throwable>
    extends AbtractCallableX<V, THROWEX>
{
  public Class<?> getDeclaredWrappedException()
  {

    Class<?> myClass = getClass();
    Class<?> fcls = GenericsUtils.getConcretTypeParameter(WrappedExCallableX.class, myClass, 1);
    if (fcls != null) {
      return fcls;
    }
    // fcls = GenericsUtils.getClassGenericTypeFromSuperClass(myClass, 1, null);
    //
    // if (fcls != null)
    // return fcls;
    return Exception.class;
  }

  /**
   * Call wrapped.
   *
   * @return the v
   * @throws DECLEX the declex
   */
  public V callWrapped() throws DECLEX
  {
    try {
      return call();
    } catch (Throwable ex) { // NOSONAR "Illegal Catch" framework
      Class<?> declaredEx = getDeclaredWrappedException();
      if (declaredEx.isAssignableFrom(ex.getClass()) == true) {
        throw (DECLEX) ex;
      }
      throw wrappException(ex);
    }
  }
}
