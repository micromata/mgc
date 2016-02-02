/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   27.10.2009
// Copyright Micromata 27.10.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.xml.xmlbuilder;

import java.io.IOException;

/**
 * XML code fragment.
 * 
 * @author roger@micromata.de
 * 
 */
public class XmlCode extends XmlNode
{

  /**
   * The text.
   */
  private final String text;

  /**
   * Instantiates a new xml code.
   *
   * @param text the text
   */
  public XmlCode(String text)
  {
    this.text = text;
  }

  @Override
  public void toXml(XmlRenderer sb) throws IOException
  {
    sb.code(text);
  }
}
