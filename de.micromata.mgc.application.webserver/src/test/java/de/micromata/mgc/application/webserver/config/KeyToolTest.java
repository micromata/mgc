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
