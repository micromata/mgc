package de.micromata.genome.db.jpa.tabattr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.GenomeTestCase;
import de.micromata.genome.db.jpa.tabattr.testentities.TestMasterAttrDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrDO;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrEntityMgr;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrEntityMgrFactory;
import de.micromata.genome.db.jpa.tabattr.testentities.TestTabAttrJpaDAOImpl;
import de.micromata.genome.jpa.EmgrCallable;
import de.micromata.genome.util.strings.converter.StandardStringConverter;

public class FirstTest extends GenomeTestCase
{

  public static TestMasterAttrDO createMaster(int c)
  {
    TestMasterAttrDO a = new TestMasterAttrDO();
    a.setAbrn("123456789012" + (c % 100));
    a.setEkp("123456789" + (c % 10));
    int zufall1000 = new Random().nextInt() % 1000;
    int zufall100 = new Random().nextInt() % 100;
    a.setSenderName1("Ro" + zufall1000 + " Komr" + c);
    a.setSenderName2("asdfasdfasdf");
    a.setSenderStreet("Goethestr.");
    a.setSenderHouseNumber("73");
    a.setSenderCity("Kassel");
    a.setSenderCountry("DEU");
    a.setRecvName1("Micr" + zufall100);
    a.setRecvName2("asdfasdfasdf");
    a.setRecvStreet("Marie-Calm-Str.");
    a.setRecvHouseNumber("73");
    a.setRecvCity("Kassel");
    a.setRecvCountry("DEU");
    a.setProductModelVersion("P1.0.0_GlobalProduktModel");
    a.setProduct("V01PAK.V01SPERR");
    a.setSenderReference("Ref " + c);
    return a;
  }

  public static String genLongString()
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100; ++i) {
      sb.append(
          "asdf asdf asdfasdfasdfasdfasdfasdf asdf asdf asdf as df asdf asd f asdf as df asdf  asdfasdfasdfasdfasdfasdf");
    }
    return sb.toString();
  }

  public static TestMasterAttrDO createMaster(TestTabAttrJpaDAOImpl dao, int attrCount, int i)
  {
    TestMasterAttrDO m = createMaster(i);
    for (int j = 0; j < attrCount; ++j) {
      TestTabAttrDO entry = (TestTabAttrDO) m.createAttrEntity("Key No " + j,
          StandardStringConverter.get().getTypeChar(""),
          "A value to write");
      entry.setParent(m);
      if ((j % 10) == 0) {
        entry.setValue(genLongString());
      }
      m.putStringAttribute(entry.getPropertyName(), entry.getValue());
    }

    return m;
  }

  int shpCount = 1;

  int attrCount = 2;

  @Test
  public void testBeans()
  {
    final TestTabAttrJpaDAOImpl dao = new TestTabAttrJpaDAOImpl();
    final TestMasterAttrDO master = createMaster(47);
    String stringValue = "asdfasdfasdf";
    Integer intValue = 42;
    Date dateValue = new Date();
    String[] stringArrayValue = new String[] { "asdf", "fhf" };
    Long[] longArrayValue = new Long[] { 1324523423423423432L, -12121212L };
    Object nullValue = null;
    Byte byteValue = 16;
    Short shortValue = 1243;
    Long longValue = 1324524234223423232L;
    Character charValue = 'k';
    byte[] byteArrayValue = new byte[] { 12, 13, 14, 15, 16 };
    BigDecimal bigdecimalValue = new BigDecimal("12.67");
    master.putAttribute("stringValue", stringValue);
    master.putAttribute("intValue", intValue);
    master.putAttribute("nullValue", nullValue);
    master.putAttribute("dateValue", dateValue);
    master.putAttribute("stringArrayValue", stringArrayValue);
    master.putAttribute("longArrayValue", longArrayValue);
    master.putAttribute("byteValue", byteValue);
    master.putAttribute("shortValue", shortValue);
    master.putAttribute("longValue", longValue);
    master.putAttribute("charValue", charValue);
    master.putAttribute("byteArrayValue", byteArrayValue);
    master.putAttribute("bigdecimalValue", bigdecimalValue);

    final TestTabAttrEntityMgrFactory mgrfac = TestTabAttrEntityMgrFactory.get();
    //    mgrfac.setHasUpdateTriggerForVersion(true);
    TestMasterAttrDO tm = mgrfac.runInTrans(new EmgrCallable<TestMasterAttrDO, TestTabAttrEntityMgr>()
    {

      @Override
      public TestMasterAttrDO call(TestTabAttrEntityMgr mgr)
      {
        mgr.insert(master);
        mgr.detach(master);
        return master;
      }

    });

    TestMasterAttrDO selmaster = mgrfac.runInTrans(new EmgrCallable<TestMasterAttrDO, TestTabAttrEntityMgr>()
    {

      @Override
      public TestMasterAttrDO call(TestTabAttrEntityMgr mgr)
      {
        return mgr.selectByPkDetached(TestMasterAttrDO.class, master.getPk());
      }

    });
    Assert.assertEquals(stringValue, selmaster.getAttribute("stringValue"));
    Assert.assertEquals(intValue, selmaster.getAttribute("intValue"));
    Assert.assertEquals(dateValue, selmaster.getAttribute("dateValue"));
    Assert.assertArrayEquals(stringArrayValue, (String[]) selmaster.getAttribute("stringArrayValue"));
    Assert.assertArrayEquals(longArrayValue, (Long[]) selmaster.getAttribute("longArrayValue"));
    Assert.assertEquals(byteValue, selmaster.getAttribute("byteValue"));
    Assert.assertEquals(shortValue, selmaster.getAttribute("shortValue"));
    Assert.assertEquals(longValue, selmaster.getAttribute("longValue"));
    Assert.assertEquals(charValue, selmaster.getAttribute("charValue"));
    Assert.assertArrayEquals(byteArrayValue, (byte[]) selmaster.getAttribute("byteArrayValue"));
    Assert.assertEquals(bigdecimalValue, selmaster.getAttribute("bigdecimalValue"));
  }

  @Test
  public void testWithShipment()
  {
    try {

      final TestTabAttrJpaDAOImpl dao = new TestTabAttrJpaDAOImpl();

      final List<TestMasterAttrDO> ma = new ArrayList<TestMasterAttrDO>();
      for (int i = 0; i < shpCount; ++i) {
        TestMasterAttrDO m = createMaster(dao, attrCount, i);
        ma.add(m);
      }
      final TestTabAttrEntityMgrFactory mgrfac = TestTabAttrEntityMgrFactory.get();
      TestMasterAttrDO tm = mgrfac.runInTrans(new EmgrCallable<TestMasterAttrDO, TestTabAttrEntityMgr>()
      {

        @Override
        public TestMasterAttrDO call(TestTabAttrEntityMgr mgr)
        {
          TestMasterAttrDO m = createMaster(dao, attrCount, 0);
          mgr.insert(m);
          mgr.detach(m);
          return m;
        }

      });

      long start = System.currentTimeMillis();
      for (final TestMasterAttrDO m : ma) {
        mgrfac.runInTrans(new EmgrCallable<Void, TestTabAttrEntityMgr>()
        {

          @Override
          public Void call(TestTabAttrEntityMgr mgr)
          {
            mgr.insert(m);
            return null;
          }
        });

        // TestTabAttrDO entry = (TestTabAttrDO) dao.create("Another Key", "A value to write");

        m.putStringAttribute("Another Key", "A value to write");
        m.getAttributes().get("Key No " + 1).setValue("New Value");
        mgrfac.runInTrans(new EmgrCallable<Void, TestTabAttrEntityMgr>()
        {

          @Override
          public Void call(TestTabAttrEntityMgr mgr)
          {
            mgr.merge(m);
            mgr.detach(m);
            return null;
          }
        });
      }

      long end = System.currentTimeMillis();
      System.out.println("Wrote "
          + shpCount
          + " entries in "
          + (end - start)
          + " ms "
          + (((double) shpCount / (double) (end - start)) * 1000)
          + " rows per second");

    } catch (RuntimeException ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  @Test
  public void threadTest()
  {
    Runnable run = new Runnable()
    {

      @Override
      public void run()
      {
        // TODO Auto-generated method stub
        testWithShipment();
      }

    };
    int threadCount = 5;
    Thread[] ta = new Thread[threadCount];
    for (int i = 0; i < threadCount; ++i) {
      ta[i] = new Thread(run);
    }
    long start = System.currentTimeMillis();
    for (int i = 0; i < threadCount; ++i) {
      ta[i].start();
    }
    try {
      for (int i = 0; i < threadCount; ++i) {
        ta[i].join();
      }
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
    long end = System.currentTimeMillis();
    System.out.println("Wrote "
        + (shpCount * threadCount)
        + " entries in "
        + (end - start)
        + " ms "
        + (((double) (shpCount * threadCount) / (double) (end - start)) * 1000)
        + " rows per second");
  }

}
