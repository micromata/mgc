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

import org.apache.commons.lang.StringEscapeUtils;

/**
 * The Class AppendableXmlRenderer.
 *
 * @author roger@micromata.de
 */
public class AppendableXmlRenderer implements XmlRenderer
{

  /**
   * The appender.
   */
  protected final Appendable appender;

  /**
   * Instantiates a new appendable xml renderer.
   *
   * @param appender the appender
   */
  public AppendableXmlRenderer(final Appendable appender)
  {
    this.appender = appender;
  }

  @Override
  public XmlRenderer code(String code) throws IOException
  {
    appender.append(code);
    return this;
  }

  /**
   * Nl.
   *
   * @return the xml renderer
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public XmlRenderer nl() throws IOException
  {
    return this;
  }

  @Override
  public XmlRenderer text(String code) throws IOException
  {
    appender.append(StringEscapeUtils.escapeXml(code));
    return this;
  }

  @Override
  public XmlRenderer elementBeginOpen() throws IOException
  {
    appender.append("<");
    return this;
  }

  @Override
  public XmlRenderer elementBeginEndClosed() throws IOException
  {
    appender.append("/>");
    return this;
  }

  @Override
  public XmlRenderer elementBeginEndOpen() throws IOException
  {
    appender.append(">");
    return this;
  }

  @Override
  public XmlRenderer elementEndClose() throws IOException
  {
    appender.append(">");
    return this;
  }

  @Override
  public XmlRenderer elementEndOpen() throws IOException
  {
    appender.append("</");
    return this;
  }
}
