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

import java.util.ArrayList;
import java.util.List;

/**
 * Better Use PluginContext.runInXXX().
 *
 * Erzeugt einen neuen Thread Context Classloader muss innerhalb try/finally aufgerufen werden, wobei im finally-Block
 * ThreadContextClassLoaderScope.restore() aufgerufen werden muss.
 *
 * @author roger@micromata.de
 *
 */
public class ThreadContextClassLoaderScope
{

  /**
   * The previous class loader.
   */
  private ClassLoader previousClassLoader;

  /**
   * The new class loader.
   */
  private ClassLoader newClassLoader;

  /**
   * The Thread local scopes.
   */
  public static ThreadLocal<List<ThreadContextClassLoaderScope>> ThreadLocalScopes = new ThreadLocal<List<ThreadContextClassLoaderScope>>()
  {

    @Override
    protected List<ThreadContextClassLoaderScope> initialValue()
    {
      return new ArrayList<ThreadContextClassLoaderScope>();
    }

  };

  /**
   * Push.
   *
   * @param scope the scope
   */
  public static void push(ThreadContextClassLoaderScope scope)
  {
    ThreadLocalScopes.get().add(scope);
  }

  /**
   * Pop.
   *
   * @param scope the scope
   */
  public static void pop(ThreadContextClassLoaderScope scope)
  {
    List<ThreadContextClassLoaderScope> scopes = ThreadLocalScopes.get();
    if (scopes.isEmpty() == true) {
      return;
    }
    scopes.remove(scope);
  }

  public static List<ThreadContextClassLoaderScope> getThreadContextScopes()
  {
    return ThreadLocalScopes.get();
  }

  /**
   * Peek.
   *
   * @return the thread context class loader scope
   */
  public static ThreadContextClassLoaderScope peek()
  {
    List<ThreadContextClassLoaderScope> scopes = ThreadLocalScopes.get();
    if (scopes.isEmpty() == true) {
      return null;
    }
    return scopes.get(scopes.size() - 1);
  }

  /**
   * Instantiates a new thread context class loader scope.
   *
   * @param newClassLoader darf nicht null sein
   */
  public ThreadContextClassLoaderScope(ClassLoader newClassLoader)
  {
    this.newClassLoader = newClassLoader;
    previousClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(newClassLoader);

    push(this);
  }

  /**
   * Restore.
   */
  public void restore()
  {
    Thread.currentThread().setContextClassLoader(previousClassLoader);
    pop(this);
  }

  public ClassLoader getNewClassLoader()
  {
    return newClassLoader;
  }

  public void setNewClassLoader(ClassLoader newClassLoader)
  {
    this.newClassLoader = newClassLoader;
  }

  public ClassLoader getPreviousClassLoader()
  {
    return previousClassLoader;
  }

  public void setPreviousClassLoader(ClassLoader previousClassLoader)
  {
    this.previousClassLoader = previousClassLoader;
  }
}
