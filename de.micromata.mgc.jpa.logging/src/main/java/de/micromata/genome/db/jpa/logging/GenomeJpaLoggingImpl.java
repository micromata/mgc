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

package de.micromata.genome.db.jpa.logging;

import de.micromata.genome.db.jpa.logging.entities.GenomeLogMasterDO;
import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;

/**
 * Standard Logging table.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class GenomeJpaLoggingImpl extends BaseJpaLoggingImpl<GenomeLogMasterDO>
{
  public GenomeJpaLoggingImpl()
  {
    initProps();
  }

  @Override
  protected Class<GenomeLogMasterDO> getMasterClass()
  {
    return GenomeLogMasterDO.class;
  }

  @Override
  protected GenomeLogMasterDO createNewMaster()
  {
    return new GenomeLogMasterDO();
  }

  @Override
  protected EmgrFactory<DefaultEmgr> getEmgrFactory()
  {
    return JpaLoggingEntMgrFactory.get();
  }

}
