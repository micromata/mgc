package de.micromata.mgc.jpa.spring.factories;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * A factory for creating JpaToSessionFactorySpringBean objects.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public abstract class JpaToSessionFactorySpringBeanFactory implements FactoryBean<SessionFactory>
{

  protected abstract EntityManagerFactory getEntityManagerFactory();

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public SessionFactory getObject() throws Exception
  {
    SessionFactory sf = getEntityManagerFactory().unwrap(SessionFactory.class);
    return sf;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Class<?> getObjectType()
  {
    return SessionFactory.class;
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
