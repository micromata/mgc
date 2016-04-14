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
 *
 */
public class ClassLoaderServices
{
  public static class ClassForNameLoaderService implements ClassLoaderService
  {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
      return Class.forName(name);
    }
  }

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

  public ClassLoaderService forName()
  {
    return new ClassForNameLoaderService();
  }

  public ClassLoaderService byClassLoader(ClassLoader classLoader)
  {
    return new ClassLoaderClassLoaderService(classLoader);
  }

  public ClassLoaderService combined(ClassLoader... classLoaders)
  {
    return new ClassLoaderClassLoaderService(new CombinedClassLoader(classLoaders));
  }

  public ClassLoaderService threadContext()
  {
    return new ClassLoaderClassLoaderService(Thread.currentThread().getContextClassLoader());
  }
}
