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

package de.micromata.genome.util.types;

import java.io.Serializable;

/**
 * Like holder, but is serializable.
 *
 * @author roger
 * @param <T> the generic type
 */
public class SerializableHolder<T extends Serializable>extends Holder<T>implements Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -2098672266581652701L;

  /**
   * Instantiates a new serializable holder.
   */
  public SerializableHolder()
  {
    super();
  }

  /**
   * Instantiates a new serializable holder.
   *
   * @param t the t
   */
  public SerializableHolder(T t)
  {
    super(t);
  }

}
