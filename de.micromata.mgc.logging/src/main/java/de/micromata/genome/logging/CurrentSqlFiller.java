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

package de.micromata.genome.logging;

/**
 * Fills current executed sql statement, if set.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class CurrentSqlFiller implements AttributeTypeDefaultFiller
{
  @Override
  public String getValue(LogWriteEntry lwe, LoggingContext ctx)
  {
    if (ctx == null || lwe.getLevel().getLevel() < LogLevel.Warn.getLevel()) {
      return null;
    }
    LogSqlAttribute attr = new LogSqlAttribute(ctx.getCurrentSql(), ctx.getCurrentSqlArgs());
    return attr.getValue();
  }
}
