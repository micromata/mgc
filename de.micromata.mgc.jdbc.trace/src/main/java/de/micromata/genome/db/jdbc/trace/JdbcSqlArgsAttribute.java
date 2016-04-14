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

package de.micromata.genome.db.jdbc.trace;

import java.util.Arrays;

import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.LogAttribute;

/**
 * The Class JdbcSqlArgsAttribute.
 *
 * @author roger
 */
public class JdbcSqlArgsAttribute extends LogAttribute
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -152709636218028663L;

  /**
   * The args.
   */
  private Object[] args;

  /**
   * Instantiates a new jdbc sql args attribute.
   *
   * @param args the args
   */
  public JdbcSqlArgsAttribute(Object[] args)
  {
    super(GenomeAttributeType.SqlArgs, "");
    this.args = args;
  }

  @Override
  public String getValue()
  {
    if (args == null) {
      return "";
    }
    String sargs = Arrays.toString(args);
    return sargs;
  }

}
