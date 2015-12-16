package de.micromata.genome.jpa;

/**
 * 
 * An entity, which want to implement copy by itself.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public interface CustomEntityCopier<T>
{
  EntityCopyStatus copyFrom(IEmgr<?> emgr, Class<? extends T> iface, T orig, String... ignoreCopyFields);
}
