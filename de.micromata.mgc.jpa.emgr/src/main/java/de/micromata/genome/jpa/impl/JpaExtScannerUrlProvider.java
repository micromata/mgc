package de.micromata.genome.jpa.impl;

import java.net.URL;
import java.util.Collection;

/**
 * Provides Class Loader URLs where to look for Entity definitions.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JpaExtScannerUrlProvider
{
  /**
   * Where to look for resources and entities.
   * 
   * @return
   */
  Collection<URL> getScannUrls();
}
