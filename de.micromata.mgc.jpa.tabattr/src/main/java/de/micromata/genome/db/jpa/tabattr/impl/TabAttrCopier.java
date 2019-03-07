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

package de.micromata.genome.db.jpa.tabattr.impl;

import java.util.Set;
import java.util.TreeSet;

import de.micromata.genome.db.jpa.tabattr.api.EntityWithAttributes;
import de.micromata.genome.jpa.EntityCopier;
import de.micromata.genome.jpa.EntityCopyStatus;
import de.micromata.genome.jpa.IEmgr;

/**
 * Copies EntityWithAttributes.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TabAttrCopier implements EntityCopier
{

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public <T> EntityCopyStatus copyTo(IEmgr<?> emgr, Class<? extends T> iface, T dest, T orig,
      String... ignoreCopyFields)
  {
    if ((orig instanceof EntityWithAttributes) == false || (dest instanceof EntityWithAttributes) == false) {
      return EntityCopyStatus.NONE;
    }
    // TODO RK implement ignoreCopyFields
    return copyAttributesTo((EntityWithAttributes) dest, (EntityWithAttributes) orig);

  }

  /**
   * Copy attributes to.
   *
   * @param dest the dest
   * @param orig the orig
   *
   * @return the {@link EntityCopyStatus}
   */
  public static EntityCopyStatus copyAttributesTo(EntityWithAttributes dest, EntityWithAttributes orig)
  {
    EntityCopyStatus ret = EntityCopyStatus.NONE;
    Set<String> destKeys = dest.getAttributeKeys();
    Set<String> origKeys = orig.getAttributeKeys();
    Set<String> insertOrgs = new TreeSet<String>(origKeys);
    insertOrgs.removeAll(destKeys);
    Set<String> updateOrgs = new TreeSet<String>(origKeys);
    updateOrgs.retainAll(destKeys);
    Set<String> deleteOrgs = new TreeSet<String>(destKeys);
    deleteOrgs.removeAll(origKeys);
    for (String insert : insertOrgs) {
      dest.putAttribute(insert, orig.getAttribute(insert));
      ret = EntityCopyStatus.MAJOR;
    }
    for (String update : updateOrgs) {
      dest.putAttribute(update, orig.getAttribute(update));
      ret = EntityCopyStatus.MAJOR;
    }
    for (String delete : deleteOrgs) {
      dest.removeAttribute(delete);
      ret = EntityCopyStatus.MAJOR;
    }
    return ret;
  }
}
