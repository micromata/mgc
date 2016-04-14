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

package de.micromata.genome.logging;

/**
 * Default renderer. Als HTML wird der Wert des {@link LogAttribute} in &lt;pre&gt; Tag geliefert. Als Text wird der
 * escaped Wert genommen.
 * 
 * @author lado
 * 
 */
public class DefaultLogAttributeRenderer implements LogAttributeRenderer
{

  /**
   * The instance.
   */
  public static DefaultLogAttributeRenderer INSTANCE = new DefaultLogAttributeRenderer();

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogAttributeRenderer#renderHtml(de.micromata.genome.logging.LogAttribute,
   * de.micromata.genome.web.HttpContext)
   */
  @Override
  public String renderHtml(LogAttribute attr)
  {
    return "<pre><code>" + attr.getEscapedValue() + "</code></pre>";
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.micromata.genome.logging.LogAttributeRenderer#renderText(de.micromata.genome.logging.LogAttribute)
   */
  @Override
  public String renderText(LogAttribute attr)
  {
    return attr.getEscapedValue();
  }
}
