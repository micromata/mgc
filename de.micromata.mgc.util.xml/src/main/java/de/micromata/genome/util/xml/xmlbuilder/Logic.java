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

package de.micromata.genome.util.xml.xmlbuilder;

/**
 * function logic.
 *
 * @author roger
 */
public class Logic
{

  /**
   * If.
   *
   * @param <T> the generic type
   * @param condition the condition
   * @param node the node
   * @return null if condiation is false
   */
  public static <T> T If(boolean condition, T node)
  {
    return IfElse(condition, node, null);
  }

  /**
   * If else.
   *
   * @param <T> the generic type
   * @param condition the condition
   * @param node the node
   * @param elseNode the else node
   * @return the t
   */
  public static <T> T IfElse(boolean condition, T node, T elseNode)
  {
    return condition ? node : elseNode;
  }
}
