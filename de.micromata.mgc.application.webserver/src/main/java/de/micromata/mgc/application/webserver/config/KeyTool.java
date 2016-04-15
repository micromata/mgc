package de.micromata.mgc.application.webserver.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

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
