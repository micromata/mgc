package de.micromata.genome.util.runtime.jndi;

import java.util.Hashtable;

import javax.naming.Context;

import org.apache.log4j.Logger;

public class SimpleNamingContextFactory implements javax.naming.spi.InitialContextFactory
{
  public static final Logger LOG = Logger.getLogger(SimpleJndiContext.class);

  public static void register()
  {
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
        SimpleNamingContextFactory.class.getName());
    LOG.info("LsEnv; Registered Jndi Default ContextFactory: " + SimpleNamingContextFactory.class.getName());
  }

  static JndiMockupNamingContextBuilder jndiMod = new JndiMockupNamingContextBuilder();

  @Override
  public Context getInitialContext(Hashtable<?, ?> environment)
  {
    LOG.info("LsEnv; Create new JndiContext");
    return new SimpleNamingContext("",
        jndiMod.getBoundObjects(),
        (Hashtable<String, Object>) environment);
  }

}
