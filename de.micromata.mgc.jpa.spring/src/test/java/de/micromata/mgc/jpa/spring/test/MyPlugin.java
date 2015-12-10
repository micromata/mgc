package de.micromata.mgc.jpa.spring.test;

@PfPlugin
public class MyPlugin implements Plugin
{
  static {
    System.out.println("MyPlugin class loaded");
  }
}
