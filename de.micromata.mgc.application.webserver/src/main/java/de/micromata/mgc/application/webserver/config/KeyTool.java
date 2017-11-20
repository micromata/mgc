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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.util.validation.ValContext;

public class KeyTool
{
  public static void generateKey(ValContext ctx, File keyFile, String storePass, String keyAlias)
  {
    String[] args = { "keytool", "-genkey", "-alias", keyAlias, "-keyalg", "RSA", "-keystore",
        keyFile.getAbsolutePath(), "-keysize", "2048", "-keypass", storePass, "-storepass", storePass, "-dname",
        "cn=Launcher, ou=MGC, o=Microamta, c=DE" };
    StringBuilder oksb = new StringBuilder();
    oksb.append("Execute: " + StringUtils.join(args, " "));
    try {
      ProcessBuilder pb = new ProcessBuilder(args);
      pb.redirectErrorStream(true);
      Process process = pb.start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line;
      while ((line = br.readLine()) != null) {
        oksb.append(line);
      }
      boolean success = process.waitFor(5, TimeUnit.SECONDS);
      if (success == false) {
        ctx.directError(null, "Fail to wait for keytool");
      } else {
        int exitValue = process.exitValue();
        if (exitValue == 0) {
          oksb.append("\nSuccess");
          ctx.directInfo(null, oksb.toString());
        } else {
          ctx.directError(null, oksb.toString());
          ctx.directError(null, "Failure executing keytool. ReturnCode: " + exitValue);
        }
      }
    } catch (Exception ex) {
      ctx.directError(null, "Failure executing keytool: " + ex.getMessage(), ex);
    }
  }
}
