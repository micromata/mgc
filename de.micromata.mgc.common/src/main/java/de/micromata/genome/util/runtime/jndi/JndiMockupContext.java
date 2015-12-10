package de.micromata.genome.util.runtime.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.bean.PrivateBeanUtils;

/**
 * Mockup implementation for JNDI context.
 * 
 * Hibernate needs to support Names. So extends SimpleNamingContext for basic Name support.
 * 
 * @author roger
 * 
 */
public class JndiMockupContext extends SimpleNamingContext
{

  public JndiMockupContext()
  {
    super();
  }

  public JndiMockupContext(String root, Hashtable<String, Object> boundObjects, Hashtable<String, Object> env)
  {
    super(root, boundObjects, env);
  }

  public JndiMockupContext(String root)
  {
    super(root);
  }

  @Override
  public NameParser getNameParser(String name) throws NamingException
  {
    return new JndiMockupParser();
  }

  protected String toString(Name name)
  {
    return name.toString();
  }

  @Override
  public Object lookup(Name name) throws NamingException
  {
    return super.lookup(toString(name));
  }

  protected String getRoot()
  {
    return PrivateBeanUtils.readField(this, "root", String.class);
  }

  protected Hashtable<String, Object> getBoundObjects()
  {
    return PrivateBeanUtils.readField(this, "boundObjects", Hashtable.class);
  }

  @Override
  public Context createSubcontext(String name)
  {
    String subcontextName = getRoot() + name;
    if (!subcontextName.endsWith("/")) {
      subcontextName += "/";
    }
    Context subcontext = new JndiMockupContext(subcontextName, getBoundObjects(), getEnvironment());
    bind(name, subcontext);
    return subcontext;
  }

  @Override
  public Object lookup(String lookupName) throws NameNotFoundException
  {
    try {
      return super.lookup(lookupName);
    } catch (NameNotFoundException ex) {
      String prefix = "java:/comp/env/";
      if (StringUtils.startsWith(lookupName, prefix) == false) {
        return super.lookup(prefix + lookupName);
      }
      throw ex;
    }
  }

}
