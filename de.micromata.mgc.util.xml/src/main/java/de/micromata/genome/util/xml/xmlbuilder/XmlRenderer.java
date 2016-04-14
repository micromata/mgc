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
