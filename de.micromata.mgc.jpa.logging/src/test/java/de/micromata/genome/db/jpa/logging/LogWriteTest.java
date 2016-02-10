package de.micromata.genome.db.jpa.logging;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.logging.CollectLogEntryCallback;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeAttributeType;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.types.Pair;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * 
 */
public class LogWriteTest extends LogTestBase
{

  @Test
  public void testSimple()
  {

    BaseJpaLoggingImpl dao = new GenomeJpaLoggingImpl();

    LogWriteEntry we = new LogWriteEntry();
    we.setCategory("MyCat");
    we.setMessage("MyMessage");
    we.setLevel(LogLevel.Warn);

    dao.doLogImpl(we);

    LogAttribute a1 = new LogAttribute(GenomeAttributeType.Miscellaneous, "Misc value");
    List<LogAttribute> al = new ArrayList<LogAttribute>();
    al.add(a1);
    we.setAttributes(al);
    dao.doLogImpl(we);
  }

  @Test
  public void testWriteViaGLog()
  {
    try {
      GLog.info(GenomeLogCategory.Coding, "My long message, which should be shortenend for short part of the message");
      CollectLogEntryCallback col = new CollectLogEntryCallback();
      LoggingServiceManager.get().getLogging().selectLogs(null, null, null, null, null, null, 0, 30, null, true, col);
      System.out.println(col.toString());
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      ex.printStackTrace();
      throw ex;
    }
  }

  String createLongString(int count)
  {
    StringBuilder sb = new StringBuilder(count);
    for (int i = 0; i < count; ++i) {
      sb.append('x');
    }
    return sb.toString();
  }

  @Test
  public void testWriteLongLog()
  {
    GenomeJpaLoggingImpl dao = new GenomeJpaLoggingImpl();
    Timestamp ts = new Timestamp(new Date().getTime());
    String longLoge = createLongString(1000 * 10);
    String msg = "UnitTest; testWriteLongLog";
    dao.note(GenomeLogCategory.TestMode, msg,
        new LogAttribute(GenomeAttributeType.Miscellaneous, longLoge));
    CollectLogEntryCallback col = new CollectLogEntryCallback();
    dao.selectLogs(ts, null, null, null, msg, null, 0, 30, null,
        false, col);
    Assert.assertTrue(col.getEntries().size() > 0);
    LogAttribute attr = col.getEntries().get(0).getAttributeByType(GenomeAttributeType.Miscellaneous);
    Assert.assertNotNull(attr);
    Assert.assertEquals(longLoge, attr.getValue());
  }

  @Test
  public void testSearchAttributes()
  {
    GenomeJpaLoggingImpl dao = new GenomeJpaLoggingImpl();
    Timestamp ts = new Timestamp(new Date().getTime());

    String sessionId = "TST_MYSESSIONID123";
    String sessionId2 = "TST_MYSESSIONID124";
    String msg = "UnitTest; testSearchAttributes";
    dao.note(GenomeLogCategory.TestMode, msg,
        new LogAttribute(GenomeAttributeType.HttpSessionId, sessionId));
    dao.note(GenomeLogCategory.TestMode, msg,
        new LogAttribute(GenomeAttributeType.HttpSessionId, sessionId2));
    CollectLogEntryCallback col = new CollectLogEntryCallback();
    List<Pair<String, String>> searchLogs = new ArrayList<>();
    searchLogs.add(Pair.make(GenomeAttributeType.HttpSessionId.name(), sessionId));
    dao.selectLogs(ts, null, null, null, msg, searchLogs, 0, 30, null,
        true, col);
    Assert.assertTrue(col.getEntries().size() == 1);

  }
}
