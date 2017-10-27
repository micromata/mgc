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

package de.micromata.genome.logging.spi.ifiles;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.EndOfSearch;
import de.micromata.genome.logging.LogEntry;
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

  private void ensureLogDir()
  {
    if (logDir.exists() == true) {
      return;
    }
    logDir.mkdirs();

  }

  public void initialize()
  {
    ensureLogDir();
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

  private static Long toPk(Object logId)
  {
    if (logId instanceof Number) {
      return ((Number) logId).longValue();
    } else if (logId instanceof String) {
      return Long.parseLong((String) logId);
    } else {
      throw new IllegalArgumentException("Cannot parse LogId: " + logId);

    }
  }

  @Override
  protected void selectLogsImpl(List<Object> logIds, boolean masterOnly, LogEntryCallback callback) throws EndOfSearch
  {
    for (Object logId : logIds) {
      Long id = toPk(logId);
      try {
        LogEntry le = selectByLogId(id, masterOnly);
        if (le != null) {
          callback.onRow(le);
        }
      } catch (IOException ex) {

      }
    }
  }

  private LogEntry selectByLogId(long logid, boolean masterOnly) throws IOException
  {
    String name = indexDirectory.findLogFileNameByLogId(logid);
    if (StringUtils.isBlank(name) == true) {
      return null;
    }
    File idxLog = new File(logDir, name + ".idx");
    if (idxLog.exists() == false) {
      return null;
    }
    File log = new File(logDir, name + ".log");
    if (log.exists() == false) {
      return null;
    }
    int startOffset = indexDirectory.getLogIndexOffsetFromLogId(logid);
    try (IndexedReader idxreader = new IndexedReader(this, log, idxLog)) {
      return idxreader.select(startOffset, masterOnly);
    }
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
