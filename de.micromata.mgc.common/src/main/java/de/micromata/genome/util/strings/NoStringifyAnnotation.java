/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   27.02.2007
// Copyright Micromata 27.02.2007
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.strings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker interface, so the field should not be dumped in toString();
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoStringifyAnnotation {

}
