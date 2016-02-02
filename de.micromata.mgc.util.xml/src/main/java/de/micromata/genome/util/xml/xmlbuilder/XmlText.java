/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   13.06.2009
// Copyright Micromata 13.06.2009
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.util.xml.xmlbuilder;

import java.io.IOException;

/**
 * A XML text element.
 * 
 * @author roger@micromata.de
 * 
 */
public class XmlText extends XmlNode
{

  /**
   * The text.
   */
  private final String text;

  /**
   * Instantiates a new xml text.
   *
   * @param text the text
   */
  public XmlText(String text)
  {
    this.text = text;
  }

  @Override
  public void toXml(XmlRenderer sb) throws IOException
  {
    sb.text(text);
  }

}
