package de.micromata.genome.logging.spi.ifiles;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.logging.CollectLogEntryCallback;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LoggingContext;
import de.micromata.genome.util.types.Pair;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class WriteLogsTest extends IFileTestBase
{

  @Test
  public void testWrite()
  {

    long startMs = System.currentTimeMillis();
    LoggingContext.pushLogAttribute(new LogAttribute(GenomeAttributeType.AdminUserName, "testuser"));
    int count = 1000;
    for (int i = 0; i < count; ++i) {
      if ((i % 10) == 0) {
        GLog.error(GenomeLogCategory.UnitTest,
            "Da ist was schlimmes passiert, sollte man nachschauen, besser waers" + i,
            new LogExceptionAttribute(new RuntimeException("Ojeoje")));
      } else if ((i % 3) == 0) {
        GLog.info(GenomeLogCategory.UnitTest, "Hallo nachricht mit attr " + i,
            new LogAttribute(GenomeAttributeType.Miscellaneous, "Eintrag ohne newline"),
            new LogAttribute(GenomeAttributeType.Miscellaneous2,
                "Eintrag mit newline\nHiergehts weiter\nund da ist ende"));
      } else {
        GLog.info(GenomeLogCategory.UnitTest, "Hallo nachricht " + i);
      }
    }
    long endMs = System.currentTimeMillis();
    CollectLogEntryCallback callback = new CollectLogEntryCallback();
    logger.selectLogs(new Timestamp(startMs), new Timestamp(endMs + 1000), null, null, null, null, 0, 10000, null,
        false,
        callback);

    int selected = callback.getEntries().size();
    Assert.assertEquals(count, selected);

    callback = new CollectLogEntryCallback();
    logger.selectLogs(new Timestamp(startMs), new Timestamp(endMs + 1000), LogLevel.Error.getLevel(), null, null,
        null, 0, 10000, null, false, callback);
    selected = callback.getEntries().size();
    Assert.assertEquals(100, selected);

    // filter by attr
    List<Pair<String, String>> attrfilter = new ArrayList<>();
    attrfilter.add(Pair.make(GenomeAttributeType.AdminUserName.name(), "testuser"));
    callback = new CollectLogEntryCallback();
    logger.selectLogs(new Timestamp(startMs), new Timestamp(endMs + 1000), null, null, null,
        attrfilter, 0, 10000, null, false, callback);
    selected = callback.getEntries().size();
    Assert.assertEquals(1000, selected);

    attrfilter = new ArrayList<>();
    attrfilter.add(Pair.make(GenomeAttributeType.AdminUserName.name(), "unkown"));
    callback = new CollectLogEntryCallback();
    logger.selectLogs(new Timestamp(startMs), new Timestamp(endMs + 1000), null, null, null,
        attrfilter, 0, 10000, null, false, callback);
    selected = callback.getEntries().size();
    Assert.assertEquals(0, selected);

    // limit 
    callback = new CollectLogEntryCallback();
    logger.selectLogs(new Timestamp(startMs), new Timestamp(endMs + 1000), null, null, null, null, 0, 10, null,
        false,
        callback);
    selected = callback.getEntries().size();
    Assert.assertEquals(10, selected);
  }
}
