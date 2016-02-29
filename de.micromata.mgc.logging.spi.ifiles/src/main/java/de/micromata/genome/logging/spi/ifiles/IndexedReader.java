package de.micromata.genome.logging.spi.ifiles;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.EndOfSearch;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogEntryCallback;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.Logging.OrderBy;
import de.micromata.genome.logging.spi.ifiles.IndexHeader.StdSearchFields;
import de.micromata.genome.util.types.Pair;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IndexedReader implements Closeable
{
  IndexFileLoggingImpl logger;
  File logFile;
  File indexFile;
  RandomAccessFile idxRandomAccessFile;
  FileChannel indexChannel;;
  MappedByteBuffer indexByteBuffer;

  RandomAccessFile logRandomAccessFile;
  FileChannel logChannel;;
  MappedByteBuffer logByteBuffer;
  IndexHeader indexHeader;

  public IndexedReader(IndexFileLoggingImpl logger, File logFile, File indexFile) throws IOException
  {
    this.logger = logger;
    this.logFile = logFile;
    this.indexFile = indexFile;
    idxRandomAccessFile = new RandomAccessFile(indexFile, "r");
    indexChannel = idxRandomAccessFile.getChannel();
    indexByteBuffer = indexChannel.map(FileChannel.MapMode.READ_ONLY, 0, indexChannel.size());
    indexHeader = IndexHeader.openIndexHeader(indexByteBuffer, indexChannel.size());
  }

  public void selectLogsImpl(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
      LogEntryCallback callback) throws EndOfSearch, IOException
  {
    List<Long> offsets = indexHeader.getCandiates(start, end, indexByteBuffer, indexChannel.size());
    if (offsets.isEmpty() == true) {
      return;
    }
    logRandomAccessFile = new RandomAccessFile(logFile, "r");
    logChannel = logRandomAccessFile.getChannel();
    logByteBuffer = logChannel.map(FileChannel.MapMode.READ_ONLY, 0, logChannel.size());
    for (Long offset : offsets) {
      if (apply(offset, loglevel, category, msg, logAttributes) == true) {
        continue;
      }
      callback.onRow(select(offset));
    }
  }

  boolean apply(Long offset, Integer loglevel, String category, String msg, List<Pair<String, String>> logAttributes)
  {
    if (loglevel != null) {
      String s = indexHeader.readSearchFromLog(offset, logByteBuffer, StdSearchFields.Level.name());
      if (s != null) {
        LogLevel ll = LogLevel.fromString(s, LogLevel.Fatal);
        if (ll.getLevel() <= loglevel) {
          return false;
        }
      }
      if (category != null) {
        s = indexHeader.readSearchFromLog(offset, logByteBuffer, StdSearchFields.Category.name());
        if (s != null) {
          if (StringUtils.containsIgnoreCase(s, category) == false) {
            return false;
          }
        }
      }
      if (msg != null) {
        s = indexHeader.readSearchFromLog(offset, logByteBuffer, StdSearchFields.ShortMessage.name());
        if (s != null) {
          if (StringUtils.containsIgnoreCase(s, msg) == false) {
            return false;
          }
        }
      }
      // TODO RK now log attributes
    }
    return true;
  }

  private LogEntry select(Long offset)
  {
    LogEntry le = new LogEntry();

    return le;
  }

  @Override
  public void close() throws IOException
  {
    IOUtils.closeQuietly(indexChannel);
    IOUtils.closeQuietly(idxRandomAccessFile);
    IOUtils.closeQuietly(logChannel);
    IOUtils.closeQuietly(logRandomAccessFile);
  }

  private static void unmap(FileChannel fc, MappedByteBuffer bb) throws Exception
  {
    // TODO RK do this in startup
    Class<?> fcClass = fc.getClass();
    java.lang.reflect.Method unmapMethod = fcClass.getDeclaredMethod("unmap",
        new Class[] { java.nio.MappedByteBuffer.class });
    unmapMethod.setAccessible(true);
    unmapMethod.invoke(null, new Object[] { bb });
  }
}
