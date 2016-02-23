package de.micromata.genome.jpa;

import java.util.Map;

/**
 * The Interface EmgrFactoryService. GENOME-1689
 */
public interface EmgrFactoryService
{

  void register(EmgrFactory<?> emgrFactory);

  /**
   * Register.
   *
   * @param emgrFac the emgr fac
   */
  //void register(EmgrFactory<Emgr<?>> emgrFac);

  /**
   * Gets the factories.
   *
   * @return the factories
   */
  public Map<String, EmgrFactory<?>> getFactories();
}
