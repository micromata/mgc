package de.micromata.genome.util.runtime.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

import de.micromata.genome.util.bean.PrivateBeanUtils;

public class JndiMockupNamingContextBuilder extends SimpleNamingContextBuilder
{

  public JndiMockupNamingContextBuilder()
  {
    super();
  }

  @Override
  public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment)
  {
    // if (environment != null && environment.get(Context.INITIAL_CONTEXT_FACTORY) != null) {
    // return super.createInitialContextFactory(environment);
    // }
    // Default case...
    return new InitialContextFactory()
    {
      @Override
      @SuppressWarnings("unchecked")
      public Context getInitialContext(Hashtable<?, ?> environment)
      {
        return new JndiMockupContext("",
            PrivateBeanUtils.readField(JndiMockupNamingContextBuilder.this, "boundObjects", Hashtable.class),
            (Hashtable<String, Object>) environment);
      }
    };
  }
}
