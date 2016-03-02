package de.micromata.genome.logging.spi.ifiles;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
  protected IndexDirectory indexDirectory;

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
      indexDirectory = IndexDirectory.open(logDir, baseFileName);
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
          indexDirectory.close();
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
    List<Pair<String, String>> normAttributes = new ArrayList<>();

    if (logAttributes != null) {
      for (Pair<String, String> pa : logAttributes) {
        normAttributes.add(Pair.make(IndexHeader.getNormalizedHeaderName(pa.getFirst()), pa.getSecond()));
      }
    }
    for (Pair<File, File> idxLog : candiates) {
      try (IndexedReader idxreader = new IndexedReader(this, idxLog.getSecond(), idxLog.getFirst())) {
        idxreader.selectLogsImpl(start, end, loglevel, category, msg, normAttributes, startRow, maxRow, orderBy,
            masterOnly, callback);
      } catch (IOException ex) {
        // TODO RK ex
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
    List<Pair<File, File>> ret = new ArrayList<>();
    List<String> names = indexDirectory.getLogFileCandiates(start, end);
    for (String name : names) {
      File idx = new File(logDir, name + ".idx");
      if (idx.exists() == false) {
        continue;
      }
      File log = new File(logDir, name + ".log");
      if (log.exists() == false) {
        continue;
      }
      ret.add(Pair.make(idx, log));
    }
    return ret;

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