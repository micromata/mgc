/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   08.05.2007
// Copyright Micromata 08.05.2007
//
/////////////////////////////////////////////////////////////////////////////
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
   * In most cases combines are not what you want.
   *
   * @param newClassLoader must not null
   * @deprecated use PluginContext.runIn...(...)
   */
  public ThreadContextClassLoaderScope(CombinedClassLoader newClassLoader)
  {
    previousClassLoader = Thread.currentThread().getContextClassLoader();
    if (newClassLoader.getParents().size() == 1 && newClassLoader.getParents().get(0) == previousClassLoader) {
      return;
    }
    this.newClassLoader = newClassLoader;
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
