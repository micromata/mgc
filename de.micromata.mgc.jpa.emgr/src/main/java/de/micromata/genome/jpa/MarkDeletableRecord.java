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

/**
 * An record can be marked as deleted.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <PK>
 */
public interface MarkDeletableRecord<PK extends Serializable>extends DbRecord<PK>
{

  /**
   * Is marked as deleted.
   *
   * @return true, if is deleted
   */
  boolean isDeleted();

  /**
   * Sets the deleted.
   *
   * @param deleted the new deleted
   */
  void setDeleted(boolean deleted);
}
