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
      LOG.info("LocalSetting already exists: " + loader.getLocalSettingsFile().getAbsolutePath());
      return loader;
    }
    File f = loader.getLocalSettingsFile();
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(f.getName())) {
      if (is != null) {
        FileUtils.copyInputStreamToFile(is, f);
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
