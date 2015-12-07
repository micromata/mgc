/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   25.01.2009
// Copyright Micromata 25.01.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.bean;

/**
 * The Interface NamedAttrSetter.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <BEAN> the generic type
 * @param <VAL> the generic type
 */
public interface NamedAttrSetter<BEAN, VAL>extends AttrSetter<BEAN, VAL>
{

  /**
   * the property name.
   *
   * @return the name
   */
  String getName();

}
