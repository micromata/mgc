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

package de.micromata.genome.util.matcher;

/**
 * Base class for comparator matchers.
 * 
 * Match alwyas return true if one argument is null.
 *
 * @author roger@micromata.de
 * @param <T> the generic type
 */
public abstract class ComparatorMatcherBase<T extends Comparable<T>>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5533194814723147344L;

  /**
   * The other.
   */
  protected T other;

  /**
   * Instantiates a new comparator matcher base.
   */
  public ComparatorMatcherBase()
  {

  }

  /**
   * Instantiates a new comparator matcher base.
   *
   * @param other the other
   */
  public ComparatorMatcherBase(T other)
  {
    this.other = other;
  }

  public T getOther()
  {
    return other;
  }

  public void setOther(T other)
  {
    this.other = other;
  }

}
