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

package de.micromata.genome.chronos.util;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.chronos.FutureJob;
import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.SchedulerException;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;

/**
 * The Class ClassJobDefinition.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class ClassJobDefinition implements JobDefinition
{

  /**
   * The class to start.
   */
  private final Class< ? extends FutureJob> classToStart;

  /**
   * The bean properties.
   */
  private Map<String, Object> beanProperties = null;

  /**
   * Gets the bean properties.
   *
   * @return the bean properties
   */
  public Map<String, Object> getBeanProperties()
  {
    return beanProperties;
  }

  /**
   * Sets the bean properties.
   *
   * @param beanProperties the bean properties
   */
  public void setBeanProperties(Map<String, Object> beanProperties)
  {
    this.beanProperties = beanProperties;
  }

  /**
   * Instantiates a new class job definition.
   *
   * @param classNameToStart the class name to start
   */
  @SuppressWarnings("unchecked")
  public ClassJobDefinition(final String classNameToStart)
  {
    try {
      classToStart = (Class< ? extends FutureJob>) Class.forName(StringUtils.trim(classNameToStart));
    } catch (Exception ex) {
      throw new RuntimeException("Failure loading class in ClassJobDefinition: " + ex.getMessage(), ex);
    }
  }

  /**
   * Instantiates a new class job definition.
   *
   * @param classToStart the class to start
   */
  public ClassJobDefinition(final Class< ? extends FutureJob> classToStart)
  {
    this.classToStart = classToStart;
  }

  /**
   * Populate.
   *
   * @param fj the fj
   */
  protected void populate(FutureJob fj)
  {
    if (beanProperties != null) {
      try {
        BeanUtils.populate(fj, beanProperties);
      } catch (Exception ex) {
        /**
         * @logging
         * @reason Die JobFactory konnte die bean properties nicht setzen
         * @action TechAdmin kontaktieren
         */
        throw new LoggedRuntimeException(ex, LogLevel.Error, GenomeLogCategory.Scheduler, "Cannot populate properties for bean: "
            + fj.getClass().getName()
            + ": "
            + ex.getMessage());
      }
    }
  }

  /**
   * Creates the instance.
   *
   * @return the future job
   */
  protected FutureJob createInstance()
  {
    FutureJob futureJob;
    try {
      futureJob = classToStart.newInstance();
    } catch (final InstantiationException ex) {
      throw new SchedulerException(ex);
    } catch (final IllegalAccessException ex) {
      throw new SchedulerException(ex);
    }
    return futureJob;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public FutureJob getInstance()
  {
    FutureJob fj = createInstance();
    populate(fj);
    return fj;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String toString()
  {
    return "JobDefinition[class=" + classToStart.getCanonicalName() + "]";
  }
}
