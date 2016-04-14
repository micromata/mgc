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

import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.util.runtime.ClassUtils;

/**
 * Creates a copy using EntityCopy annotation.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */

public class EmgrCopyUtils
{

  /**
   * Copy to.
   *
   * @param <T> the generic type
   * @param iface the iface
   * @param dest the dest
   * @param orig the orig
   */
  public static <T> EntityCopyStatus copyTo(IEmgr<?> emgr, Class<? extends T> iface, T dest, T orig,
      String... ignoreCopyFields)
  {
    if (dest instanceof CustomEntityCopier) {
      return ((CustomEntityCopier) dest).copyFrom(emgr, iface, orig, ignoreCopyFields);
    }
    List<EntityCopy> copiers = ClassUtils.findClassAnnotations(iface, EntityCopy.class);
    if (copiers.isEmpty() == false) {
      return copyTo(emgr, iface, dest, orig, copiers);
    }
    List<Class<? extends EntityCopier>> copiercl = emgr.getEmgrFactory().getRegisteredCopierForBean(iface);
    List<EntityCopier> cpl = new ArrayList<>();
    if (copiercl.isEmpty() == false) {
      for (Class<? extends EntityCopier> cpc : copiercl) {
        cpl.add(createCopier(cpc));
      }
    } else {
      cpl.add(new PropertyEntityCopier());
    }
    EntityCopyStatus ret = EntityCopyStatus.NONE;
    for (EntityCopier cpi : cpl) {
      ret = ret.combine(cpi.copyTo(emgr, iface, dest, orig));
    }
    return ret;
  }

  /**
   * Copy to.
   *
   * @param <T> the generic type
   * @param iface the iface
   * @param dest the dest
   * @param orig the orig
   * @param entcopiers the entcopiers
   */
  public static <T> EntityCopyStatus copyTo(IEmgr<?> emgr, Class<? extends T> iface, T dest, T orig,
      List<EntityCopy> entcopiers)
  {
    EntityCopyStatus ret = EntityCopyStatus.NONE;
    for (EntityCopy entc : entcopiers) {
      for (Class<? extends EntityCopier> cc : entc.copier()) {
        EntityCopier copier = createCopier(cc);
        ret = ret.combine(copier.copyTo(emgr, iface, dest, orig));
      }
    }
    return ret;
  }

  /**
   * Creates the copier.
   *
   * @param cc the cc
   * @return the entity copier
   */
  private static EntityCopier createCopier(Class<? extends EntityCopier> cc)
  {
    try {
      return cc.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new LoggedRuntimeException(LogLevel.Fatal, GenomeLogCategory.Jpa,
          "Cannot create instance of: " + cc.getName() + "; " + e.getMessage(), new LogExceptionAttribute(e));
    }
  }

}
