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
 * Extension to common lang exception utils
 * 
 * @author Roger Rene Kommer (r.kommer@micromata.de)
 * 
 */
public class ExceptionUtils extends org.apache.commons.lang.exception.ExceptionUtils
{
  /**
   * Rethrow an exception into Error, RuntimeException or Exception declared. If non, a RuntimeException will be used.
   * 
   * @param ex
   * @param declared
   * @throws Exception
   */
  public static void wrappException(Throwable ex, Class<? extends Exception>... declared) throws Exception
  {
    if (ex instanceof Error) {
      throw (Error) ex;
    }
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    for (Class<? extends Exception> ce : declared) {
      if (ce.isAssignableFrom(ex.getClass()) == true) {
        throw (Exception) ex;
      }
    }
    throw new RuntimeException(ex);
  }

  /**
   * Unwrapp an rethrow Throwable as Exception or Error.
   *
   * @param ex the ex
   * @throws Exception the exception
   */
  public static void throwUnwrappedThrowableToException(Throwable ex) throws Exception
  {
    if (ex instanceof Error) {
      throw (Error) ex;
    }
    if (ex instanceof Exception) {
      throw (Exception) ex;
    }
    throw new IllegalArgumentException("Exception is Neither error or Exception: " + ex.getClass(), ex);
  }
}
