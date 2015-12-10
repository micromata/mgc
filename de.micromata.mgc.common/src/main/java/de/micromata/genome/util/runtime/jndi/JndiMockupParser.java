package de.micromata.genome.util.runtime.jndi;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

/**
 * Mockup implementation for JNDI
 * 
 * @author roger
 * 
 */
public class JndiMockupParser implements NameParser
{

  @Override
  public Name parse(String name) throws NamingException
  {
    return new CompositeName(name);
  }

}
