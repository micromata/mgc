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

package de.micromata.genome.jpa;

import java.io.Serializable;

import jakarta.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * Marker-Interface for all Entities with a primary key.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface DbRecord<PK extends Serializable>extends Serializable
{
  static final Logger LOG = Logger.getLogger(DbRecord.class);

  /**
   * get the PK of the record.
   *
   * @return the pk
   */
  @Transient
  public PK getPk();

  /**
   * Sets the PK of the record.
   *
   * @param pk the new pk
   */
  public void setPk(PK pk);

  default Long getPkAsLong()
  {
    PK pk = getPk();
    if (pk == null) {
      return null;
    }
    if (pk instanceof Number) {
      return ((Number) pk).longValue();
    }
    LOG.error("Cannot cast pk to Long: " + getClass().getName() + "; " + pk.getClass());
    return null;
  }
}
