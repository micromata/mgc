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

package de.micromata.genome.db.jpa.xmldump.impl;

import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.hibernate.collection.spi.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/*
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.collection.internal.PersistentMap;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.internal.PersistentSortedMap;
import org.hibernate.collection.internal.PersistentSortedSet;
*/
import org.hibernate.proxy.HibernateProxy;

import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * Replaces Hibernate 3 specific collections with java.util implementations.
 *
 * <strong>NOTE</strong> This mapper takes care only of the writing to the XML (deflating) not the other way around
 * (inflating) because there is no need.
 *
 * @author Costin Leau
 *
 */

public class HibernateMapper extends MapperWrapper
{

  private Map<Class<?>, Class<?>> collectionMap = new HashMap<Class<?>, Class<?>>();

  public HibernateMapper(MapperWrapper arg0)
  {
    super(arg0);
    init();
  }

  public void init()
  {
    collectionMap.put(PersistentBag.class, ArrayList.class);
    collectionMap.put(PersistentList.class, ArrayList.class);
    collectionMap.put(PersistentMap.class, HashMap.class);
    collectionMap.put(PersistentSet.class, HashSet.class);
    collectionMap.put(PersistentSortedMap.class, TreeMap.class);
    collectionMap.put(PersistentSortedSet.class, TreeSet.class);
  }

  public HibernateMapper(Mapper arg0)
  {
    super(arg0);
    init();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class defaultImplementationOf(Class clazz)
  {
    // System.err.println("checking class:" + clazz);
    if (collectionMap.containsKey(clazz)) {
      // System.err.println("** substituting " + clazz + " with " + collectionMap.get(clazz));
      return collectionMap.get(clazz);
    }

    return super.defaultImplementationOf(clazz);
  }

  @SuppressWarnings("unchecked")
  @Override
  public String serializedClass(Class clazz)
  {
    // check whether we are hibernate proxy and substitute real name
    for (int i = 0; i < clazz.getInterfaces().length; i++) {
      if (HibernateProxy.class.equals(clazz.getInterfaces()[i])) {
        // System.err.println("resolving to class name:" + clazz.getSuperclass().getName());
        return clazz.getSuperclass().getName();
      }
    }
    if (collectionMap.containsKey(clazz)) {
      // System.err.println("** substituting " + clazz + " with " + collectionMap.get(clazz));
      return ((Class) collectionMap.get(clazz)).getName();
    }

    return super.serializedClass(clazz);
  }

}
