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

package de.micromata.genome.util.strings;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The Class ReducedMultiLineStyle.
 *
 * @author roger@micromata.de
 */
public class ReducedMultiLineStyle extends ToStringStyle
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 8350351381527673977L;

  /**
   * Instantiates a new reduced multi line style.
   */
  public ReducedMultiLineStyle()
  {
    super();
    this.setContentStart("[");
    this.setFieldSeparator(System.lineSeparator() + "  ");
    this.setFieldSeparatorAtStart(true);
    this.setContentEnd(System.lineSeparator() + "]");
    this.setUseShortClassName(true);
  }

  /**
   * The instance.
   */
  public static ReducedMultiLineStyle INSTANCE = new ReducedMultiLineStyle();

  /**
   * Read resolve.
   *
   * @return the object
   */
  private Object readResolve()
  {
    return ReducedMultiLineStyle.INSTANCE;
  }

  @Override
  protected void appendFieldSeparator(StringBuffer buffer)
  {
    buffer.append(System.lineSeparator());
    int level = ReducedReflectionToStringBuilder.getRegistryObject().level;
    for (int i = 0; i <= level; ++i) {
      buffer.append(" ");
    }
  }

  @Override
  protected void appendContentEnd(StringBuffer buffer)
  {
    int level = ReducedReflectionToStringBuilder.getRegistryObject().level;
    buffer.append(System.lineSeparator());
    for (int i = 0; i <= level; ++i) {
      buffer.append(" ");
    }
    buffer.append("]");
  }
}
