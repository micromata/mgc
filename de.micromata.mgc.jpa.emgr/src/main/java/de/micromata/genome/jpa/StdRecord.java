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
import java.util.Date;

/**
 * Default interface for a JPA entity.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface StdRecord<PK extends Serializable>extends DbRecord<PK>
{

  /**
   * sets the modified by.
   *
   * @param modifiedBy the new modified by
   */
  public void setModifiedBy(final String modifiedBy);

  /**
   * get the modified by.
   *
   * @param modifiedAt the new modified at
   */
  public void setModifiedAt(final Date modifiedAt);

  /**
   * set the modified at timestamp.
   *
   * @param createdAt the new created at
   */
  public void setCreatedAt(final Date createdAt);

  /**
   * {@inheritDoc}
   *
   */

  public Integer getUpdateCounter();

  /**
   * set the update counter.
   *
   * @param updateCounter the new update counter
   */
  public void setUpdateCounter(final Integer updateCounter);

  /**
   * {@inheritDoc}
   *
   */

  public String getCreatedBy();

  /**
   * {@inheritDoc}
   *
   */

  public void setCreatedBy(final String createdBy);

  /**
   * {@inheritDoc}
   *
   */

  public String getModifiedBy();

  /**
   * {@inheritDoc}
   *
   */

  public Date getModifiedAt();

  /**
   * {@inheritDoc}
   *
   */

  public Date getCreatedAt();

}
