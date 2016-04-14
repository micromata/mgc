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

/**
 * The Class TraceSqlArguments.
 *
 * @author roger
 */
public class TraceSqlArguments
{

  /**
   * The sql.
   */
  private String sql;

  /**
   * The args.
   */
  private Object[] args = null;

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(sql).append("; ");
    if (args != null) {
      for (Object a : args) {
        sb.append(a).append(", ");
      }
    }
    return sb.toString();
  }

  /**
   * Resize.
   *
   * @param newSize the new size
   */
  protected void resize(int newSize)
  {
    Object[] nargs = new Object[newSize + (10 - newSize % 10)];
    if (args != null) {
      System.arraycopy(args, 0, nargs, 0, args.length);
    }
    args = nargs;
  }

  /**
   * Put.
   *
   * @param index the index
   * @param arg the arg
   */
  public void put(int index, Object arg)
  {
    if (args == null || args.length <= index) {
      resize(index);
    }
    args[index] = arg;

  }

  /**
   * Clear args.
   */
  public void clearArgs()
  {
    args = null;

  }

  public String getSql()
  {
    return sql;
  }

  public void setSql(String sql)
  {
    this.sql = sql;
  }

  public Object[] getArgs()
  {
    return args;
  }

  public void setArgs(Object[] args)
  {
    this.args = args;
  }
}
