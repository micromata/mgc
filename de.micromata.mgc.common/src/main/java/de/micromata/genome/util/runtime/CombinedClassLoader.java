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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections15.iterators.IteratorEnumeration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Hilfclassloader der mehrere ClassLoader abfragt.
 *
 * Parent URL wird ignoriert
 *
 * @author roger@micromata.de
 */
public class CombinedClassLoader extends URLClassLoader
{

  /**
   * The parents.
   */
  private List<ClassLoader> parents = new ArrayList<ClassLoader>();

  /**
   * Instantiates a new combined class loader.
   *
   * @param parentsClassLoaders the parents class loaders
   */
  public CombinedClassLoader(ClassLoader... parentsClassLoaders)
  {
    super(new URL[0]);
    for (ClassLoader cl : parentsClassLoaders) {
      addParent(cl);
    }
  }

  /**
   * Instantiates a new combined class loader.
   *
   * @param parentsClassLoaders the parents class loaders
   */
  public CombinedClassLoader(List<ClassLoader> parentsClassLoaders)
  {
    super(new URL[0]);
    for (ClassLoader cl : parentsClassLoaders) {
      addParent(cl);
    }
  }

  /**
   * Contains class loader.
   *
   * @param other the other
   * @return true, if successful
   */
  public boolean containsClassLoader(ClassLoader other)
  {
    for (ClassLoader cl : parents) {
      if (cl == other) {
        return true;
      }
    }
    return false;
  }

  /**
   * Fuegt den ClassLoader hinzu, wenn er nicht bereits vorhanden ist.
   *
   * @param cl Der neue ClassLoader
   */
  public void addParent(ClassLoader cl)
  {
    if (cl != null && containsClassLoader(cl) == false) {
      parents.add(cl);

    }
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException
  {
    // the parent will do it.
    return super.findClass(name);
  }

  /**
   * Returns the search path of URLs for loading classes and resources. This includes the original list of URLs
   * specified to the constructor, along with any URLs subsequently appended by the addURL() method.
   */
  @Override
  public URL[] getURLs()
  {
    return super.getURLs();

    // URL[][] urls = new URL[parents.size() + 1][];
    // int idx = 0;
    // for (ClassLoader cl : parents) {
    // if (cl instanceof URLClassLoader) {
    // urls[idx] = ((URLClassLoader) cl).getURLs();
    // } else {
    // urls[idx] = new URL[0];
    // }
    // ++idx;
    // }
    // urls[idx] = super.getURLs();
    // int size = 0;
    // for (URL[] u : urls) {
    // size += u.length;
    // }
    // URL[] ret = new URL[size];
    // int curOffs = 0;
    // for (URL[] u : urls) {
    // if (u.length > 0) {
    // System.arraycopy(u, 0, ret, curOffs, u.length);
    // curOffs += u.length;
    // }
    // }
    // return ret;
  }

  @Override
  public URL findResource(String name)
  {
    for (ClassLoader cl : parents) {
      URL resource = cl.getResource(name);
      if (resource != null) {
        return resource;
      }
    }
    return null;
    // return super.findResource(name);
  }

  @Override
  public URL getResource(String name)
  {
    for (ClassLoader cl : parents) {
      URL resource = cl.getResource(name);
      if (resource != null) {
        return resource;
      }
    }
    // return super.getResource(name);
    return null;
  }

  @Override
  public InputStream getResourceAsStream(String name)
  {
    for (ClassLoader cl : parents) {
      InputStream resource = cl.getResourceAsStream(name);
      if (resource != null) {
        return resource;
      }
    }
    return null;
  }

  @Override
  public Enumeration<URL> findResources(String name) throws IOException
  {
    List<URL> res = new ArrayList<URL>();
    for (ClassLoader cl : parents) {
      Enumeration<URL> resource = cl.getResources(name);
      CollectionUtils.addAll(res, resource);
    }
    return new IteratorEnumeration<URL>(res.iterator());
  }

  @Override
  public Enumeration<URL> getResources(String name) throws IOException
  {
    return findResources(name);
    // for (ClassLoader cl : parents) {
    // Enumeration<URL> resource = cl.getResources(name);
    // List<URL> t = EnumerationUtils.toList(resource);
    // if (t.isEmpty() == false) {
    // return new IteratorEnumeration<URL>(t.iterator());
    // }
    // }
    // final List<URL> empty = Collections.emptyList();
    // return new IteratorEnumeration<URL>(empty.iterator());
    // return super.getResources(name);
  }

  @Override
  protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
  {
    try {
      return loadClass(name);
    } catch (ClassNotFoundException e) {

    }

    // fallback to normal classloading
    return super.loadClass(name, resolve);
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException
  {
    for (ClassLoader cl : parents) {
      try {
        Class<?> cls = cl.loadClass(name);
        return cls;
      } catch (ClassNotFoundException ign) {

      }
    }
    throw new ClassNotFoundException(name);
    // return super.loadClass(name, false);
  }

  @Override
  protected String findLibrary(String libname)
  {
    return super.findLibrary(libname);
  }

  // @Override
  // protected Package getPackage(String name)
  // {
  // for (ClassLoader cl : parents) {
  // Package p = cl.getPa
  // }
  // return super.getPackage(name);
  // }

  // @Override
  // protected Package[] getPackages()
  // {
  //    
  // return super.getPackages();
  // }

  public List<ClassLoader> getParents()
  {
    return parents;
  }

  public void setParents(List<ClassLoader> parents)
  {
    this.parents = parents;
  }
}
