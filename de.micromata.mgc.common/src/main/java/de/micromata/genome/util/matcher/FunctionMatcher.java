package de.micromata.genome.util.matcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Matches against function.
 * 
 * The function has at least one argument, receiving the object matching against.
 * 
 * @param <T> the generic type
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class FunctionMatcher<T> extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7239337594796747169L;

  /**
   * The target.
   */
  private Object target;

  /**
   * The method.
   */
  private Method method;

  /**
   * The args.
   */
  private Object[] args;

  /**
   * Instantiates a new function matcher.
   * 
   * @param target the target
   * @param method the method
   * @param args the args
   */
  public FunctionMatcher(Object target, Method method, Object[] args)
  {
    super();
    this.target = target;
    this.method = method;
    this.args = args;
  }

  /**
   * {@inheritDoc}
   * 
   * @see de.micromata.genome.util.matcher.Matcher#match(java.lang.Object)
   */
  @Override
  public boolean match(T object)
  {
    Object[] nargs = new Object[args.length + 1];
    nargs[0] = object;
    System.arraycopy(args, 0, nargs, 1, args.length);
    try {
      return (Boolean) method.invoke(target, nargs);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException("Fail to invoke method: " + method + "; " + e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Fail to invoke method: " + method + "; " + e.getMessage(), e);
    } catch (InvocationTargetException e) {
      if (e.getTargetException() instanceof RuntimeException) {
        throw (RuntimeException) e.getTargetException();
      }
      throw new IllegalArgumentException("Fail to invoke method: " + method + "; " + e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(method.getName()).append("(");
    boolean first = true;
    for (Object arg : args) {
      if (first == false) {
        sb.append(", ");
      }
      sb.append(arg);
    }
    sb.append(")");
    return sb.toString();
  }

}
