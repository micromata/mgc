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
 * Some standard classLoader.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class ClassLoaderServices
{
  /**
   * The type Class for name loader service.
   */
  public static class ClassForNameLoaderService implements ClassLoaderService
  {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
      return Class.forName(name);
    }
  }

  /**
   * The type Class loader class loader service.
   */
  public static class ClassLoaderClassLoaderService implements ClassLoaderService
  {
    private ClassLoader classLoader;

    public ClassLoaderClassLoaderService(ClassLoader classLoader)
    {
      this.classLoader = classLoader;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
      return classLoader.loadClass(name);
    }

  }

  /**
   * For name class loader service.
   *
   * @return the class loader service
   */
  public ClassLoaderService forName()
  {
    return new ClassForNameLoaderService();
  }

  /**
   * By class loader class loader service.
   *
   * @param classLoader the class loader
   * @return the class loader service
   */
  public ClassLoaderService byClassLoader(ClassLoader classLoader)
  {
    return new ClassLoaderClassLoaderService(classLoader);
  }

  /**
   * Thread context class loader service.
   *
   * @return the class loader service
   */
  public ClassLoaderService threadContext()
  {
    return new ClassLoaderClassLoaderService(Thread.currentThread().getContextClassLoader());
  }
}
