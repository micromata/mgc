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

import java.util.ArrayList;
import java.util.List;

/**
 * XML Shortcuts.
 * 
 * Status noch alpha und nicht komplett.
 *
 * @author roger@micromata.de
 * @todo !DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
 * @todo CDATA
 */
public class Xml
{

  /**
   * Xml.
   *
   * @param elements the elements
   * @return the xml document
   */
  public static XmlDocument xml(XmlNode... elements)
  {
    return new XmlDocument(elements);
  }

  /**
   * Xml.
   *
   * @param attrs the attrs
   * @param elements the elements
   * @return the xml document
   */
  public static XmlDocument xml(String[][] attrs, XmlNode... elements)
  {
    return new XmlDocument(attrs, elements);
  }

  /**
   * Nodes.
   *
   * @param childs the childs
   * @return the xml node[]
   */
  public static XmlNode[] nodes(XmlNode... childs)
  {
    return childs;
  }

  /**
   * Comment.
   *
   * @param comment the comment
   * @return the xml node
   */
  public static XmlNode comment(String comment)
  {
    return new XmlComment(comment);
  }

  /**
   * Code.
   *
   * @param code the code
   * @return the xml node
   */
  public static XmlNode code(String code)
  {
    return new XmlCode(code);
  }

  /**
   * just a () wrapper.
   *
   * @param attrs the attrs
   * @return the string[][]
   */
  public static String[][] attrs(String[][] attrs)
  {
    return attrs;
  }

  /**
   * Attrs.
   *
   * @param attrs the attrs
   * @return the string[][]
   */
  public static String[][] attrs(String... attrs)
  {
    if (attrs.length == 0) {
      return XmlElement.EMPTY_STRINGARRAYARRAY;
    }
    String[][] ret = new String[attrs.length / 2][2];
    for (int i = 0; i < attrs.length;) {
      ret[i / 2][0] = attrs[i++];
      ret[i / 2][1] = attrs[i++];
    }
    return ret;
  }

  /**
   * Attrs.
   *
   * @return the string[][]
   */
  public static String[][] attrs()
  {
    return new String[][] {};
  }

  /**
   * Element.
   *
   * @param elemName the elem name
   * @param attrs the attrs
   * @return the xml element
   */
  public static XmlElement element(String elemName, String[][] attrs)
  {
    return new XmlElement(elemName, attrs);
  }

  /**
   * Element.
   *
   * @param elemName the elem name
   * @param childs the childs
   * @return the xml element
   */
  public static XmlElement element(String elemName, XmlNode... childs)
  {
    return new XmlElement(elemName, childs);
  }

  /**
   * Element.
   *
   * @param elemName the elem name
   * @param attrs the attrs
   * @return the xml element
   */
  public static XmlElement element(String elemName, String... attrs)
  {
    return new XmlElement(elemName, attrs);
  }

  /**
   * Element.
   *
   * @param elemName the elem name
   * @param attrs the attrs
   * @param childs the childs
   * @return the xml element
   */
  public static XmlElement element(String elemName, String[][] attrs, XmlNode... childs)
  {
    return new XmlElement(elemName, attrs, childs);
  }

  /**
   * Text.
   *
   * @param text the text
   * @return the xml text
   */
  public static XmlText text(String text)
  {

    if (text == null) {
      text = "";
    }
    return new XmlText(text);
  }

  /**
   * As list.
   *
   * @param <T> the generic type
   * @param values the values
   * @return the list
   */
  public static <T> List<T> asList(T... values)
  {
    ArrayList<T> ret = new ArrayList<T>(values.length);
    for (T v : values) {
      ret.add(v);
    }
    return ret;
  }

  /**
   * List as attrs.
   *
   * @param l the l
   * @return the string[][]
   */
  public static String[][] listAsAttrs(List<String> l)
  {
    return attrs(l.toArray(new String[] {}));
  }

  /**
   * Adds the.
   *
   * @param <T> the generic type
   * @param list the list
   * @param ts the ts
   * @return the list
   */
  public static <T> List<T> add(List<T> list, T... ts)
  {
    for (T t : ts) {
      list.add(t);
    }
    return list;
  }
}
