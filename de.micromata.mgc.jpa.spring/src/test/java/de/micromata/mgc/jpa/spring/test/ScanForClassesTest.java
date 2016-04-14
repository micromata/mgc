//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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
