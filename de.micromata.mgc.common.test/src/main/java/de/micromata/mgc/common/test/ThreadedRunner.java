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

package de.micromata.mgc.common.test;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.micromata.genome.util.runtime.RuntimeCallable;

/**
 * A Utility to run a RuntimeCallable in multiple threads in loops
 * 
 * @author roger
 * 
 */
public class ThreadedRunner
{
  private int loops = 1;

  private int threadCount;

  private Thread[] threads;

  private long startTime;

  final StringBuilder exeptions = new StringBuilder();

  public ThreadedRunner(int loops, int threadCount)
  {
    super();
    this.loops = loops;
    this.threadCount = threadCount;
  }

  public void run(final RuntimeCallable caller)
  {
    start(caller);
    join();
  }

  public void join()
  {
    try {
      for (Thread t : threads) {
        t.join();
      }
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
    long end = System.currentTimeMillis();
    long dif = end - startTime;
    double difPerOpOverall = (double) dif / (threadCount * loops);
    double difPerOp = difPerOpOverall * threadCount;
    System.out.println("Runned threaded  test in "
        + dif
        + " ms with "
        + threadCount
        + " threads in "
        + loops
        + " loops. Per op overall: "
        + difPerOpOverall
        + " ms"
        + "; op average: "
        + difPerOp
        + " ms");
    if (exeptions.length() > 0) {
      System.err.println(exeptions);
      throw new RuntimeException("One or more Threads failed: " + exeptions);
    }

  }

  public void start(final RuntimeCallable caller)
  {
    startTime = System.currentTimeMillis();
    threads = new Thread[threadCount];
    for (int i = 0; i < threadCount; ++i) {
      final int intPrefix = i;
      threads[i] = new Thread(new Runnable() {

        @Override
        public void run()
        {
          try {
            for (int i = 0; i < loops; ++i) {
              synchronized (ThreadedRunner.this) {
                if (exeptions.length() > 0) {
                  break;
                }
              }
              caller.call();
            }
          } catch (Exception ex) {
            synchronized (ThreadedRunner.this) {
              exeptions.append("\n\nThread " + Thread.currentThread().getId() + " faield:\n").append(ExceptionUtils.getFullStackTrace(ex));
            }
          }
        }

      });
    }
    for (Thread t : threads) {
      t.start();
    }
  }
}
