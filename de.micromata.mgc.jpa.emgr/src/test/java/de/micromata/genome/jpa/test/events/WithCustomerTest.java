package de.micromata.genome.jpa.test.events;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.mgc.common.test.MgcTestCase;

public class WithCustomerTest extends MgcTestCase
{
  static JpaTestEntMgrWithCustomerFactory emfac = JpaTestEntMgrWithCustomerFactory.get();

  @Test
  public void testCreateCustomer()
  {
    Long pk = emfac.tx().go(emgr -> {
      MyStockItemDO no = new MyStockItemDO();
      emgr.insert(no);
      return no.getPk();
    });

    MyStockItemDO noreaded = emfac.tx().go(emgr -> emgr.selectByPkDetached(MyStockItemDO.class, pk));
    String customerId = noreaded.getCustomerId();
    Assert.assertTrue(customerId != null);
  }
}
