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

package de.micromata.genome.db.jpa.genomecore.chronos;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.chronos.spi.jdbc.ChronosStdRecordDO;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDisplayDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDisplayDO;
import de.micromata.genome.jpa.StdRecordDO;

/**
 * Utility to convert from JDBC DO to JPA and vice versa.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SchedJpaTypeConverter
{

  /**
   * Jdbc2jpa.
   *
   * @param target the target
   * @param source the source
   */
  public static void jdbc2jpa(StdRecordDO<Long> target, ChronosStdRecordDO source)
  {
    target.setCreatedAt(source.getCreatedAt());
    target.setCreatedBy(source.getCreatedBy());
    target.setModifiedAt(source.getModifiedAt());
    target.setModifiedBy(source.getModifiedBy());
    target.setUpdateCounter(source.getUpdateCounter());
  }

  /**
   * Jpa2jdbc.
   *
   * @param target the target
   * @param source the source
   */
  public static void jpa2jdbc(ChronosStdRecordDO target, StdRecordDO<Long> source)
  {
    target.setPk(source.getPk());
    target.setCreatedAt(source.getCreatedAt());
    target.setCreatedBy(source.getCreatedBy());
    target.setModifiedAt(source.getModifiedAt());
    target.setModifiedBy(source.getModifiedBy());
    target.setUpdateCounter(source.getUpdateCounter());
  }

  /**
   * From db.
   *
   * @param source the source
   * @return the scheduler do
   */
  public static SchedulerDO fromDb(JpaSchedulerDO source)
  {
    SchedulerDO target = new SchedulerDO();
    fromDb(target, source);
    return target;
  }

  /**
   * From db.
   *
   * @param target the target
   * @param source the source
   */
  public static void fromDb(SchedulerDO target, JpaSchedulerDO source)
  {

    jpa2jdbc(target, source);
    //nshed.setHostName();
    target.setJobMaxRetryCount(source.getJobMaxRetryCount());
    target.setJobRetryTime(source.getJobRetryTime());
    target.setName(source.getName());
    target.setNodeBindingTimeout(source.getNodeBindingTimeout());
    target.setServiceRetryTime(source.getServiceRetryTime());
    target.setThreadPoolSize(source.getThreadPoolSize());
  }

  /**
   * To db.
   *
   * @param source the source
   * @return the jpa scheduler do
   */
  public static JpaSchedulerDO toDb(SchedulerDO source)
  {
    JpaSchedulerDO target = new JpaSchedulerDO();
    toDb(target, source);
    return target;
  }

  /**
   * To db.
   *
   * @param target the target
   * @param source the source
   */
  public static void toDb(JpaSchedulerDO target, SchedulerDO source)
  {
    target.setJobMaxRetryCount(source.getJobMaxRetryCount());
    target.setJobRetryTime(source.getJobRetryTime());
    target.setName(source.getName());
    target.setNodeBindingTimeout(source.getNodeBindingTimeout());
    target.setServiceRetryTime(source.getServiceRetryTime());
    target.setThreadPoolSize(source.getThreadPoolSize());
  }

  /**
   * Display from db.
   *
   * @param jsh the jsh
   * @return the scheduler display do
   */
  public static SchedulerDisplayDO displayFromDb(JpaSchedulerDO jsh)
  {
    SchedulerDisplayDO nshed = new SchedulerDisplayDO();
    fromDb(nshed, jsh);
    return nshed;
  }

  /**
   * From db.
   *
   * @param source the source
   * @return the job result do
   */
  public static JobResultDO fromDb(JpaJobResultDO source)
  {
    JobResultDO target = new JobResultDO();
    fromDb(target, source);
    return target;
  }

  /**
   * From db.
   *
   * @param target the target
   * @param source the source
   */
  public static void fromDb(JobResultDO target, JpaJobResultDO source)
  {
    jpa2jdbc(target, source);
    target.setDuration(source.getDuration());
    target.setExecutionStart(source.getExecutionStart());
    target.setHostName(source.getHostName());
    target.setJobPk(source.getJobPk());
    target.setResultString(source.getResultString());
    target.setRetryCount(source.getRetryCount());
    target.setState(source.getState());
    target.setVm(source.getVm());
  }

  /**
   * To db.
   *
   * @param source the source
   * @return the jpa job result do
   */
  public static JpaJobResultDO toDb(JobResultDO source)
  {
    JpaJobResultDO target = new JpaJobResultDO();
    toDb(target, source);
    return target;
  }

  /**
   * To db.
   *
   * @param target the target
   * @param source the source
   */
  public static void toDb(JpaJobResultDO target, JobResultDO source)
  {
    jdbc2jpa(target, source);
    target.setDuration(source.getDuration());
    target.setExecutionStart(source.getExecutionStart());
    target.setHostName(source.getHostName());
    target.setJobPk(source.getJobPk());
    target.setResultString(StringUtils.substring(source.getResultString(), 0, 1290));
    target.setRetryCount(source.getRetryCount());
    target.setState(source.getState());
    target.setVm(source.getVm());
  }

  /**
   * From db.
   *
   * @param td the td
   * @return the trigger job do
   */
  public static TriggerJobDO fromDb(JpaTriggerJobDO td)
  {
    TriggerJobDO ret = new TriggerJobDO();
    fromDb(ret, td);
    return ret;
  }

  /**
   * From db to display.
   *
   * @param td the td
   * @return the trigger job display do
   */
  public static TriggerJobDisplayDO fromDbToDisplay(JpaTriggerJobDO td)
  {
    TriggerJobDisplayDO ret = new TriggerJobDisplayDO();
    fromDb(ret, td);
    return ret;
  }

  /**
   * From db.
   *
   * @param target the target
   * @param source the source
   */
  public static void fromDb(TriggerJobDO target, JpaTriggerJobDO source)
  {
    jpa2jdbc(target, source);
    target.setCurrentResultPk(source.getCurrentResultPk());
    target.setNextFireTime(source.getNextFireTime());
    target.setHostName(source.getHostName());
    target.setJobArgumentString(source.getJobArgumentString());
    target.setJobDefinitionString(source.getJobDefinitionString());
    target.setJobName(source.getJobName());
    target.setLastScheduledTime(source.getLastScheduledTime());
    target.setState(source.getState());
    target.setTriggerDefintionString(source.getTriggerDefintionString());
    target.setScheduler(source.getScheduler());
    target.setRetryCount(source.getRetryCount());
  }

  /**
   * To db.
   *
   * @param source the source
   * @return the jpa trigger job do
   */
  public static JpaTriggerJobDO toDb(TriggerJobDO source)
  {
    JpaTriggerJobDO target = new JpaTriggerJobDO();
    toDb(target, source);
    return target;
  }

  /**
   * To db.
   *
   * @param target the target
   * @param source the source
   */
  public static void toDb(JpaTriggerJobDO target, TriggerJobDO source)
  {
    jdbc2jpa(target, source);
    target.setCurrentResultPk(source.getCurrentResultPk());
    target.setNextFireTime(source.getNextFireTime());
    target.setHostName(source.getHostName());
    target.setJobArgumentString(source.getJobArgumentString());
    target.setJobDefinitionString(source.getJobDefinitionString());
    target.setJobName(source.getJobName());
    target.setLastScheduledTime(source.getLastScheduledTime());
    target.setState(source.getState());
    target.setTriggerDefintionString(source.getTriggerDefinition());
    target.setScheduler(source.getScheduler());
    target.setRetryCount(source.getRetryCount());
  }

}
