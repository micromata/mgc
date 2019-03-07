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

package de.micromata.genome.logging.spi;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.types.Pair;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 * Writes the logconfiguration in a file.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class FileLogConfigurationDAOImpl extends PropLogConfigurationDAOBase
{

  /**
   * The fq file name.
   */
  private String fqFileName;

  /**
   * The project root.
   */
  private String projectRoot;

  /**
   * The log config file.
   */
  private String logConfigFile = "/dev/extrc/config/GenomeLogConfig.properties";

  /**
   * Load properties.
   *
   * @return the properties
   */
  protected Properties loadProperties()
  {
    String pfile = getPropertyFileName();
    File pf = new File(pfile);
    if (pf.exists() == false) {
      return null;
    }
    Properties p = new Properties();
    try {
      p.load(new FileInputStream(pf));
    } catch (IOException ex) {
      /**
       * @logging
       * @reason IO Error while opening property file for Logging Configuration
       * @action korrekt file
       */
      GLog.warn(GenomeLogCategory.Configuration, "Failure opening logconfig file: " + ex.getMessage(), //
          new LogAttribute(GenomeAttributeType.Miscellaneous, pfile), new LogExceptionAttribute(ex));

    }
    return p;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.spi.PropLogConfigurationDAOBase#buildPattern()
   */
  @Override
  protected void buildPattern()
  {
    Properties p = loadProperties();
    if (p == null) {
      return;
    }
    List<Pair<String, Integer>> npattern = new ArrayList<>();
    for (Map.Entry<Object, Object> me : p.entrySet()) {
      String pattern = Objects.toString(me.getKey(), StringUtils.EMPTY);
      String v = Objects.toString(me.getValue(), StringUtils.EMPTY);
      LogLevel ll = LogLevel.valueOf(v);
      int ilev = ll.getLevel();
      if (pattern.equals(THRESHOLD_NAME) == true) {

        setMaxThreshold(ilev);
      } else {
        npattern.add(new Pair<String, Integer>(pattern, ilev));
      }
    }
    Collections.sort(npattern, new Comparator<Pair<String, Integer>>()
    {

      /**
       * @param o1 the object1
       * @param o2 the object2
       * @return the compare result
       */
      @Override
      public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2)
      {
        return o2.getFirst().length() - o1.getFirst().length();
      }
    });
    List<Pair<Matcher<String>, Integer>> ncpattern = new ArrayList<Pair<Matcher<String>, Integer>>();
    for (Pair<String, Integer> pp : npattern) {
      ncpattern.add(new Pair<Matcher<String>, Integer>(matcherFactory.createMatcher(pp.getFirst()), pp.getSecond()));
    }
    synchronized (this) {
      pattern = ncpattern;
    }
  }

  /**
   * Store properties.
   *
   * @param p the p
   */
  protected void storeProperties(Properties p)
  {
    String pfile = getPropertyFileName();
    try {

      File pf = new File(pfile);
      p.store(new FileOutputStream(pf), "# Genome LogConfiguration");
    } catch (IOException ex) {
      throw new LoggedRuntimeException(LogLevel.Warn, GenomeLogCategory.Configuration,
          "Cannot store GenomeLogConfiguration: "
              + ex.getMessage(),
          new LogAttribute(GenomeAttributeType.Miscellaneous, pfile), new LogExceptionAttribute(ex));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.spi.PropLogConfigurationDAOBase#setLogLevel(de.micromata.genome.logging.LogLevel,
   * java.lang.String)
   */
  @Override
  public void setLogLevel(LogLevel logLevel, String patternString)
  {
    Properties p = loadProperties();
    if (p == null) {
      p = new Properties();
    }
    if (logLevel == null) {
      p.remove(patternString);
    } else {
      p.setProperty(patternString, logLevel.toString());
    }
    storeProperties(p);
    buildPattern();
  }

  public String getFqFileName()
  {
    return fqFileName;
  }

  public void setFqFileName(String fileName)
  {
    this.fqFileName = fileName;
  }

  public String getProjectRoot()
  {
    return projectRoot;
  }

  public void setProjectRoot(String projectRoot)
  {
    this.projectRoot = projectRoot;
  }

  public String getLogConfigFile()
  {
    return logConfigFile;
  }

  public void setLogConfigFile(String projectSubDir)
  {
    this.logConfigFile = projectSubDir;
  }

  protected String getPropertyFileName()
  {
    if (StringUtils.isNotBlank(fqFileName) == true) {
      return fqFileName;
    }
    return projectRoot + logConfigFile;
  }

}
