package de.micromata.genome.util.types;

import java.io.Serializable;

/**
 * Like Holder, but it seriable and holds transient object.
 * 
 * If the TransientHolder will be serialized and deserialized holded object will be null.
 * 
 * @param <T> the generic type
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TransientHolder<T> implements Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -7684039078761152292L;

  /**
   * The holded.
   */
  private transient T holded;

  /**
   * Instantiates a new transient holder.
   */
  public TransientHolder()
  {
  }

  /**
   * Instantiates a new transient holder.
   * 
   * @param t the t
   */
  public TransientHolder(T t)
  {
    holded = t;
  }

  /**
   * Gets the.
   * 
   * @return the t
   */
  public T get()
  {
    return holded;
  }

  /**
   * Sets the.
   * 
   * @param t the t
   */
  public void set(T t)
  {
    holded = t;
  }

  public T getHolded()
  {
    return holded;
  }

  public void setHolded(T t)
  {
    holded = t;
  }
}
