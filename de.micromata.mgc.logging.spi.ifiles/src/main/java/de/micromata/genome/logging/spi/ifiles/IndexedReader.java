package de.micromata.genome.logging.spi.ifiles;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.BaseLogging;
import de.micromata.genome.logging.EndOfSearch;
import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogAttributeTypeWrapper;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogEntryCallback;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.Logging.OrderBy;
import de.micromata.genome.util.types.Pair;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IndexedReader implements Closeable
{
  public static final Charset logCharset = IndexedWriter.logCharset;
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
    long chsize = indexChannel.size();
    indexByteBuffer = indexChannel.map(FileChannel.MapMode.READ_ONLY, 0, chsize);
    indexHeader = IndexHeader.openIndexHeader(indexByteBuffer, indexChannel.size());
  }

  public void selectLogsImpl(Timestamp start, Timestamp end, Integer loglevel, String category, String msg,
      List<Pair<String, String>> logAttributes, int startRow, int maxRow, List<OrderBy> orderBy, boolean masterOnly,
      LogEntryCallback callback) throws EndOfSearch, IOException
  {
    long indexMaxSize = indexChannel.size();
    List<Pair<Integer, Integer>> offsets = indexHeader.getCandiates(start, end, indexByteBuffer, indexMaxSize);
    if (offsets.isEmpty() == true) {
      return;
    }
    logRandomAccessFile = new RandomAccessFile(logFile, "r");
    logChannel = logRandomAccessFile.getChannel();
    logByteBuffer = logChannel.map(FileChannel.MapMode.READ_ONLY, 0, logChannel.size());
    for (Pair<Integer, Integer> offset : offsets) {
      if (apply(offset.getFirst(), loglevel, category, msg, logAttributes) == false) {
        continue;
      }
      callback.onRow(select(offset, masterOnly));
    }
  }

  boolean apply(Integer offset, Integer loglevel, String category, String msg, List<Pair<String, String>> logAttributes)
  {

    if (loglevel != null) {
      String s = indexHeader.readSearchFromLog(offset, logByteBuffer, StdSearchFields.Level.name());
      if (s != null) {
        LogLevel ll = LogLevel.fromString(s, LogLevel.Fatal);
        if (ll.getLevel() < loglevel) {
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

  private LogAttribute createLogAttribute(String name, String value)
  {
    Map<String, LogAttributeType> map = BaseLogging.getRegisterdLogAttributes();
    LogAttributeType attrt = map.get(name);
    if (attrt == null) {
      attrt = new LogAttributeTypeWrapper(name);
    }
    return new LogAttribute(attrt, value);
  }

  private long buildLogPk(int offset)
  {

    long pk = ((long) indexHeader.indexDirectoryIdx) << 32;
    pk += offset;
    return pk;
  }

  private LogEntry select(Pair<Integer, Integer> startEndOffset, boolean masterOnly) throws IOException
  {
    LogEntry le = new LogEntry();
    int startOffset = startEndOffset.getFirst();
    le.setLogEntryIndex(buildLogPk(startOffset));
    logChannel.position(startOffset);
    for (Pair<String, Integer> pair : indexHeader.headerOrder) {
      String name = pair.getFirst();
      Integer length = pair.getSecond();
      String value = NIOUtils.readString(logRandomAccessFile, length, logCharset);
      logRandomAccessFile.read(); // | character
      StdSearchFields sf = StdSearchFields.findFromString(name);

      if (sf != null) {
        sf.getValueSetter().accept(le, value);
      } else {
        le.getAttributes().add(createLogAttribute(name, value));
      }
    }
    if (masterOnly == true) {
      return le;
    }
    // TODO RK parse more
    return le;
  }

  @Override
  public void close() throws IOException
  {
    NIOUtils.unmap(indexChannel, indexByteBuffer);
    NIOUtils.unmap(logChannel, logByteBuffer);
    IOUtils.closeQuietly(indexChannel);
    IOUtils.closeQuietly(idxRandomAccessFile);
    IOUtils.closeQuietly(logChannel);
    IOUtils.closeQuietly(logRandomAccessFile);

  }

}
