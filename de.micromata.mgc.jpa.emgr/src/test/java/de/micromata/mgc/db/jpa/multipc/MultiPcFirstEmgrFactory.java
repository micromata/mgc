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

package de.micromata.mgc.db.jpa.multipc;

import jakarta.persistence.EntityManager;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.EmgrTx;

public class MultiPcFirstEmgrFactory extends EmgrFactory<DefaultEmgr>
{
  static MultiPcFirstEmgrFactory INSTANCE;

  public static synchronized MultiPcFirstEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new MultiPcFirstEmgrFactory();
    return INSTANCE;
  }

  protected MultiPcFirstEmgrFactory()
  {
    super("de.micromata.genome.jpa.multipc.first");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager, EmgrTx<DefaultEmgr> emgrTx)
  {
    return new DefaultEmgr(entityManager, this, emgrTx);
  }

}
