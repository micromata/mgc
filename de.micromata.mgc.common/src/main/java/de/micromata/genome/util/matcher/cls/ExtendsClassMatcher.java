/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   04.07.2009
// Copyright Micromata 04.07.2009
//
/////////////////////////////////////////////////////////////////////////////
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
