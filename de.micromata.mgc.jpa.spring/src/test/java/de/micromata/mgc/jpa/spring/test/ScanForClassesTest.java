package de.micromata.mgc.jpa.spring.test;

import java.util.ServiceLoader;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class ScanForClassesTest
{
  @Test
  public void testScanAnot()
  {
    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

    scanner.addIncludeFilter(new AnnotationTypeFilter(PfPlugin.class));
    Set<BeanDefinition> canditidates = scanner.findCandidateComponents("de.micromata");
    for (BeanDefinition bd : canditidates) {
      System.out.println("testScanAnot: " + bd.getBeanClassName());
    }
  }

  @Test
  public void testScanIface()
  {
    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

    scanner.addIncludeFilter(new AssignableTypeFilter(Plugin.class));
    Set<BeanDefinition> canditidates = scanner.findCandidateComponents("de.micromata");
    for (BeanDefinition bd : canditidates) {
      System.out.println("testScanIface: " + bd.getBeanClassName());
    }
  }

  @Test
  public void testGetService()
  {
    ServiceLoader<Plugin> plugins = ServiceLoader.load(Plugin.class);
    for (Plugin p : plugins) {
      System.out.println("Plugin: " + p.getClass().getName());
    }
  }
}
