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

package de.micromata.genome.jpa.events.impl;

import de.micromata.genome.jpa.StdRecord;
import de.micromata.genome.jpa.events.EmgrAfterCopyForUpdateEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;

/**
 * Manage update Counter after update
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrAfterCopyForUpdateEventEventHandler implements EmgrEventHandler<EmgrAfterCopyForUpdateEvent>
{

  @Override
  public void onEvent(EmgrAfterCopyForUpdateEvent event)
  {
    if ((event.getTarget() instanceof StdRecord) == false || (event.getSource() instanceof StdRecord) == false) {
      return;
    }
    ((StdRecord) event.getSource()).setUpdateCounter(((StdRecord) event.getTarget()).getUpdateCounter());
  }

}
