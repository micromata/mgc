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

package de.micromata.genome.logging.spi.ifiles;

import java.text.ParseException;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.types.DateUtils;

public enum StdSearchFields
{
  Time(23, lwe -> DateUtils.getStandardDateTimeFormat().format(new Date(lwe.getTimestamp())),

      (le, value) -> {
        try {
          le.setTimestamp(DateUtils.getStandardDateTimeFormat().parse(StringUtils.trim(value)).getTime());
        } catch (ParseException ex) {
          ex.printStackTrace(); // TODO better solution
        }
      }),

  Level(10, lwe -> lwe.getLevel().name(),
      (le, value) -> le.setLogLevel(LogLevel.fromString(StringUtils.trim(value), LogLevel.Note))),

  Category(30, lwe -> lwe.getCategory(),
      (le, value) -> le.setCategory(StringUtils.trim(value))),

  ShortMessage(32, lwe -> lwe.getMessage(),
      (le, value) -> le.setMessage(StringUtils.trim(value)))

  ;

  private final int size;
  private final Function<LogWriteEntry, String> fieldExtractor;
  private final BiConsumer<LogEntry, String> valueSetter;

  private StdSearchFields(int size, Function<LogWriteEntry, String> fieldExtractor,
      BiConsumer<LogEntry, String> valueSetter)
  {
    this.size = size;
    this.fieldExtractor = fieldExtractor;
    this.valueSetter = valueSetter;
  }

  public int getSize()
  {
    return size;
  }

  public Function<LogWriteEntry, String> getFieldExtractor()
  {
    return fieldExtractor;
  }

  public BiConsumer<LogEntry, String> getValueSetter()
  {
    return valueSetter;
  }

  public static StdSearchFields findFromString(String text)
  {
    for (StdSearchFields sf : values()) {
      if (sf.name().equals(text) == true) {
        return sf;
      }
    }
    return null;
  }
}