package de.micromata.genome.util.text;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

public class NormalizeFileNameTest
{
  private void test(String fn)
  {
    String fnnorm = FilenameUtils.normalize(fn);
    File f = new File(fn);
    String abf = f.getAbsolutePath();
    System.out.println("UnNorm: " + fn + "; norm: " + fnnorm + "; abspath: " + abf);
  }

  @Test
  public void testNormalize()
  {
    String fn = "../genome-core\\dev/extrc/CfgRepository\\de.micromata.genome.web.stripes";
    test(fn);
    fn = "./../genome-core\\dev/extrc/CfgRepository\\de.micromata.genome.web.stripes";
    test(fn);

  }
}
