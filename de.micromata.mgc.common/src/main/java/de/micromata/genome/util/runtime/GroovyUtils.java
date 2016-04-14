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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;

import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

/**
 * Wrapper for Groovy. Encapsulate difference between 1.x and 2.x groovy
 * 
 * @author roger
 * @since 1.2.1
 * 
 */
public class GroovyUtils
{

  /**
   * Convert to string.
   *
   * @param is the is
   * @return the string
   */
  public static String convertToString(InputStream is)
  {
    try {
      return IOUtils.toString(is, CharEncoding.UTF_8);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Evaluate.
   *
   * @param sh the sh
   * @param is the is
   * @param name the name
   * @return the object
   */
  public static Object evaluate(GroovyShell sh, InputStream is, String name)
  {
    String s = convertToString(is);
    GroovyCodeSource codeSource = new GroovyCodeSource(s, name, "");
    return sh.evaluate(codeSource);
  }

  /**
   * Evaluate.
   *
   * @param sh the sh
   * @param code the code
   * @param name the name
   * @return the object
   */
  public static Object evaluate(GroovyShell sh, String code, String name)
  {
    GroovyCodeSource codeSource = new GroovyCodeSource(code, name, "");
    return sh.evaluate(codeSource);
  }

  /**
   * Parses the.
   *
   * @param shell the shell
   * @param is the is
   * @param name the name
   * @return the script
   */
  public static Script parse(GroovyShell shell, InputStream is, String name)
  {
    String s = convertToString(is);
    return shell.parse(s, name);
  }
}
