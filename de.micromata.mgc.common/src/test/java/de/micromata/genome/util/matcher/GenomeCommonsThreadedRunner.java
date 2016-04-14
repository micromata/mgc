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

package de.micromata.genome.util.matcher;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.micromata.genome.util.runtime.RuntimeCallable;

/**
 * A Utility to run a RuntimeCallable in multiple threads in loops
 * 
 * @author roger
 * 
 */
public class GenomeCommonsThreadedRunner
{
  int loops = 1;

  int threadCount;

  final StringBuilder exeptions = new StringBuilder();

  public GenomeCommonsThreadedRunner(int loops, int threadCount)
  {
    super();
    this.loops = loops;
    this.threadCount = threadCount;
  }

  public void run(final RuntimeCallable caller)
  {
    long start = System.currentTimeMillis();
    Thread[] threads = new Thread[threadCount];
    for (int i = 0; i < threadCount; ++i) {
      final int intPrefix = i;
      threads[i] = new Thread(new Runnable() {

        @Override
        public void run()
        {
          try {
            for (int i = 0; i < loops; ++i) {
              synchronized (GenomeCommonsThreadedRunner.this) {
                if (exeptions.length() > 0) {
                  break;
                }
              }
              caller.call();
            }
          } catch (Exception ex) {
            synchronized (GenomeCommonsThreadedRunner.this) {
              exeptions.append("\n\nThread " + Thread.currentThread().getId() + " faield:\n").append(ExceptionUtils.getFullStackTrace(ex));
            }
          }
        }

      });
    }
    for (Thread t : threads) {
      t.start();
    }
    try {
      for (Thread t : threads) {
        t.join();
      }
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
    long end = System.currentTimeMillis();
    long dif = end - start;
    double difPerOp = (double) dif / (threadCount * loops);
    System.out.println("Runned threaded  test in "
        + dif
        + " ms with "
        + threadCount
        + " threads in "
        + loops
        + " loops. Per op: "
        + difPerOp
        + " ms");
    if (exeptions.length() > 0) {
      System.err.println(exeptions);
      throw new RuntimeException("One or more Threads failed: " + exeptions);
    }
  }
}
