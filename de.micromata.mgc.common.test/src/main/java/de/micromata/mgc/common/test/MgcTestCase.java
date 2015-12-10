package de.micromata.mgc.common.test;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.junit.BeforeClass;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.LocalSettingsEnv;
import de.micromata.genome.util.runtime.jndi.JndiMockupNamingContextBuilder;

public class MgcTestCase
{
  protected static LocalSettingsEnv localSettingsEnv;

  @BeforeClass
  public static void initLs()
  {
    LocalSettings ls = LocalSettings.get();
    prepareJndi();
  }

  protected static void prepareJndi()
  {
    JndiMockupNamingContextBuilder contextBuilder = new JndiMockupNamingContextBuilder();
    Hashtable<String, Object> env = new Hashtable<String, Object>();
    InitialContextFactory initialContextFactory = contextBuilder.createInitialContextFactory(env);
    try {
      Context initialContext = initialContextFactory.getInitialContext(env);
      localSettingsEnv = new LocalSettingsEnv(initialContext);
      contextBuilder.activate();

    } catch (NamingException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static void sleep(long milliseconds)
  {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
