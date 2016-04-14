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
