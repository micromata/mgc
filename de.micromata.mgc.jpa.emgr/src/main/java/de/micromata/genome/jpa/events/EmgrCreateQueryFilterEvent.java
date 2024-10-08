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

package de.micromata.genome.jpa.events;

import jakarta.persistence.Query;

import de.micromata.genome.jpa.IEmgr;

/**
 * Wrapps creating a query.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class EmgrCreateQueryFilterEvent extends EmgrFilterEvent<Query>
{

  /**
   * The sql.
   */
  private String sql;

  /**
   * Instantiates a new emgr create query filter event.
   *
   * @param emgr the emgr
   * @param sql the sql
   */
  public EmgrCreateQueryFilterEvent(IEmgr<?> emgr, String sql)
  {
    super(emgr);
    this.sql = sql;
  }

  public String getSql()
  {
    return sql;
  }

  public void setSql(String sql)
  {
    this.sql = sql;
  }
}
