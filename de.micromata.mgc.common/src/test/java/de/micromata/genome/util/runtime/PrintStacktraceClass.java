package de.micromata.genome.util.runtime;

import java.io.PrintStream;
import java.util.Map;

import org.junit.Test;

public class PrintStacktraceClass
{
  @Test
  public void testPrintStack()
  {
    PrintStream out = System.out;
    Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
    for (Map.Entry<Thread, StackTraceElement[]> me : map.entrySet()) {
      out.println("\n" + me.getKey() + "\n");
      for (StackTraceElement el : me.getValue()) {
        out.println(el.toString());
      }
    }
  }
}
