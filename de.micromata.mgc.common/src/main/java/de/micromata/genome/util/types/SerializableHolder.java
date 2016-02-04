package de.micromata.genome.util.types;

import java.io.Serializable;

/**
 * Like holder, but is serializable.
 *
 * @author roger
 * @param <T> the generic type
 */
public class SerializableHolder<T extends Serializable>extends Holder<T>implements Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -2098672266581652701L;

  /**
   * Instantiates a new serializable holder.
   */
  public SerializableHolder()
  {
    super();
  }

  /**
   * Instantiates a new serializable holder.
   *
   * @param t the t
   */
  public SerializableHolder(T t)
  {
    super(t);
  }

}
