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

package de.micromata.genome.db.jpa.normsearch.eventlistener;

import de.micromata.genome.db.jpa.normsearch.NormalizedSearchServiceManager;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.events.EmgrAfterDeletedEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;

/**
 * Updates history after Delete.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NormSearchAfterDeleteListener implements EmgrEventHandler<EmgrAfterDeletedEvent>
{

  @Override
  public void onEvent(EmgrAfterDeletedEvent event)
  {

    if ((event.getEntity() instanceof DbRecord) == false) {
      return;
    }
    NormalizedSearchServiceManager.get().getNormalizedSearchService().onDelete(event.getEmgr(),
        (DbRecord<?>) event.getEntity());

  }

}
