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

package de.micromata.mgc.application.jetty;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.event.MgcEventRegistries;
import de.micromata.mgc.application.AbstractMgcApplicationStartStopListener;
import de.micromata.mgc.application.MgcApplicationStartStopEvent;

public class JettyServerRunner
{
  public void runServer(MgcApplicationWithJettyApplication<?> jettyServer)
  {

    try {
      // TODO RK
      //      System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
      //      System.out.println(">>> genomehome: " + genomeHome);
      //      System.out.println(">>> url: " + publicUrl);
      //      System.out.println(">>> wardir: " + warDir);
      //      System.out.println("type 'stop' on command line for stopping");

      MgcEventRegistries.getEventInstanceRegistry()
          .registerListener(new AbstractMgcApplicationStartStopListener()
          {

            @Override
            public void onEvent(MgcApplicationStartStopEvent event)
            {
              System.out.println(event.getValMessage().getI18nkey());
            }
          });

      jettyServer.startImpl(new String[] {});
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      do {
        String rl = in.readLine();
        if (StringUtils.equalsIgnoreCase(rl, "stop") == true) {
          break;
        }
      } while (true);
      System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");

      jettyServer.stopAndWait();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(100);
    }
  }

}
