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

package de.micromata.genome.db.jpa.history.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.db.jpa.tabattr.api.TimeableAttrRow;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.jpa.metainf.EntityMetadata;
import de.micromata.genome.util.types.Converter;

/**
 * The Class TimependingHistoryPropertyProvider.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TimependingHistoryPropertyConverter implements HistoryPropertyConverter
{

  @Override
  public List<HistProp> convert(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, ColumnMetadata pd)
  {
    List<? extends TimeableAttrRow> tlist = (List) pd.getGetter().get(entity);
    TabAttrHistoryPropertyConverter attrConverter = new TabAttrHistoryPropertyConverter();
    List<HistProp> ret = new ArrayList<>();
    for (TimeableAttrRow row : tlist) {
      String key = Converter.isoTimestampFormat.get().format(row.getStartTime());

      HistProp hp = new HistProp();
      hp.setName(key + ".startTime");
      hp.setType(Date.class.getName());
      hp.setValue(key);
      ret.add(hp);

      EntityMetadata rowmd = emgr.getEmgrFactory().getMetadataRepository().getEntityMetadata(row.getClass());
      ColumnMetadata mattributes = rowmd.getColumn("attributes");
      List<HistProp> attrs = attrConverter.convert(emgr, historyMetaInfo, row, mattributes);
      for (HistProp chp : attrs) {
        chp.setName(key + "." + chp.getName());
        ret.add(chp);
      }

    }
    return ret;
  }

}
