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
 * Print xml with indention.
 * 
 * @author roger@micromata.de
 * 
 */
public class PrittyXmlRenderer extends AppendableXmlRenderer
{

  /**
   * The indent.
   */
  protected int indent = 0;

  /**
   * The need indent.
   */
  protected boolean needIndent = false;

  /**
   * The indent string.
   */
  protected String indentString = "  ";

  /**
   * The last was tag end.
   */
  protected boolean lastWasTagEnd = false;

  /**
   * Instantiates a new pritty xml renderer.
   *
   * @param appender the appender
   */
  public PrittyXmlRenderer(Appendable appender)
  {
    super(appender);
  }

  /**
   * Indent.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  protected void indent() throws IOException
  {
    for (int i = 0; i < indent; ++i) {
      appender.append(indentString);
    }
  }

  @Override
  public XmlRenderer elementBeginOpen() throws IOException
  {
    //    if (true || lastWasTagEnd == true) {
    appender.append("\n");
    indent();
    //    }
    return super.elementBeginOpen();
  }

  @Override
  public XmlRenderer elementBeginEndClosed() throws IOException
  {
    lastWasTagEnd = true;
    return super.elementBeginEndClosed();
  }

  @Override
  public XmlRenderer elementBeginEndOpen() throws IOException
  {
    lastWasTagEnd = true;
    ++indent;
    return super.elementBeginEndOpen();
  }

  @Override
  public XmlRenderer elementEndClose() throws IOException
  {
    lastWasTagEnd = true;
    return super.elementEndClose();
  }

  @Override
  public XmlRenderer elementEndOpen() throws IOException
  {
    --indent;
    if (lastWasTagEnd == true) {
      appender.append("\n");
      indent();
    }
    return super.elementEndOpen();
  }

  @Override
  public XmlRenderer text(String code) throws IOException
  {
    lastWasTagEnd = false;
    return super.text(code);
  }

  @Override
  public XmlRenderer code(String code) throws IOException
  {
    lastWasTagEnd = false;
    return super.code(code);
  }

}
