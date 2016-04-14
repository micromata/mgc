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
 * The Class AbtractCallableX.
 *
 * @author roger
 * @param <V> return value
 * @param <EX> declared exception
 */
public abstract class AbtractCallableX<V, EX extends Throwable>implements CallableX<V, EX>
{

  public Class<?> getDeclaredException()
  {
    Class<?> cls = GenericsUtils.getConcretTypeParameter(CallableX.class, this.getClass(), 1);
    if (cls == null) {
      return Exception.class;
    }
    return cls;
  }

  /**
   * Checks if is declared exception.
   *
   * @param <V> the value type
   * @param <EX> the generic type
   * @param callable the callable
   * @param ex the ex
   * @return true, if is declared exception
   */
  public <V, EX extends Throwable> boolean isDeclaredException(CallableX<V, EX> callable, Throwable ex)
  {
    Class<?> decl = getDeclaredException();
    return decl.isAssignableFrom(ex.getClass()) == true;
  }

  /**
   * Checks if is declared exception.
   *
   * @param ex the ex
   * @return true, if is declared exception
   */
  public boolean isDeclaredException(Throwable ex)
  {
    Class<?> decl = getDeclaredException();
    return decl.isAssignableFrom(ex.getClass()) == true;
  }

  /**
   * Wrapp exception.
   *
   * @param ex the ex
   * @return the runtime exception
   */
  public RuntimeException wrappException(Throwable ex)
  {
    if (ex instanceof RuntimeException) {
      return (RuntimeException) ex;
    }
    if (ex instanceof Error) {
      throw (Error) ex;
    }
    return new RuntimeException(ex);
  }

  // public V callWrapped() throws EX
  // {
  // try {
  // return call();
  // } catch (Exception ex) {
  // if (isDeclaredException(ex) == true)
  // throw (EX) ex;
  // throw wrappException(ex);
  // }
  // }
}
