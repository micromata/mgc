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

package de.micromata.genome.jpa.metainf;

import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

import org.junit.Test;

import de.micromata.genome.jpa.test.JpaTestEntMgrFactory;
import de.micromata.mgc.common.test.MgcTestCase;

public class CoreJpaMetadataTest extends MgcTestCase
{

  @Test
  public void testMetaData()
  {
    EntityManagerFactory fac = JpaTestEntMgrFactory.get().getEntityManagerFactory();
    Metamodel metamodel = fac.getMetamodel();
    Set<EntityType<?>> ents = metamodel.getEntities();
    for (EntityType<?> ent : ents) {
      System.out.println(dumpEntityType(ent));
    }
    Set<EmbeddableType<?>> embedds = metamodel.getEmbeddables();
    Set<ManagedType<?>> manged = metamodel.getManagedTypes();
    manged.size();
  }

  public String dumpEntityType(EntityType<?> ent)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Entity: ").append(ent.getName()).append("\n");
    Set<?> attrs = ent.getAttributes();
    for (Object oatt : attrs) {
      Attribute<?, ?> att = (Attribute<?, ?>) oatt;
      sb.append(" attr: ").append(att.getName()).append("\n");
      if (att instanceof SingularAttribute) {
        SingularAttribute sa = (SingularAttribute) att;

      }
    }
    return sb.toString();
  }
}
