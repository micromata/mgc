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

package de.micromata.genome.util.runtime.config;

import de.micromata.genome.util.validation.ValContext;

/**
 * Some hibernate settings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HibernateSchemaConfigModel extends AbstractLocalSettingsConfigModel
{
  @ALocalSettingsPath(key = "hibernate.hbm2ddl.auto", defaultValue = "update",
      comment = "Configurare Database schema update. Valid values are validate, update, create, create-drop")
  private String schemaUpdate;

  @ALocalSettingsPath(key = "hibernate.show_sql", defaultValue = "false", comment = "Show the executed sql on console")
  private String showSql;
  @ALocalSettingsPath(key = "hibernate.format_sql", defaultValue = "false",
      comment = "Format the shown execute sql in formatted form")
  private String formatSql;

  @Override
  public void validate(ValContext ctx)
  {

  }

  public String getSchemaUpdate()
  {
    return schemaUpdate;
  }

  public void setSchemaUpdate(String schemaUpdate)
  {
    this.schemaUpdate = schemaUpdate;
  }

  public String getShowSql()
  {
    return showSql;
  }

  public void setShowSql(String showSql)
  {
    this.showSql = showSql;
  }

  public boolean isShowSql()
  {
    return "true".equals(showSql);
  }

  public void setShowSql(boolean show)
  {
    showSql = Boolean.toString(show);
  }

  public String getFormatSql()
  {
    return formatSql;
  }

  public void setFormatSql(String formatSql)
  {
    this.formatSql = formatSql;
  }

  public boolean isFormatSql()
  {
    return "true".equals(formatSql);
  }

  public void setFormatSql(boolean format)
  {
    formatSql = Boolean.toString(format);

  }
}
