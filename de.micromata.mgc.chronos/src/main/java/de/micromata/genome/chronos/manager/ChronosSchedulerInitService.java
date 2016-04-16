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

package de.micromata.genome.chronos.manager;

/**
 * Provides jobs and scheduler definitions for intialize.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface ChronosSchedulerInitService
{
  void initSchedulerManager(SchedulerManager schedManager);

  default void copyAddSchedulerManager(SchedulerManager source, SchedulerManager target)
  {
    target.getScheduleFactories().addAll(source.getScheduleFactories());
    target.getJobs().putAll(source.getJobs());
    target.getGlobalFilter().addAll(source.getGlobalFilter());
    target.getStandardJobs().addAll(source.getStandardJobs());
    target.getStartupJobs().addAll(source.getStartupJobs());
    target.setVirtualHostName(source.getVirtualHostName());
    target.setMinNodeBindTime(source.getMinNodeBindTime());
    target.setMinRefreshInMillis(source.getMinRefreshInMillis());
    target.setStartRefreshInMillis(source.getStartRefreshInMillis());
    target.setMaxRefreshInMillis(source.getMaxRefreshInMillis());
    target.setRestartOwnJobsOnBooting(source.isRestartOwnJobsOnBooting());
    target.setRestartOwnJobTimeoutInMillis(source.getRestartOwnJobTimeoutInMillis());
  }
}
