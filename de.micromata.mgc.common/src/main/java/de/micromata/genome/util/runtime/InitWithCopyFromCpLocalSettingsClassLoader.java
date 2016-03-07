package de.micromata.genome.util.runtime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * It is a wrapper for a localSettings loader, which copies a local Settings from from classpath.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class InitWithCopyFromCpLocalSettingsClassLoader implements Supplier<LocalSettingsLoader>
{
  private static final Logger LOG = Logger.getLogger(InitWithCopyFromCpLocalSettingsClassLoader.class);

  private Supplier<LocalSettingsLoader> localSettingsLoaderSupplier;

  public InitWithCopyFromCpLocalSettingsClassLoader(Supplier<LocalSettingsLoader> localSettingsLoaderSupplier)
  {
    this.localSettingsLoaderSupplier = localSettingsLoaderSupplier;
  }

  @Override
  public LocalSettingsLoader get()
  {
    LocalSettingsLoader loader = localSettingsLoaderSupplier.get();
    if (loader.localSettingsExists() == true) {
      return loader;
    }
    File f = loader.getLocalSettingsFile();
    try {
      InputStream is = getClass().getClassLoader().getResourceAsStream(f.getName());
      if (is != null) {
        FileUtils.copyInputStreamToFile(is, f);
        IOUtils.closeQuietly(is);
        LOG.info("Copied from CP: " + f.getName() + " to " + f.getAbsolutePath());
      } else {
        LOG.warn("Cannot find localsettings in CP: " + f.getName());
      }

    } catch (IOException ex) {
      LOG.error("Failure to write local settings file: " + f.getAbsolutePath() + ": " + ex.getMessage(), ex);
    }

    return loader;
  }

}
