package de.micromata.mgc.jpa.spring;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import de.micromata.genome.jpa.EmgrFactory;
import de.micromata.genome.jpa.events.EmgrEventHandler;
import de.micromata.genome.jpa.events.EmgrEventRegistry;

/**
 * The Class SpringEmgrFilterBean.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
@Component()
public class SpringEmgrFilterBean
{

  /**
   * The application context.
   */
  @Autowired
  private ApplicationContext applicationContext;

  /**
   * Register emgr filter.
   *
   * @param emgrFactory the emgr factory
   */
  public void registerEmgrFilter(EmgrFactory<?> emgrFactory)
  {
    Map<String, EmgrEventHandler> beans = applicationContext.getBeansOfType(EmgrEventHandler.class);
    EmgrEventRegistry eventFactory = emgrFactory.getEventFactory();
    for (EmgrEventHandler eh : beans.values()) {
      eventFactory.registerEvent(eh);
    }
  }
}
