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
