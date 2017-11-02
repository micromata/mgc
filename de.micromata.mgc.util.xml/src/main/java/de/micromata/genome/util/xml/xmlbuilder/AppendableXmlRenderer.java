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
import org.apache.commons.text.StringEscapeUtils;

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
    appender.append(StringEscapeUtils.escapeXml10(code));
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
