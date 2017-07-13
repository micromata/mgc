package de.micromata.mgc.jpa.logging.sample;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import de.micromata.genome.db.jpa.logging.GenomeJpaLoggingImpl;
import de.micromata.genome.logging.CollectLogEntryCallback;
import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.util.runtime.LocalSettingsEnv;

/**
 * Sample for Logging into a DB.
 * 
 * Please refer also to the local-settings.properties, where you can find database settings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class HelloJpaLoggingMain
{
  public static void main(String[] args)
  {
    // do this in your main.
    // it looks for local-settings.properties in your working directory.
    // If you use InteliJ, and debug this, please make sure, application has correct working directory.
    // ensure env is intialized.
    LocalSettingsEnv.get();
    // set jpa logging
    LoggingServiceManager.get().setLogging(new GenomeJpaLoggingImpl());
    // use later to select logs
    Timestamp ts = new Timestamp(new Date().getTime());

    GLog.note(GenomeLogCategory.UnitTest, "My First Message");

    // now look, if we can find it in the database.
    CollectLogEntryCallback col = new CollectLogEntryCallback();

    LoggingServiceManager.get().getLogging().selectLogs(ts, null, null, null, null, null, 0, 30, null,
        false, col);
    List<LogEntry> entries = col.getEntries();
    for (LogEntry le : entries) {
      System.out.println("Got from MGC Logging DB. Message: " + le.getMessage());
    }
  }
}
