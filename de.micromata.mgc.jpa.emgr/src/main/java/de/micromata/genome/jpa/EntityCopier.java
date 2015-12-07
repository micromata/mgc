package de.micromata.genome.jpa;

/**
 * Copies an entity from orig to dest using given interface class as type template.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface EntityCopier
{

  /**
   * Copy to orig to dest using getter/setter.
   *
   * @param <T> the generic type
   * @param iface the iface
   * @param dest the dest
   * @param orig the orig
   */
  <T> void copyTo(Class<? extends T> iface, T dest, T orig);
}
