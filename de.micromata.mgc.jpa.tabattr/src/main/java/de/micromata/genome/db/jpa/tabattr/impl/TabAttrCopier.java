package de.micromata.genome.db.jpa.tabattr.impl;

import java.util.Set;
import java.util.TreeSet;

import de.micromata.genome.db.jpa.tabattr.api.EntityWithAttributes;
import de.micromata.genome.jpa.EntityCopier;

/**
 * The Class TabAttrCopier.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class TabAttrCopier implements EntityCopier
{

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public <T> void copyTo(Class<? extends T> iface, T dest, T orig)
  {
    if ((orig instanceof EntityWithAttributes) == false || (dest instanceof EntityWithAttributes) == false) {
      return;
    }
    copyAttributesTo((EntityWithAttributes) dest, (EntityWithAttributes) orig);

  }

  /**
   * Copy attributes to.
   *
   * @param dest the dest
   * @param orig the orig
   */
  public static void copyAttributesTo(EntityWithAttributes dest, EntityWithAttributes orig)
  {
    Set<String> destKeys = dest.getAttributeKeys();
    Set<String> origKeys = orig.getAttributeKeys();
    Set<String> insertOrgs = new TreeSet<String>(origKeys);
    insertOrgs.removeAll(destKeys);
    Set<String> updateOrgs = new TreeSet<String>(origKeys);
    updateOrgs.retainAll(destKeys);
    Set<String> deleteOrgs = new TreeSet<String>(destKeys);
    deleteOrgs.removeAll(origKeys);
    for (String insert : insertOrgs) {
      dest.putAttribute(insert, orig.getAttribute(insert));
    }
    for (String update : updateOrgs) {
      dest.putAttribute(update, orig.getAttribute(update));
    }
    for (String delete : deleteOrgs) {
      dest.removeAttribute(delete);
    }

  }
}
