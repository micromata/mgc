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
