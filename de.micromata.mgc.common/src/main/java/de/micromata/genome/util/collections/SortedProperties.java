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

package de.micromata.genome.util.collections;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Die <code>store()</code>-Methode speichert die Einträge in alphabetischer Reihenfolge ab. Hat auch die moeglichkeit,
 * dass im VAlue := nicht escaped wird
 * 
 * @author noodles@micromata.de, roger@microamta.de
 */
public class SortedProperties extends Properties
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6788379042847551459L;

  /**
   * Do not escape character, which are really not needed to escape when reading.
   */
  private boolean storeUnescapedUnimportant = true;

  /**
   * Instantiates a new sorted properties.
   */
  public SortedProperties()
  {
    super();
  }

  /**
   * Instantiates a new sorted properties.
   *
   * @param defaults the defaults
   */
  public SortedProperties(Properties defaults)
  {
    super(defaults);

  }

  /**
   * Zwei Objekte anhand toString-Value vergleichen.
   *
   * @author noodles@micromata.de
   * @param <T> the generic type
   */
  private static class ToStringComparator<T extends Object> implements Comparator<T>
  {

    @Override
    public int compare(T l, T r)
    {
      String left = (l != null) ? "" + l : "";
      String right = (r != null) ? "" + r : "";

      final int result = left.compareToIgnoreCase(right);
      return result;
    }

  }

  @Override
  public synchronized Enumeration<Object> keys()
  {
    final Set<Object> keyset = super.keySet();
    final List<Object> list = new ArrayList<Object>(keyset);

    Collections.sort(list, new ToStringComparator<Object>());
    Enumeration<Object> result = Collections.enumeration(list);

    return result;
  }

  @Override
  public void store(OutputStream out, String comments) throws IOException
  {
    if (storeUnescapedUnimportant == false) {
      super.store(out, comments);
    } else {
      storeInternal(new BufferedWriter(new OutputStreamWriter(out, "8859_1")), comments, true);
    }
  }

  /**
   * Store internal.
   *
   * @param bw the bw
   * @param comments the comments
   * @param escUnicode the esc unicode
   * @throws IOException Signals that an I/O exception has occurred.
   */
  protected void storeInternal(BufferedWriter bw, String comments, boolean escUnicode) throws IOException
  {
    if (comments != null) {
      PropertiesReadWriter.writeComments(bw, comments);
    }
    bw.write("#" + new Date().toString());
    bw.newLine();
    synchronized (this) {
      for (Enumeration e = keys(); e.hasMoreElements();) {
        String key = (String) e.nextElement();
        String val = (String) get(key);
        key = PropertiesReadWriter.saveConvert(key, true, escUnicode, true);
        /*
         * No need to escape embedded and trailing spaces for value, hence pass false to flag.
         */
        val = PropertiesReadWriter.saveConvert(val, false, escUnicode, false);
        bw.write(key + "=" + val);
        bw.newLine();
      }
    }
    bw.flush();
  }

  public boolean isStoreUnescapedUnimportant()
  {
    return storeUnescapedUnimportant;
  }

  public void setStoreUnescapedUnimportant(boolean storeUnescapedUnimportant)
  {
    this.storeUnescapedUnimportant = storeUnescapedUnimportant;
  }

}
