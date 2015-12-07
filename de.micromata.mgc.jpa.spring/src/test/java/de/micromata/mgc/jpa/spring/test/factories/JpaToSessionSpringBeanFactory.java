package de.micromata.mgc.jpa.spring.test.factories;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaToSessionSpringBeanFactory implements FactoryBean<Session>
{
  @Autowired
  SessionFactory sessionFactory;

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

  @Override
  public Class<?> getObjectType()
  {
    return Session.class;
  }

  @Override
  public boolean isSingleton()
  {
    return true;
  }

}
