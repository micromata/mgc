package de.micromata.genome.logging.spi.ifiles;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import groovy.json.internal.Charsets;

/**
 * 
 * Keep track of all indexed log files.
 * 
 * Format:
 * 
 * fileType, fileVersion, writeoffset, counter, [counter,mindate, maxdate, filename]*
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IndexDirectory implements Closeable
{
  public static final byte[] INDEX_FILE_TYPE = "GCLGFXID".getBytes();
  public static final byte[] INDEX_FILE_VERSION = "1".getBytes();
  public static final int LAST_WRITE_POS_OFFSET = INDEX_FILE_TYPE.length + INDEX_FILE_VERSION.length;
  public static final int HEADER_SIZE = LAST_WRITE_POS_OFFSET + Integer.BYTES;
  public static final int LOG_FILE_NAME_SIZE = 100;
  public static final int ROWIX_SIZE = Integer.BYTES;
  public static final int ROW_SIZE = Long.BYTES + Long.BYTES + LOG_FILE_NAME_SIZE;

  /**
   * Memory mapped 100 MB for directory index
   */
  //  public static final int DIRIDX_FILE_SIZE_INC = 1024 * 1024 * 1;
  public static final int DIRIDX_FILE_SIZE_INC = 1024 * 4;

  public static IndexDirectory open(File logDir, String baseFileName) throws IOException
  {
    File ixdFile = new File(logDir, baseFileName + ".ixd");
    return new IndexDirectory(ixdFile);
  }

  private File ixdFile;
  RandomAccessFile idxRandomAccessFile;
  FileChannel indexChannel;
  MappedByteBuffer indexByteBuffer;

  public IndexDirectory(File ixdFile) throws IOException
  {
    this.ixdFile = ixdFile;
    boolean createNew = false;
    int mappedSize = DIRIDX_FILE_SIZE_INC;
    if (ixdFile.exists() == false) {
      createNew = true;
    }
    idxRandomAccessFile = new RandomAccessFile(ixdFile, "rw");
    indexChannel = idxRandomAccessFile.getChannel();
    int filesize = (int) indexChannel.size();
    indexByteBuffer = indexChannel.map(FileChannel.MapMode.READ_WRITE, 0,
        DIRIDX_FILE_SIZE_INC > filesize ? DIRIDX_FILE_SIZE_INC : filesize);
    if (createNew == true) {
      writeNew();
    } else {
      loadVerify();
    }
  }

  private void writeNew() throws IOException
  {
    indexByteBuffer.position(0);
    indexByteBuffer.put(INDEX_FILE_TYPE);
    indexByteBuffer.put(INDEX_FILE_VERSION);
    indexByteBuffer.putInt(HEADER_SIZE);
    indexByteBuffer.putInt(1);
  }

  private void loadVerify() throws IOException
  {
    if (((int) indexChannel.size()) < HEADER_SIZE + ROW_SIZE) {
      return;
    }
    indexByteBuffer.position((int) indexChannel.size() - ROW_SIZE);
  }

  private static String getDirectoryNameFromFile(File file)
  {
    String name = file.getName();
    if (name.endsWith(".idx") == true) {
      name = name.substring(0, name.length() - 4);
    }
    return name;
  }

  private static String fileToStoredName(File logFile)
  {
    String name = getDirectoryNameFromFile(logFile);
    String lwwrite = StringUtils.substring(name, 0, LOG_FILE_NAME_SIZE);
    lwwrite = StringUtils.rightPad(lwwrite, LOG_FILE_NAME_SIZE);
    return lwwrite;
  }

  private int getWritePos()
  {
    return indexByteBuffer.getInt(LAST_WRITE_POS_OFFSET);
  }

  private void setNewWritePos(int offset)
  {
    indexByteBuffer.putInt(LAST_WRITE_POS_OFFSET, offset);

  }

  public int createNewLogIdxFile(File logFile) throws IOException
  {
    int pos = getWritePos();
    int retpos = pos;
    String lwwrite = fileToStoredName(logFile);
    indexByteBuffer.putLong(pos, 0);
    pos += Long.BYTES;
    indexByteBuffer.putLong(pos, 0);
    pos += Long.BYTES;
    NIOUtils.write(indexByteBuffer, pos, lwwrite);
    pos += lwwrite.length();
    setNewWritePos(pos);
    return retpos;
  }

  @Override
  public void close() throws IOException
  {
    IOUtils.closeQuietly(idxRandomAccessFile);
    IOUtils.closeQuietly(indexChannel);

  }

  public void renameFile(File oldFile, File newFile) throws IOException
  {
    String oldName = getDirectoryNameFromFile(oldFile);

    indexByteBuffer.position(HEADER_SIZE);
    byte[] nameBuffer = new byte[LOG_FILE_NAME_SIZE];
    while (indexByteBuffer.position() + ROW_SIZE < indexChannel.size()) {
      indexByteBuffer.getInt();
      indexByteBuffer.get(nameBuffer);
      String trimmed = new String(nameBuffer).trim();
      String fnt = StringUtils.substring(oldName, 0, LOG_FILE_NAME_SIZE);
      if (StringUtils.equalsIgnoreCase(trimmed, fnt) == true) {
        String lwwrite = fileToStoredName(newFile);
        indexByteBuffer.position(indexByteBuffer.position() - LOG_FILE_NAME_SIZE);
        indexByteBuffer.put(lwwrite.getBytes(Charsets.US_ASCII));
        break;
      }

    }
  }

  public void updateDate(int indexDirectoryIdx, long timestamp)
  {
    long start = indexByteBuffer.getLong(indexDirectoryIdx);
    long end = indexByteBuffer.getLong(indexDirectoryIdx + Long.BYTES);
    if (start == 0 || start > timestamp) {
      indexByteBuffer.putLong(indexDirectoryIdx, timestamp);
    }
    if (end < timestamp) {
      indexByteBuffer.putLong(indexDirectoryIdx + Long.BYTES, timestamp);
    }
  }

  public List<String> getLogFileCandiates(Timestamp start, Timestamp end)
  {
    TreeMap<Long, String> files = new TreeMap<>();

    //    int offset = HEADER_SIZE;
    int max = getWritePos() - ROW_SIZE;
    int offset = max;

    while (offset >= HEADER_SIZE) {
      long st = indexByteBuffer.getLong(offset);
      if (start != null) {
        if (st < start.getTime()) {
          offset -= ROW_SIZE;
          continue;
        }
      }
      if (end != null) {
        long et = indexByteBuffer.getLong(offset + Long.BYTES);
        if (et > end.getTime()) {
          offset -= ROW_SIZE;
          continue;
        }
      }
      indexByteBuffer.position(offset + Long.BYTES + Long.BYTES);
      byte[] nameBuffer = new byte[LOG_FILE_NAME_SIZE];
      indexByteBuffer.get(nameBuffer);
      String trimmed = new String(nameBuffer).trim();
      files.put(st, trimmed);
      offset -= ROW_SIZE;
    }
    List<String> ret = new ArrayList<>(files.values());
    return ret;
  }

  public static long buildLogIdByIndexDirectoryAndOffset(int indexDir, int offset)
  {
    long pk = ((long) indexDir) << 32;
    pk += offset;
    return pk;
  }

  public String findLogFileNameByLogId(long logId)
  {
    int indexdir = (int) (logId >>> 32);
    int offset = indexdir + 2 * Long.BYTES;
    byte[] nameBuffer = new byte[LOG_FILE_NAME_SIZE];
    indexByteBuffer.position(offset);
    indexByteBuffer.get(nameBuffer);
    String trimmed = new String(nameBuffer).trim();
    return trimmed;
  }

  public int getLogIndexOffsetFromLogId(long logid)
  {
    return (int) logid;
  }
}
