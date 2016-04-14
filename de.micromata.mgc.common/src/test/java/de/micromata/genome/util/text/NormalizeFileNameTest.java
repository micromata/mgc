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
