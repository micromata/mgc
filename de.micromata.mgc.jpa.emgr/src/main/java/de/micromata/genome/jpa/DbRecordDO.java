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

package de.micromata.genome.jpa;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Standard implementation of a record with a pk.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @author Johannes Unterstein (j.unterstein@micromata.de)
 */
@MappedSuperclass
public abstract class DbRecordDO<PK extends Serializable> implements DbRecord<PK>, Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7751062458383712172L;

  /**
   * The entity primary key.
   */
  public final static String PK_PROP = "pk";
  protected PK pk;

  @Override
  @Transient
  public abstract PK getPk();

  @Override
  public void setPk(final PK pk)
  {
    this.pk = pk;
  }

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
