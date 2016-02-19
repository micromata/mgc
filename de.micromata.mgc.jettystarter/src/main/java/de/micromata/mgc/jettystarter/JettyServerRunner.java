package de.micromata.mgc.jettystarter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class JettyServerRunner
{
  public void runServer(JettyServer jettyServer)
  {
    try {
      // TODO RK
      //      System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
      //      System.out.println(">>> genomehome: " + genomeHome);
      //      System.out.println(">>> url: " + publicUrl);
      //      System.out.println(">>> wardir: " + warDir);
      //      System.out.println("type 'stop' on command line for stopping");
      jettyServer.start();
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
