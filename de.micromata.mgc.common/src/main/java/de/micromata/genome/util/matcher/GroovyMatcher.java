package de.micromata.genome.util.matcher;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.runtime.InvokerHelper;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

/**
 * Matches agains a groovy expression.
 *
 * @author roger
 * @param <T> the generic type
 */
public class GroovyMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 6341664478451114710L;

  /**
   * The source.
   */
  private String source;

  /**
   * The script class.
   */
  private Class<?> scriptClass;

  /**
   * Instantiates a new groovy matcher.
   */
  public GroovyMatcher()
  {

  }

  /**
   * Instantiates a new groovy matcher.
   *
   * @param source the source
   */
  public GroovyMatcher(String source)
  {
    GroovyShell gs = new GroovyShell();
    this.source = source;
    Script script = gs.parse(source);
    this.scriptClass = script.getClass();
  }

  /**
   * Instantiates a new groovy matcher.
   *
   * @param source the source
   * @param scriptClass the script class
   */
  public GroovyMatcher(String source, Class<?> scriptClass)
  {
    this.source = source;
    this.scriptClass = scriptClass;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public boolean match(T object)
  {
    Binding binding;
    if (object instanceof Binding) {
      binding = (Binding) object;
    } else if (object instanceof Map) {
      binding = new Binding((Map) object);
    } else {
      Map<String, Object> context = new HashMap<String, Object>();
      context.put("arg", object);
      binding = new Binding(context);
    }
    Script script = InvokerHelper.createScript(scriptClass, binding);

    Object ret = script.run();
    return Boolean.TRUE.equals(ret);
  }

  public String getSource()
  {
    return source;
  }

  public void setSource(String source)
  {
    this.source = source;
  }

  public Class<?> getScriptClass()
  {
    return scriptClass;
  }

  public void setScriptClass(Class<?> scriptClass)
  {
    this.scriptClass = scriptClass;
  }
}
