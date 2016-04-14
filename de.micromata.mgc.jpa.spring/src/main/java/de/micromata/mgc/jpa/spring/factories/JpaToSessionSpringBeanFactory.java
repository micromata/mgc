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

package de.micromata.mgc.jpa.spring.factories;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A factory for creating JpaToSessionSpringBean objects.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class JpaToSessionSpringBeanFactory implements FactoryBean<Session>
{

  /**
   * The session factory.
   */
  @Autowired
  SessionFactory sessionFactory;

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Session getObject() throws Exception
  {
    try {
      Session session = sessionFactory.getCurrentSession();
      if (session != null) {
        return session;
      }
    } catch (HibernateException ex) {
      // ignore
    }
    return sessionFactory.openSession();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Class<?> getObjectType()
  {
    return Session.class;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isSingleton()
  {
    return true;
  }

}
