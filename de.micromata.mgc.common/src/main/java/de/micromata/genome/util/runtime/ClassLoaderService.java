package de.micromata.genome.util.runtime;

/**
 * Basic service to load a class.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface ClassLoaderService
{
  /**
   * Load class by name.
   * 
   * @param name
   * @return
   * @throws ClassNotFoundException
   */
  Class<?> loadClass(String name) throws ClassNotFoundException;
}
