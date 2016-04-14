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

package de.micromata.genome.db.jpa.xmldump.api;

import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * Prepare an entity after restoring from xml.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface JpaXmlBeforePersistListener
{

  /**
   * Prepare persist.
   *
   * @param emgr the emgr
   * @param entityMetadata the entity metadata
   * @param entity the entity
   * @param ctx the ctx
   * @return persisted attached object, otherwise null.
   */
  public Object preparePersist(EntityMetadata entityMetadata, Object entity, XmlDumpRestoreContext ctx);
}
