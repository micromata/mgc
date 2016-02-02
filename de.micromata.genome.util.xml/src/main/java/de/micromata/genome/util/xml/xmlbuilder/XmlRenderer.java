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
 * Interface to render XML.
 *
 * @author roger@micromata.de
 */
public interface XmlRenderer
{

  /**
   * Just pass through code.
   *
   * @param code the code
   * @return the xml renderer
   * @throws IOException Signals that an I/O exception has occurred.
   */
  XmlRenderer code(String code) throws IOException;

  /**
   * Text will be escaped.s
   *
   * @param code the code
   * @return the xml renderer
   * @throws IOException Signals that an I/O exception has occurred.
   */
  XmlRenderer text(String code) throws IOException;

  /**
   * for new Line.
   *
   * @return the xml renderer
   * @throws IOException Signals that an I/O exception has occurred.
   */
  XmlRenderer elementBeginOpen() throws IOException;

  /**
   * Element begin end open.
   *
   * @return the xml renderer
   * @throws IOException Signals that an I/O exception has occurred.
   */
  XmlRenderer elementBeginEndOpen() throws IOException;

  /**
   * Element begin end closed.
   *
   * @return the xml renderer
   * @throws IOException Signals that an I/O exception has occurred.
   */
  XmlRenderer elementBeginEndClosed() throws IOException;

  /**
   * Element end open.
   *
   * @return the xml renderer
   * @throws IOException Signals that an I/O exception has occurred.
   */
  XmlRenderer elementEndOpen() throws IOException;

  /**
   * Element end close.
   *
   * @return the xml renderer
   * @throws IOException Signals that an I/O exception has occurred.
   */
  XmlRenderer elementEndClose() throws IOException;
}
