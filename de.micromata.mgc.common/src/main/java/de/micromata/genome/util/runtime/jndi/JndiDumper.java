//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.util.runtime.jndi;

import javax.mail.Session;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * Utility to dump a JNDI context.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JndiDumper
{
  public static void dumpJndi()
  {
    StringBuilder sb = new StringBuilder();
    try {
      dumpJndi(sb, "");
    } catch (NamingException ex) {
      ex.printStackTrace(System.out);
    }
    System.out.println(sb.toString());
    ;
  }

  public static String getJndiDump()
  {
    StringBuilder sb = new StringBuilder();
    try {
      dumpJndi(sb, "");
      return sb.toString();
    } catch (NamingException ex) {
      sb.append("Error accessing naming: " + ex.getMessage());
      return sb.toString();
    }
  }

  public static void dumpJndi(StringBuilder sb, String indent) throws NamingException
  {
    InitialContext initialContext = new InitialContext();

    NamingEnumeration<Binding> bindings = initialContext.listBindings("");

    while (bindings.hasMore()) {
      Binding binding = bindings.next();
      dumpJndiBinding(initialContext, "", binding, sb, indent);
    }
    bindings = initialContext.listBindings("java:/comp");

    while (bindings.hasMore()) {
      Binding binding = bindings.next();
      dumpJndiBinding(initialContext, "java:/comp", binding, sb, indent);
    }

  }

  public static void dumpJndiBinding(InitialContext initialContext, String parentKey, Binding binding, StringBuilder sb,
      String indent)
      throws NamingException
  {
    try {
      Object obj = binding.getObject();
      ClassLoader bindClassLoader = obj.getClass().getClassLoader();
      sb.append(indent).append(parentKey + "/" + binding.getName()).append("(").append(System.identityHashCode(binding))
          .append(", cl: ").append(bindClassLoader)
          .append("): ")
          .append(binding.toString());
      if (obj instanceof Context) {

        sb.append("\n");
        Context ctx = (Context) obj;
        indent += "  ";
        NamingEnumeration<Binding> bindings = ctx.listBindings("");
        while (bindings.hasMore()) {
          Binding cbinding = bindings.next();
          dumpJndiBinding(initialContext, parentKey + "/" + binding.getName(), cbinding, sb, indent);
        }
      } else if (obj instanceof Reference) {
        Reference ref = (Reference) obj;
        //      binding.get
        //      ref.
        sb.append("\n");
      } else if (obj instanceof Session) {
        Session sess = (Session) obj;
        sb.append("\n");
      } else if (obj instanceof DataSource) {
        DataSource ds = (DataSource) obj;
        if (ds instanceof BasicDataSource) {
          BasicDataSource dbds = (BasicDataSource) ds;
          sb.append(" '" + dbds.getUrl() + "'");
        }
        sb.append("\n");
      } else if (obj != null) {
        Class<? extends Object> clazz = obj.getClass();

        sb.append(" unkown type: " + clazz);
        sb.append("\n");
      }
    } catch (NamingException ex) {
      sb.append("Error access binding: " + binding.getName() + ": " + ex.getMessage());
    }
  }

  public static void register()
  {
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
        "de.micromata.genome.util.runtime.jndi.SimpleNamingContextFactory");
    //    env.put(Context.INITIAL_CONTEXT_FACTORY,
    //        "com.sun.jndi.rmi.registry.RegistryContextFactory");
  }
}
