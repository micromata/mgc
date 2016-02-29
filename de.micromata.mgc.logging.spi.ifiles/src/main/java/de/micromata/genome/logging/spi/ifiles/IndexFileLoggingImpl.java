package de.micromata.genome.logging.spi.ifiles;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.EndOfSearch;
import de.micromata.genome.logging.LogEntryCallback;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.Pair;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IndexFileLoggingImpl extends BaseLogging
{
  private String lsPrefix = "";

  private File logDir;
  private String baseFileName;
  private long sizeLimit = 50 * 1024 * 1024;
  private IndexedWriter currentWriter;

  public IndexFileLoggingImpl()
  {
    initViaLc();
  }

  public IndexFileLoggingImpl(boolean manual)
  {
    if (manual == false) {
      initViaLc();
    }
  }

  private void initViaLc()
  {
    LocalSettings ls = LocalSettings.get();
    logDir = new File(ls.get(getLsKey("iflog.logDir"), "./logs"));
    baseFileName = ls.get(getLsKey("iflog.baseFileName"), "GenomeIF");
    sizeLimit = ls.getLongValue(getLsKey("iflog.sizeLimit"), 1024 * 1024 * 10);
    initialize();
  }

  public void initialize()
  {
    try {
      currentWriter = IndexedWriter.openWriter(this);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
    Runtime.getRuntime().addShutdownHook(new Thread()
    {
      @Override
      public void run()
      {
        try {
          currentWriter.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
  }

  @Override
  public String formatLogId(Object logId)
  {
    return ObjectUtils.toString(logId);
  }

  @Override
  public Object parseLogId(String logId)
  {
    return logId;
  }

  @Override
  public void doLogImpl(LogWriteEntry lwe)
  {
    synchronized (this) {
      if (lwe.getTimestamp() == 0) {
        lwe.setTimestamp(System.currentTimeMillis());
      }
      try {
        currentWriter = currentWriter.write(this, lwe);
      } catch (IOException ex) {

      }
    }
  }

  @Override
  protected void selectLogsImpl(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
      LogEntryCallback callback) throws EndOfSearch
  {
    List<Pair<File, File>> candiates = getCandiates(start, end);
    if (candiates.isEmpty() == true) {
      return;
    }
    List<Pair<String, String>> normAttributes = new ArrayList<>(logAttributes);
    for (Pair<String, String> pa : logAttributes) {
      normAttributes.add(Pair.make(IndexHeader.getNormalizedHeaderName(pa.getFirst()), pa.getSecond()));
    }
    for (Pair<File, File> idxLog : candiates) {
      try {
        IndexedReader idxreader = new IndexedReader(this, idxLog.getSecond(), idxLog.getFirst());
        idxreader.selectLogsImpl(start, end, loglevel, category, msg, normAttributes, startRow, maxRow, orderBy,
            masterOnly, callback);
      } catch (IOException ex) {

      }

    }

  }

  @Override
  protected void selectLogsImpl(List<Object> logId, boolean masterOnly, LogEntryCallback callback) throws EndOfSearch
  {
    // TODO Auto-generated method stub

  }

  private List<Pair<File, File>> getCandiates(Timestamp start, Timestamp end)
  {
    File[] result = getLogDir().listFiles((dir, file) -> {
      if (file.startsWith(getBaseFileName()) == true
          && (file.endsWith(".log") == true || file.endsWith(".idx") == true)) {
        return true;
      }
      return false;
    });
    List<Pair<File, File>> ret = new ArrayList<>();
    TreeMap<Long, Pair<File, File>> byDate = new TreeMap<>();
    for (File f : result) {
      long date = getDateFromFileName(f);

      byDate.putIfAbsent(date, new Pair<File, File>());
      if (f.getName().endsWith(".idx") == true) {
        byDate.get(date).setFirst(f);
      } else {
        byDate.get(date).setSecond(f);
      }
    }

    for (Map.Entry<Long, Pair<File, File>> me : byDate.entrySet()) {
      if (start != null && start.getTime() < me.getKey()) {
        continue;
      }
      if (end != null && end.getTime() > me.getKey()) {
        break;
      }
      ret.add(me.getValue());
    }
    return ret;

  }

  private long getDateFromFileName(File f)
  {
    String name = f.getName();
    name = name.substring(0, name.length() - 4); // suffix 
    name = name.substring(getBaseFileName().length());
    if (name.length() < IndexedWriter.logFileDateFormatString.length()) {
      return 0;
    }
    try {
      Date date = IndexedWriter.logFileDateFormat.get().parse(name);
      return date.getTime();
    } catch (ParseException ex) {
      return 0;
    }
  }

  @Override
  public boolean supportsSearch()
  {
    return true;
  }

  @Override
  public boolean supportsFulltextSearch()
  {
    return true;
  }

  private String getLsKey(String key)
  {
    if (StringUtils.isBlank(lsPrefix) == true) {
      return key;
    }
    return lsPrefix + "." + key;
  }

  public File getLogDir()
  {
    return logDir;
  }

  public void setLogDir(File logDir)
  {
    this.logDir = logDir;
  }

  public String getBaseFileName()
  {
    return baseFileName;
  }

  public void setBaseFileName(String baseFileName)
  {
    this.baseFileName = baseFileName;
  }

  public long getSizeLimit()
  {
    return sizeLimit;
  }

  public void setSizeLimit(long sizeLimit)
  {
    this.sizeLimit = sizeLimit;
  }

  public IndexedWriter getCurrentWriter()
  {
    return currentWriter;
  }

  public void setCurrentWriter(IndexedWriter currentWriter)
  {
    this.currentWriter = currentWriter;
  }

}
