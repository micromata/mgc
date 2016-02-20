package de.micromata.mgc.javafx;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SystemServiceTest
{
  @Test
  public void testDriver()
  {
    Assert.assertTrue(SystemService.get().classExists("de.micromata.mgc.javafx.SystemService"));
    Assert.assertFalse(SystemService.get().classExists("garbage.will.not.found.SystemService"));

  }

  @Test
  public void testListDrivers()
  {
    List<JdbcDriverDescription> driverlist = SystemService.get().getJdbcDrivers();
    driverlist.size();
  }
}
