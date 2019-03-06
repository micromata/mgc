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

package de.micromata.genome.db.jpa.history.api;

import de.micromata.genome.db.jpa.history.impl.HistoryMetaInfo;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import java.util.List;

/**
 * The Interface HistoryPropertyConverter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface HistoryPropertyConverter {

  /**
   * Converts a property to a string for storing an history entry.
   *
   * @param entity the entity
   * @param pd the pd
   * @param emgr the emgr
   * @param historyMetaInfo the historyMetaInfo
   * @return the string
   */
  List<HistProp> convert(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, ColumnMetadata pd);
}
