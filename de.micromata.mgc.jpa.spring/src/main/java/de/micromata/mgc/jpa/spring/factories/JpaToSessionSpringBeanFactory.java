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
