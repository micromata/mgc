package de.micromata.mgc.jettystarter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.event.MgcEventRegistries;
import de.micromata.mgc.launcher.AbstractMgcApplicationStartStopListener;
import de.micromata.mgc.launcher.MgcApplicationStartStopEvent;

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

      jettyServer.start(new String[] {});
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
