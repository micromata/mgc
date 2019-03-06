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

import java.util.Collections;
import java.util.List;

import de.micromata.genome.db.jpa.history.api.HistProp;
import de.micromata.genome.db.jpa.history.api.HistoryPropertyConverter;
import de.micromata.genome.jpa.IEmgr;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.genome.util.strings.converter.StandardStringConverter;
import de.micromata.genome.util.strings.converter.StringConverter;

/**
 * The Class SimplePropertyConverter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class SimplePropertyConverter implements HistoryPropertyConverter
{

  /**
   * The string converter.
   */
  private static StringConverter stringConverter = StandardStringConverter.get();

  @Override
  public List<HistProp> convert(IEmgr<?> emgr, HistoryMetaInfo historyMetaInfo, Object entity, ColumnMetadata pd)
  {
    Object value = pd.getGetter().get(entity);
    return Collections.singletonList(convertInternal(value, pd));
  }

  /**
   * Convert.
   *
   * @param value the value
   * @param pd the coulmn metadata
   * @return the string
   */
  protected HistProp convertInternal(Object value, ColumnMetadata pd)
  {
    HistProp ret = new HistProp();
    ret.setName("");
    ret.setType(pd.getJavaType().getName());

    ret.setValue(convertToString(value, pd));
    return ret;
  }

  protected String convertToString(Object value, ColumnMetadata pd)
  {
    if (value instanceof Enum<?>) {
      return ((Enum<?>) value).name();
    }
    return stringConverter.asString(value);

  }
}
