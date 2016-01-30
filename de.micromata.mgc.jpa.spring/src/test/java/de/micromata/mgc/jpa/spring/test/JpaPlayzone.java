package de.micromata.mgc.jpa.spring.test;

import org.junit.Test;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import de.micromata.mgc.common.test.MgcTestCase;

public class JpaPlayzone extends MgcTestCase
{
  @Test
  public void testNested()
  {
    JpaTransactionManager jpm = new JpaTransactionManager(SpringJpaEmgrFactory.get().getEntityManagerFactory());

    TransactionTemplate tx = new TransactionTemplate(jpm);
    tx.setPropagationBehavior(TransactionTemplate.PROPAGATION_SUPPORTS);
    tx.execute((st) -> {

      return null;
    });
  }
}
