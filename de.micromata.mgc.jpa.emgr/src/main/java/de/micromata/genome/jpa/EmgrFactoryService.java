package de.micromata.genome.jpa;

import java.util.List;

/**
 * The Interface EmgrFactoryService. GENOME-1689
 */
public interface EmgrFactoryService
{

  /**
   * Register.
   *
   * @param emgrFac the emgr fac
   */

  void register(EmgrFactory<?> emFac);

  /**
   * Gets the factories.
   *
   * @return the factories
   */
  public List<EmgrFactory<?>> getFactories();
}
