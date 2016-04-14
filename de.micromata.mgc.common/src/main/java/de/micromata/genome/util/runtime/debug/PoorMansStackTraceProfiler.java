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

package de.micromata.genome.util.runtime.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.matcher.BooleanListRulesFactory;
import de.micromata.genome.util.matcher.Matcher;
import de.micromata.genome.util.matcher.string.StartWithMatcher;
import de.micromata.genome.util.types.Holder;
import de.micromata.genome.util.types.Pair;

/**
 * In POP analoge Klasse zu StacktracePerf.
 * 
 * Siehe https://team.micromata.de/confluence/pages/viewpage.action?pageId=39064370
 * 
 * @author roger
 * 
 */
public class PoorMansStackTraceProfiler extends Thread
{

  /**
   * The sleeptime.
   */
  private long sleeptime = 5;

  /**
   * The max depth stack.
   */
  private int maxDepthStack = -1;

  /**
   * The stop.
   */
  private boolean stop = false;

  /**
   * The pause.
   */
  private boolean pause = false;

  /**
   * if set, ignores all stacktraces (and children) matching this.
   */
  private Matcher<String> ignoreMatcher = null;

  /**
   * The Class StackTracePart.
   */
  public static class StackTracePart
  {

    /**
     * The visit counter.
     */
    private int visitCounter = 1;

    /**
     * The parent.
     */
    private StackTracePart parent;

    /**
     * The childs.
     */
    private List<StackTracePart> childs = new ArrayList<StackTracePart>();

    /**
     * The stack trace element.
     */
    private StackTraceElement stackTraceElement;

    /**
     * Instantiates a new stack trace part.
     *
     * @param parent the parent
     * @param stackTraceElement the stack trace element
     */
    public StackTracePart(StackTracePart parent, StackTraceElement stackTraceElement)
    {
      this.parent = parent;
      this.stackTraceElement = stackTraceElement;
    }

    /**
     * Instantiates a new stack trace part.
     *
     * @param parent the parent
     * @param stackTraceElements the stack trace elements
     * @param offset the offset
     */
    public StackTracePart(StackTracePart parent, StackTraceElement[] stackTraceElements, int offset)
    {
      this(parent, stackTraceElements[stackTraceElements.length - offset]);
      if (stackTraceElements.length > offset + 1) {
        StackTracePart nc = new StackTracePart(this, stackTraceElements, ++offset);
        childs.add(nc);
      }
    }

    /**
     * Visit.
     *
     * @param stackTraceElements the stack trace elements
     * @param offset the offset
     */
    public void visit(StackTraceElement[] stackTraceElements, int offset)
    {
      ++visitCounter;
      if (stackTraceElements.length <= offset) {
        return;
      }
      StackTraceElement nc = stackTraceElements[stackTraceElements.length - offset];
      for (StackTracePart c : childs) {
        if (c.getStackTraceElement().equals(nc) == true) {
          c.visit(stackTraceElements, ++offset);
          return;
        }
      }
      StackTracePart ncc = new StackTracePart(this, stackTraceElements, offset);
      childs.add(ncc);
    }

    /**
     * Dump.
     *
     * @param sb the sb
     * @param indend the indend
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void dump(Appendable sb, String indend) throws IOException
    {
      dump(sb, indend, 100, 1);
    }

    /**
     * Dump.
     *
     * @param sb the sb
     * @param indend the indend
     * @param maxDepth the max depth
     * @param minHitCount the min hit count
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void dump(Appendable sb, String indend, int maxDepth, int minHitCount) throws IOException
    {
      sb.append(indend).append(stackTraceElement.toString()).append("  => " + visitCounter).append("\n");
      if (childs.isEmpty() == true) {
        return;
      }
      Collections.sort(childs, new Comparator<StackTracePart>()
      {

        @Override
        public int compare(StackTracePart o1, StackTracePart o2)
        {
          if (o1.getVisitCounter() == o2.getVisitCounter()) {
            return 0;
          }
          if (o1.getVisitCounter() < o2.getVisitCounter()) {
            return 1;
          }
          return -1;
        }
      });
      indend += "  ";
      int depth = 0;
      for (StackTracePart sp : childs) {
        if (sp.getVisitCounter() < minHitCount) {
          break;
        }
        sp.dump(sb, indend, maxDepth, minHitCount);
        ++depth;
        if (maxDepth != -1 && depth > maxDepth) {
          break;
        }
      }
    }

    public StackTracePart getParent()
    {
      return parent;
    }

    public void setParent(StackTracePart parent)
    {
      this.parent = parent;
    }

    public StackTraceElement getStackTraceElement()
    {
      return stackTraceElement;
    }

    public void setStackTraceElement(StackTraceElement stackTraceElement)
    {
      this.stackTraceElement = stackTraceElement;
    }

    public int getVisitCounter()
    {
      return visitCounter;
    }

    public void setVisitCounter(int visitCounter)
    {
      this.visitCounter = visitCounter;
    }

  }

  /**
   * The stm.
   */
  Map<StackTraceElement, Holder<Integer>> stm = new HashMap<StackTraceElement, Holder<Integer>>();

  /**
   * The trees.
   */
  Map<StackTraceElement, StackTracePart> trees = new HashMap<StackTraceElement, StackTracePart>();

  /**
   * Instantiates a new poor mans stack trace profiler.
   */
  public PoorMansStackTraceProfiler()
  {
    ignoreMatcher = new StartWithMatcher("de.micromata.genome.util.runtime.debug.PoorMansStackTraceProfiler");
  }

  /**
   * Instantiates a new poor mans stack trace profiler.
   *
   * @param maxDepthStack the max depth stack
   * @param excludeMatcher uses BooleanListRulesFactory to create a matcher, which ignores all stacktraces below
   *          matching.
   */
  public PoorMansStackTraceProfiler(int maxDepthStack, String excludeMatcher)
  {
    this.maxDepthStack = maxDepthStack;
    String expr = "(de.micromata.genome.util.runtime.debug.PoorMansStackTraceProfiler*)";
    if (StringUtils.isNotBlank(excludeMatcher) == true) {
      expr += " || " + excludeMatcher;
    }
    ignoreMatcher = new BooleanListRulesFactory<String>().createMatcher(expr);
  }

  /**
   * Ignore stack trace.
   *
   * @param se the se
   * @param depth the depth
   * @return true, if successful
   */
  private boolean ignoreStackTrace(StackTraceElement se, int depth)
  {
    if (maxDepthStack != -1 && depth > maxDepthStack) {
      return true;
    }
    if (ignoreMatcher == null) {
      return false;
    }
    String fqname = se.getClassName() + "." + se.getMethodName();
    if (ignoreMatcher.match(fqname) == true) {
      return true;
    }
    return false;
  }

  /**
   * Collect.
   *
   * @param thread the thread
   * @param se the se
   */
  public void collect(Thread thread, StackTraceElement[] se)
  {
    if (se.length == 0) {
      return;
    }
    int depth = 0;
    for (StackTraceElement el : se) {
      if (ignoreStackTrace(el, depth) == true) {
        break;
      }
      Holder<Integer> i = stm.get(el);
      if (i == null) {
        stm.put(el, new Holder<Integer>(1));
      } else {
        i.set(i.get() + 1);
      }
      ++depth;
    }
    StackTraceElement topTrace = se[se.length - 1];
    if (ignoreStackTrace(topTrace, se.length - 1) == true) {
      return;
    }
    StackTracePart part = trees.get(topTrace);
    if (part == null) {
      part = new StackTracePart(null, se, 1);
      trees.put(se[se.length - 1], part);
    } else {
      part.visit(se, 1);
    }
  }

  /**
   * Collect.
   */
  public void collect()
  {
    Map<Thread, StackTraceElement[]> ms = Thread.getAllStackTraces();
    for (Map.Entry<Thread, StackTraceElement[]> e : ms.entrySet()) {
      if (e.getKey() == this) {
        continue;
      }
      collect(e.getKey(), e.getValue());

    }
  }

  /**
   * Dump.
   */
  public void dump()
  {
    try {
      dumpStList(System.out);
      dumpStackTracePart(System.out);
    } catch (IOException ex) {

    }
  }

  /**
   * Dump stack trace part.
   *
   * @param sb the sb
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void dumpStackTracePart(Appendable sb) throws IOException
  {
    dumpStackTracePart(sb, 100, 1);
  }

  /**
   * Dump stack trace part.
   *
   * @param sb the sb
   * @param maxDepth the max depth
   * @param minHitCount the min hit count
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void dumpStackTracePart(Appendable sb, int maxDepth, int minHitCount) throws IOException
  {
    for (Map.Entry<StackTraceElement, StackTracePart> e : trees.entrySet()) {
      sb.append("\nThread Tree\n");
      e.getValue().dump(sb, "  ", maxDepth, minHitCount);
    }
  }

  /**
   * Dump st list.
   *
   * @param sb the sb
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void dumpStList(Appendable sb) throws IOException
  {
    dumpStList(sb, 1);
  }

  /**
   * Dump st list.
   *
   * @param sb the sb
   * @param minCallCount the min call count
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void dumpStList(Appendable sb, int minCallCount) throws IOException
  {
    List<Pair<Integer, StackTraceElement>> sl = new ArrayList<Pair<Integer, StackTraceElement>>(stm.size());
    for (Map.Entry<StackTraceElement, Holder<Integer>> e : stm.entrySet()) {
      sl.add(Pair.make(e.getValue().get(), e.getKey()));
    }
    Collections.sort(sl, new Comparator<Pair<Integer, StackTraceElement>>()
    {

      @Override
      public int compare(Pair<Integer, StackTraceElement> o1, Pair<Integer, StackTraceElement> o2)
      {
        if (o1.getFirst().equals(o2.getFirst()) == true) {
          return 0;
        }
        if (o1.getFirst() < o2.getFirst()) {
          return 1;
        }
        return -1;
      }
    });
    sb.append("\n\nAll Method:\n");
    for (Pair<Integer, StackTraceElement> p : sl) {
      if (p.getFirst() < minCallCount) {
        break;
      }
      sb.append(p.getFirst().toString()).append("  ").append(p.getSecond().toString()).append("\n");
    }

  }

  @Override
  public void run()
  {
    while (stop == false) {
      if (pause == false) {
        collect();
      }
      try {
        Thread.sleep(sleeptime);
      } catch (InterruptedException ex) {
        break;
      }
    }
    //    dump();
  }

  public void setPause(boolean pause)
  {
    if (pause == true && this.pause == false) {
      suspend();
    } else if (pause == false && this.pause == true) {
      resume();
    }
    this.pause = pause;

  }

  /**
   * Stop and wait.
   */
  public void stopAndWait()
  {
    setStop(true);
    setPause(false);
    try {
      this.join();
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
  }

  public boolean isStop()
  {
    return stop;
  }

  public void setStop(boolean stop)
  {
    this.stop = stop;
  }

  public long getSleeptime()
  {
    return sleeptime;
  }

  public void setSleeptime(long sleeptime)
  {
    this.sleeptime = sleeptime;
  }

  public boolean isPause()
  {
    return pause;
  }
}
