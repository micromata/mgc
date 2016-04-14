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

import org.hibernate.dialect.Oracle10gDialect;

/**
 * Configuration dialect which allows for <code>SELECT FOR UPDATE</code> operations by using pessimistic locking in
 * Oracle.
 * 
 * Define
 * 
 * <pre>
 * hibernate.dialect_resolvers = de.micromata.genome.jpa.hibernate.dialect.JpaDialectResolver
 * </pre>
 * 
 * or
 * 
 * <pre>
 * hibernate.dialect = de.micromata.genome.jpa.hibernate.dialect.Oracle10gDialectNoFollowOnLocking
 * </pre>
 * 
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Michael Lesniak (mlesniak@micromata.de)
 * 
 */
public class Oracle10gDialectNoFollowOnLocking extends Oracle10gDialect
{
  /**
   * Used to enable pesimistic locks (select for update) when using optimistic locks. {@inheritDoc}
   * 
   * @see org.hibernate.dialect.Oracle8iDialect#useFollowOnLocking()
   */
  @Override
  public boolean useFollowOnLocking()
  {
    return false;
  }

}
