package de.micromata.genome.jpa;

import java.util.Map;
import java.util.WeakHashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class EmgrFactoryServiceImpl.
 */
public class EmgrFactoryServiceImpl implements EmgrFactoryService
{

  /** The factories. */
  private Map<String, EmgrFactory<?>> factories = new WeakHashMap<>();

  /**
   * Gets the factories.
   *
   * @return the factories
   */
  public Map<String, EmgrFactory<?>> getFactories()
  {
    return factories;
  }

  /**
   * {@inheritDoc}
   *
  */

  @Override
  public void register(EmgrFactory<?> emgrFac)
  {
    factories.put(emgrFac.getUnitName(), emgrFac);
  }
}
