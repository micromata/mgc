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
    int skipRows = startRow;
    int rowsSelected = 0;
    int indexMaxSize = (int) indexChannel.size();
    List<Pair<Integer, Integer>> offsets = indexHeader.getCandiates(start, end, indexByteBuffer, indexMaxSize);
    if (offsets.isEmpty() == true) {
      return;
    }
    logRandomAccessFile = new RandomAccessFile(logFile, "r");
    logChannel = logRandomAccessFile.getChannel();
    logByteBuffer = logChannel.map(FileChannel.MapMode.READ_ONLY, 0, logChannel.size());
    if (orderBy != null && orderBy.isEmpty() == false) {

    }
    for (Pair<Integer, Integer> offset : offsets) {
      if (rowsSelected >= maxRow) {
        return;
      }
      if (apply(offset.getFirst(), loglevel, category, msg, logAttributes) == false) {
        continue;
      }
      if (skipRows > 0) {
        --skipRows;
        continue;
      }
      callback.onRow(select(offset.getFirst(), masterOnly));
      ++rowsSelected;
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
    }
    if (logAttributes == null || logAttributes.isEmpty() == true) {
      return true;
    }
    for (Pair<String, String> attr : logAttributes) {
      String s = indexHeader.readSearchFromLog(offset, logByteBuffer, attr.getFirst());
      if (StringUtils.containsIgnoreCase(s, attr.getSecond()) == false) {
        return false;
      }
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
    return IndexDirectory.buildLogIdByIndexDirectoryAndOffset(indexHeader.indexDirectoryIdx, offset);
  }

  private boolean seekNextLine(int fromPos) throws IOException
  {
    logRandomAccessFile.seek(fromPos);
    int ch;
    while ((ch = logRandomAccessFile.read()) != -1) {
      if (ch == '\n') {
        long curpos = logRandomAccessFile.getFilePointer();
        ch = logRandomAccessFile.read();
        if (ch == -1) {
          return false;
        }
        if (ch == '\r') {
          return true;
        } else {
          logRandomAccessFile.seek(curpos);
          return true;
        }
      }
    }
    return false;
  }

  public LogEntry select(int startOffset, boolean masterOnly) throws IOException
  {
    if (logRandomAccessFile == null) {
      logRandomAccessFile = new RandomAccessFile(logFile, "r");
      logChannel = logRandomAccessFile.getChannel();
      logByteBuffer = logChannel.map(FileChannel.MapMode.READ_ONLY, 0, logChannel.size());
    }

    LogEntry le = new LogEntry();
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
    if (seekNextLine(startOffset) == false) {
      return le;
    }
    int ch;
    do {
      ch = logRandomAccessFile.read();
      if (ch != '@') {
        break;
      }
      LogAttribute attr = parseLogAttributeFromPos();
      if (attr == null) {
        break;
      }
      if (attr.getType().name().equals("message") == true) {
        le.setMessage(attr.getValue());
      } else {
        le.getAttributes().add(attr);
      }
    } while (ch != -1);
    return le;
  }

  private LogAttribute parseLogAttributeFromPos() throws IOException
  {
    StringBuilder key = new StringBuilder();
    int ch = 0;
    boolean keyFound = false;
    while (keyFound == false && ch != -1) {
      ch = logRandomAccessFile.read();
      switch (ch) {
        case -1:
          return null;
        case '\r':
        case '\n':
          return null;
        case ':':
          keyFound = true;
          break;
        default:
          key.append((char) ch);
          break;
      }
    }
    if (key.length() == 0) {
      return null;
    }
    StringBuilder value = new StringBuilder();
    boolean valueFound = false;
    while (valueFound == false && ch != -1) {
      ch = logRandomAccessFile.read();
      switch (ch) {
        case '\n':
          long cpos = logRandomAccessFile.getFilePointer();
          ch = logRandomAccessFile.read();
          if (ch == '\r') {
            ++cpos;
            ch = logRandomAccessFile.read();
          }
          if (ch == '\t') {
            value.append("\n");
            continue;
          } else {
            logRandomAccessFile.seek(cpos);
            valueFound = true;
            break;
          }
        default:
          value.append((char) ch);
          break;
      }
    }
    String skey = key.toString();
    String svalue = value.toString();
    if (skey.startsWith(" ") == true) {
      skey = skey.substring(1);
    }
    if (svalue.startsWith(" ") == true) {
      svalue = svalue.substring(1);
    }
    return createLogAttribute(skey, svalue);
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
