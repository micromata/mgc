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

import org.apache.commons.lang3.math.NumberUtils;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValMessagesException;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IFileLoggingLocalSettingsConfigModel extends BaseLoggingLocalSettingsConfigModel
{

  @ALocalSettingsPath(defaultValue = "100", comment = "Size limit of the log files in MB")
  private String sizeLimit;
  @ALocalSettingsPath(defaultValue = "logs", comment = "Directory where to store log files")
  private String logPath;
  @ALocalSettingsPath(defaultValue = "Genome", comment = "Base file name of the log files")
  private String baseFileName;

  public IFileLoggingLocalSettingsConfigModel()
  {
    setTypeId("ifile");
  }

  @Override
  public void validate(ValContext ctx)
  {
    if (sizeLimit == null || NumberUtils.isDigits(sizeLimit) == false) {
      ctx.directError(sizeLimit, "sizeLimit must be megabyte integer between 1 and 2000");
    } else {
      int maxLimit = Integer.parseInt(sizeLimit);
      if (maxLimit < 1 || maxLimit > 2000) {
        ctx.directError(sizeLimit, "sizeLimit must be megabyte integer between 1 and 2000");
      }
    }
    // TODO more
    super.validate(ctx);
  }

  @Override
  public Logging createLogging()
  {
    ValContext ctx = new ValContext();
    validate(ctx);
    if (ctx.hasErrors() == true) {
      throw new ValMessagesException("Invalid Logging configuration", ctx.getMessages());
    }
    IndexFileLoggingImpl ret = new IndexFileLoggingImpl(true);
    ret.setMaxLogAttrLength(Integer.parseInt(sizeLimit));
    ret.setBaseFileName(getBaseFileName());
    ret.setLogDir(new File(getLogPath()));
    ret.setSizeLimit(Integer.parseInt(getSizeLimit()) * 1024 * 1024);
    ret.initialize();
    return ret;
  }

  public String getSizeLimit()
  {
    return sizeLimit;
  }

  public void setSizeLimit(String sizeLimit)
  {
    this.sizeLimit = sizeLimit;
  }

  public String getLogPath()
  {
    return logPath;
  }

  public void setLogPath(String logPath)
  {
    this.logPath = logPath;
  }

  public String getBaseFileName()
  {
    return baseFileName;
  }

  public void setBaseFileName(String baseFileName)
  {
    this.baseFileName = baseFileName;
  }

}
