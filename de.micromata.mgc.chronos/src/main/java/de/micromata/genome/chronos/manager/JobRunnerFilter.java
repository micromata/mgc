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
 * A global Filter which wrapps Chronos Job running.
 * 
 * @author roger@micromata.de
 * 
 */
public interface JobRunnerFilter
{

  /**
   * Filter.
   *
   * @param jobFilterCain the job filter cain
   * @return the object
   * @throws Exception the exception
   */
  public Object filter(JobFilterChain jobFilterCain) throws Exception;

  /**
   * Return a priority for executing filters Lower value is lower priority. The Filter will applied later.
   *
   * @return the priority
   */
  public int getPriority();
}
