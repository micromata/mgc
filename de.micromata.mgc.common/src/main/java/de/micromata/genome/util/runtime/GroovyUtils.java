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
