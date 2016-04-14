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

package de.micromata.genome.util.matcher.cls;

import de.micromata.genome.util.matcher.MatcherBase;

/**
 * Extends or implements given class.
 * 
 * @author roger@micromata.de
 * 
 */
public class ExtendsClassMatcher extends MatcherBase<Class<?>>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 2781760884364697384L;

  /**
   * The super class.
   */
  private Class<?> superClass;

  /**
   * Instantiates a new extends class matcher.
   *
   * @param superClass the super class
   */
  public ExtendsClassMatcher(Class<?> superClass)
  {
    this.superClass = superClass;
  }

  @Override
  public boolean match(Class<?> object)
  {
    return superClass.isAssignableFrom(object);
  }

  @Override
  public String toString()
  {
    return "<EXPR>.extendsClass(" + superClass.getCanonicalName() + ")";

  }

  public Class<?> getSuperClass()
  {
    return superClass;
  }

}
