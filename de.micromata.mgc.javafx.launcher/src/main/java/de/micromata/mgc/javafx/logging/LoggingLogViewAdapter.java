package de.micromata.mgc.javafx.logging;

import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.JsonArray;

import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.logging.loghtmlwindow.LogJsonUtils;
import de.micromata.genome.logging.spi.log4j.RoundList;
import netscape.javascript.JSObject;

public class LoggingLogViewAdapter
{
  private RoundList<LogWriteEntry> logWriteEntries = new RoundList<>(1000);
  JSObject logView;
  public boolean supportsPoll = true;
  public boolean supportsSearch = false;

  public void init(JSObject logView)
  {
    this.logView = logView;
    this.supportsSearch = LoggingServiceManager.get().getLogging().supportsSearch();
  }

  List<LogWriteEntry> getLogEntries(long lastPollTime)
  {
    synchronized (logWriteEntries) {
      List<LogWriteEntry> ret = new ArrayList<>();
      for (LogWriteEntry le : logWriteEntries) {
        if (le.getTimestamp() <= lastPollTime) {
          continue;
        }
        ret.add(le);
      }
      return ret;
    }
  }

  public void logPoll(long lastPollTime, JSObject callback)
  {
    List<LogWriteEntry> sicEntry = getLogEntries(lastPollTime);

    JsonArray ret = new JsonArray();
    for (LogWriteEntry le : sicEntry) {
      ret.add(LogJsonUtils.logEntryToJson(le));
    }
    String sret = ret.toString();
    callback.call("doCallback", sret);
  }

  public void logSelect(JSObject logFormData, JSObject callback)
  {

  }

  public void addLogEntry(LogWriteEntry le)
  {
    synchronized (logWriteEntries) {
      logWriteEntries.add(le);
    }
  }

  public void addLogEntries(List<LogWriteEntry> logentries)
  {
    synchronized (logWriteEntries) {
      logWriteEntries.addAll(logentries);
    }
  }

  public void jsdebug(String message)
  {
    System.out.println(message);
  }

  public void jserror(String message)
  {
    System.err.println(message);
  }

  public void jswarn(String message)
  {
    System.out.println(message);
  }
}
