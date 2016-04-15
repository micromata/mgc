package de.micromata.mgc.application.webserver.config;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import org.junit.Test;

import de.micromata.genome.util.validation.ValContext;
import de.micromata.genome.util.validation.ValMessage;

public class KeyToolTest
{
  //@Test
  public void testLaunchKeyTool()
  {
    ValContext ctx = new ValContext();
    File keyfile = new File("./target/testkeystore");
    if (keyfile.exists() == true) {
      keyfile.delete();
    }
    KeyTool.generateKey(ctx, keyfile, "testmgc2016", "testmgc");
    List<ValMessage> messages = ctx.getMessages();
    for (ValMessage msg : messages) {
      System.out.println(msg.getMessage());
    }
  }

  @Test
  public void testDumpIps()
  {
    try {
      Enumeration e = NetworkInterface.getNetworkInterfaces();
      while (e.hasMoreElements()) {
        NetworkInterface n = (NetworkInterface) e.nextElement();
        Enumeration ee = n.getInetAddresses();
        while (ee.hasMoreElements()) {
          InetAddress i = (InetAddress) ee.nextElement();
          System.out.println(i.getHostAddress() + ": " + i.getHostName());
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
