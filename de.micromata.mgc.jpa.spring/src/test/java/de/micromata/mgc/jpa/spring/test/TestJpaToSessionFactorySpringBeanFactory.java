package de.micromata.mgc.jpa.spring.test;

import javax.persistence.EntityManagerFactory;

import de.micromata.mgc.jpa.spring.factories.JpaToSessionFactorySpringBeanFactory;

public class TestJpaToSessionFactorySpringBeanFactory extends JpaToSessionFactorySpringBeanFactory
{

  @Override
  protected EntityManagerFactory getEntityManagerFactory()
  {
    return SpringJpaEmgrFactory.get().getEntityManagerFactory();
  }

}
