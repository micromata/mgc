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

package de.micromata.genome.util.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.util.types.Converter;

/**
 * The Class AssertUtils.
 */
/*
 * Handling stacktraces for assertions. This may be used for debugging tools.
 * 
 * @author roger
 */
public class AssertUtils
{

  /**
   * The source paths.
   */
  public static String[] sourcePaths = new String[] { "src/test/java", "src/testutils/java" };

  /**
   * Gets the lines.
   *
   * @param c the c
   * @return the lines
   */
  private static List<String> getLines(final String c)
  {
    final List<String> s = Converter.parseStringTokens(c, "\n", true);
    final List<String> ret = new ArrayList<String>();

    for (int i = 0; i < s.size(); ++i) {
      final String t = s.get(i);
      if ("\n".equals(t) == true) {
        ret.add("");
      } else {
        ret.add(t);
        ++i;
      }
    }
    return ret;
  }

  /**
   * Gets the file lines.
   *
   * @param f the f
   * @return the file lines
   */
  private static List<String> getFileLines(final File f)
  {
    final StringWriter sout = new StringWriter();
    try {
      IOUtils.copy(new FileInputStream(f), sout, Charset.defaultCharset());
    } catch (final Exception ex) {
      return null;
    }
    final String fc = sout.getBuffer().toString();
    final List<String> s = getLines(fc);
    return s;
  }

  /**
   * The line map.
   */
  public static Map<String, List<String>> lineMap = new HashMap<String, List<String>>();

  /**
   * Retrieve lines.
   *
   * @param fname the fname
   * @return the list
   */
  private static List<String> retrieveLines(final String fname)
  {
    for (final String sp : sourcePaths) {
      final File d = new File(sp); // NOPMD stefan
      final File sf = new File(d, fname); // NOPMD stefan
      try {
        final String ss = sf.getCanonicalPath();

        if (sf.exists() == false) {
          continue;
        }
        return getFileLines(sf);
      } catch (final IOException ex) {
        continue;
      }
    }
    return null;
  }

  /**
   * Gets the lines of.
   *
   * @param fname the fname
   * @return the lines of
   */
  private static synchronized List<String> getLinesOf(final String fname)
  {
    if (lineMap.containsKey(fname) == true) {
      return lineMap.get(fname);
    }
    final List<String> lines = retrieveLines(fname);
    if (lines != null) {
      lineMap.put(fname, lines);
    }
    return lines;
  }

  /**
   * Gets the code line.
   *
   * @param clsName the cls name
   * @param fname the fname
   * @param lineNo the line no
   * @return the code line
   */
  public static String getCodeLine(final String clsName, final String fname, final int lineNo)
  {
    final int idx = clsName.lastIndexOf('.');
    String path = "";
    if (idx != -1) {
      path = clsName.substring(0, idx);
      path = path.replace('.', '/');
      path = path + "/" + fname; // NOPMD
    } else {
      path = fname;
    }
    final List<String> lines = getLinesOf(path);
    if (lines == null) {
      return "<no source found>";
    }
    if (lines.size() <= lineNo - 1) {
      return "<no source found>";
    }
    return lines.get(lineNo - 1);
  }

  /**
   * Gets the code line.
   *
   * @param stackUp the stack up
   * @return the code line
   */
  public static String getCodeLine(final int stackUp)
  {
    final StackTraceElement[] stel = Thread.currentThread().getStackTrace();
    final int stackOffset = 3;
    if (stel.length <= stackUp + stackOffset) {
      return "";
    }
    final StackTraceElement se = stel[stackUp + stackOffset];
    return getCodeLine(se);
  }

  /**
   * Gets the code line.
   *
   * @param se the se
   * @return the code line
   */
  public static String getCodeLine(final StackTraceElement se)
  {
    if (se == null) {
      return "<stack not found>";
    }
    final String fname = se.getFileName();
    final int cl = se.getLineNumber();
    final String clsName = se.getClassName();
    final String codeLine = getCodeLine(clsName, fname, cl);
    return StringUtils.trim(codeLine);
  }

  /**
   * Gets the stack above.
   *
   * @param above the above
   * @return the stack above
   */
  public static StackTraceElement getStackAbove(final Class<?> above)
  {
    final StackTraceElement[] stel = Thread.currentThread().getStackTrace();
    boolean foundFirst = false;
    final String clsName = above.getCanonicalName();
    for (int i = 0; i < stel.length; ++i) {
      final StackTraceElement s = stel[i];
      final String scn = s.getClassName();
      if (clsName.equals(scn) == false) {
        if (foundFirst == true) {
          return s;
        }
      } else {
        foundFirst = true;
      }
    }
    if (above.getSuperclass() != null) {
      return getStackAbove(above.getSuperclass());
    }
    return null;
  }

  /**
   * Gets the code line.
   *
   * @param above the above
   * @return the code line
   */
  public static String getCodeLine(final Class<?> above)
  {
    final StackTraceElement se = getStackAbove(above);
    if (se != null) {
      return getCodeLine(se);
    }
    return "<stack not found>";
  }
}
