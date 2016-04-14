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

package de.micromata.genome.logging.spi.ifiles;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingServiceManager;

public class IFileTestBase
{
  protected int sizeLimit = -1;
  Logging prevLogging;

  protected IndexFileLoggingImpl logger;

  @Before
  public void setup()
  {
    prevLogging = LoggingServiceManager.get().getLogging();
    logger = new IndexFileLoggingImpl(true);
    if (sizeLimit != -1) {
      logger.setSizeLimit(sizeLimit);
    }
    File target = new File("./target/" + getClass().getSimpleName());
    if (target.exists() == false) {
      target.mkdirs();
    }
    for (File listf : target.listFiles()) {
      listf.delete();
    }
    logger.setLogDir(target);
    logger.setBaseFileName("TestLog");
    logger.initialize();
    LoggingServiceManager.get().setLogging(logger);
  }

  @After
  public void tearDown()
  {
    LoggingServiceManager.get().setLogging(prevLogging);
  }

}
