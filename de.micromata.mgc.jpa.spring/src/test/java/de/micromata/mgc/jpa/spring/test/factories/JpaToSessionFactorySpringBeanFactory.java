package de.micromata.mgc.jpa.spring.test.factories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;

import de.micromata.mgc.jpa.spring.test.SpringJpaEmgrFactory;

public class JpaToSessionFactorySpringBeanFactory implements FactoryBean<SessionFactory>
{

  @Override
  public SessionFactory getObject() throws Exception
  {
    SessionFactory sf = SpringJpaEmgrFactory.get().getEntityManagerFactory().unwrap(SessionFactory.class);
    return sf;
  }

  @Override
  public Class<?> getObjectType()
  {
    return SessionFactory.class;
  }

  @Override
  public boolean isSingleton()
  {
    return true;
  }

}
