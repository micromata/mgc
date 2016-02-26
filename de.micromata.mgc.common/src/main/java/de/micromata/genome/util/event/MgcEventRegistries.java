package de.micromata.genome.util.event;

/**
 * Standard registry.
 * 
 * Before using this registry, consider to create own registry instances for your domain specific usage.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class MgcEventRegistries
{
  private static SimpleEventClassRegistry eventClassRegistry = new SimpleEventClassRegistry("mgcclassregistry");
  private static SimpleEventInstanceRegistry eventInstanceRegistry = new SimpleEventInstanceRegistry(
      "mgcclassregistry");

  public static MgcEventClassRegistry getEventClassRegistry()
  {
    return eventClassRegistry;
  }

  public static McEventInstanceRegistry getEventInstanceRegistry()
  {
    return eventInstanceRegistry;
  }
}
