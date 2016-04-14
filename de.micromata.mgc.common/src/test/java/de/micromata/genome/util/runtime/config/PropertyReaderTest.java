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

package de.micromata.genome.util.runtime.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.micromata.genome.util.runtime.config.OrderedPropertiesWithComments.Line;

public class PropertyReaderTest
{
  @Test
  public void testReadWithComments()
  {
    OrderedPropertiesWithComments props = new OrderedPropertiesWithComments();
    try {
      props.load(new FileInputStream("./dev/extrc/test/properties/ls_with_comments1.properties"));
      List<Line> lines = props.getOrginalLines();
      lines.isEmpty();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
