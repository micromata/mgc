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
 * A XML Comment.
 * 
 * @author roger@micromata.de
 */
public class XmlComment extends XmlNode
{

  /**
   * The comment.
   */
  private final String comment;

  /**
   * Instantiates a new xml comment.
   *
   * @param comment the comment
   */
  public XmlComment(final String comment)
  {
    this.comment = comment;
  }

  @Override
  public void toXml(XmlRenderer sb) throws IOException
  {
    sb.code("<!-- ").text(comment).code(" -->");

  }

}
