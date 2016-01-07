package de.micromata.mgc.jpa.hibernatesearch.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import de.micromata.mgc.jpa.hibernatesearch.impl.ListHibernateFieldInfoProvider;

/**
 * Additionally information for an entity supporting hibernate search.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HibernateSearchInfo {

  Class<? extends HibernateSearchFieldInfoProvider> fieldInfoProvider() default ListHibernateFieldInfoProvider.class;

  /**
   * String encoded parameter passed to HibernateSearchFieldInfoProvider.
   * 
   * @return
   */
  String param() default "";

}
