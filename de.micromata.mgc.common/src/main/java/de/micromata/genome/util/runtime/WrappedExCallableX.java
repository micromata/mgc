package de.micromata.genome.util.runtime;

/**
 * The Class WrappedExCallableX.
 *
 * @author roger
 * @param <V> the value type
 * @param <DECLEX> the generic type
 * @param <THROWEX> the generic type
 */
public abstract class WrappedExCallableX<V, DECLEX extends Throwable, THROWEX extends Throwable>
    extends AbtractCallableX<V, THROWEX>
{
  public Class<?> getDeclaredWrappedException()
  {

    Class<?> myClass = getClass();
    Class<?> fcls = GenericsUtils.getConcretTypeParameter(WrappedExCallableX.class, myClass, 1);
    if (fcls != null) {
      return fcls;
    }
    // fcls = GenericsUtils.getClassGenericTypeFromSuperClass(myClass, 1, null);
    //
    // if (fcls != null)
    // return fcls;
    return Exception.class;
  }

  /**
   * Call wrapped.
   *
   * @return the v
   * @throws DECLEX the declex
   */
  public V callWrapped() throws DECLEX
  {
    try {
      return call();
    } catch (Throwable ex) { // NOSONAR "Illegal Catch" framework
      Class<?> declaredEx = getDeclaredWrappedException();
      if (declaredEx.isAssignableFrom(ex.getClass()) == true) {
        throw (DECLEX) ex;
      }
      throw wrappException(ex);
    }
  }
}
