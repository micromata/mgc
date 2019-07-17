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

package de.micromata.genome.jpa.hibernate.dialect;

import org.hibernate.dialect.Database;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;

/**
 * resolves dialect.
 *
 * Currently only return Oracle10gDialectNoFollowOnLocking in case of Oracle10gDialect
 *
 * Define
 *
 * <pre>
 * hibernate.dialect_resolvers = de.micromata.genome.jpa.hibernate.dialect.GenomeDialectResolver
 * </pre>
 *
 * In your local-settings.properties.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class GenomeDialectResolver implements DialectResolver
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7095360183898128038L;

  @Override
  public Dialect resolveDialect(DialectResolutionInfo info)
  {
    Dialect dialect = null;
    for ( Database database : Database.values() ) {
      dialect = database.resolveDialect( info );
      if ( dialect != null ) {
        break;
      }
    }
    if (dialect.getClass() == Oracle10gDialect.class) {
      return new Oracle10gDialectNoFollowOnLocking();
    }
    return dialect;
  }
}
