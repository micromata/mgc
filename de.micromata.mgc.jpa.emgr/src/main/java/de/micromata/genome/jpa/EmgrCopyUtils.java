package de.micromata.genome.jpa;

import java.util.List;

import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggedRuntimeException;
import de.micromata.genome.util.runtime.ClassUtils;

/**
 * Creates a copy using EntityCopy annotation.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@EntityCopy(copier = { PropertyEntityCopier.class })
public class EmgrCopyUtils
{

  /**
   * Copy to.
   *
   * @param <T> the generic type
   * @param iface the iface
   * @param dest the dest
   * @param orig the orig
   */
  public static <T> void copyTo(Class<? extends T> iface, T dest, T orig)
  {
    List<EntityCopy> copiers = ClassUtils.findClassAnnotations(iface, EntityCopy.class);
    if (copiers.isEmpty()) {
      // thats a trick to retrieve standard annotation
      copiers = ClassUtils.findClassAnnotations(EmgrCopyUtils.class, EntityCopy.class);
    }
    copyTo(iface, dest, orig, copiers);
  }

  /**
   * Copy to.
   *
   * @param <T> the generic type
   * @param iface the iface
   * @param dest the dest
   * @param orig the orig
   * @param entcopiers the entcopiers
   */
  public static <T> void copyTo(Class<? extends T> iface, T dest, T orig, List<EntityCopy> entcopiers)
  {
    for (EntityCopy entc : entcopiers) {
      for (Class<? extends EntityCopier> cc : entc.copier()) {
        EntityCopier copier = createCopier(cc);
        copier.copyTo(iface, dest, orig);
      }
    }
  }

  /**
   * Creates the copier.
   *
   * @param cc the cc
   * @return the entity copier
   */
  private static EntityCopier createCopier(Class<? extends EntityCopier> cc)
  {
    try {
      return cc.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new LoggedRuntimeException(LogLevel.Fatal, GenomeLogCategory.Jpa,
          "Cannot create instance of: " + cc.getName() + "; " + e.getMessage(), new LogExceptionAttribute(e));
    }
  }

}
