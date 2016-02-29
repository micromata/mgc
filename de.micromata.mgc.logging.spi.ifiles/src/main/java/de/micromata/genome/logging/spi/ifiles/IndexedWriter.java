package de.micromata.genome.logging.spi.ifiles;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.spi.ifiles.IndexHeader.StdSearchFields;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.Pair;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IndexedWriter implements Closeable
{
  public static final String logFileDateFormatString = "yyyy-MM-dd'T'HH_mm_ss_SSS";
  /**
   * Standard date format according to ISO 8601.
   */
  public static ThreadLocal<SimpleDateFormat> logFileDateFormat = new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat(logFileDateFormatString, Locale.US);
    }
  };
  private boolean flushAfterLog = false;
  private File logFile;
  private File indexFile;
  private long maxSize;
  private int bufferSize = 10000;

  private PosTrackingOutputStream logOut;
  private Writer logWriter;
  private OutputStream idxOut;
  private Collection<LogAttributeType> searchAttributes;
  private IndexHeader indexHeader;

  public IndexedWriter(IndexFileLoggingImpl logger, File logFile, File indexFile, long maxSize) throws IOException
  {
    this.logFile = logFile;
    this.indexFile = indexFile;
    this.maxSize = maxSize;
    searchAttributes = logger.getSearchAttributes();
    checkDirExists();
    moveExistantLogFiles(logger);
    openOuts(logger);
  }

  private void openOuts(IndexFileLoggingImpl logger) throws IOException
  {
    OutputStream out = new FileOutputStream(logFile);
    if (bufferSize > 0) {
      out = new BufferedOutputStream(out, bufferSize);
    }
    logOut = new PosTrackingOutputStream(out);
    logWriter = new OutputStreamWriter(logOut);
    idxOut = createIndexFile(logger);

  }

  protected OutputStream createIndexFile(IndexFileLoggingImpl logger) throws IOException, FileNotFoundException
  {
    indexHeader = IndexHeader.getIndexHeader(searchAttributes);

    FileOutputStream fout = new FileOutputStream(indexFile, true);
    indexHeader.writeFileHeader(fout);
    return fout;
  }

  private void checkDirExists()
  {
    File parentFile = logFile.getParentFile();
    if (parentFile.exists() == false) {
      parentFile.mkdirs();
    }
  }

  static String getFileDateSuffix(long millsecs)
  {
    String timestamp = logFileDateFormat.get().format(new Date(millsecs));
    timestamp = "_" + timestamp.replace(':', '_');
    return timestamp;
  }

  static IndexedWriter openWriter(IndexFileLoggingImpl logger) throws IOException
  {

    String newFileName = logger.getBaseFileName();
    IndexedWriter writer = new IndexedWriter(logger, new File(logger.getLogDir(), newFileName + ".log"),
        new File(logger.getLogDir(), newFileName + ".idx"), logger.getSizeLimit());
    return writer;

  }

  public boolean moveExistantLogFiles(IndexFileLoggingImpl logger) throws IOException
  {
    //    File logFile = new File(logger.getBaseFileName() + ".log");
    //    File idxFile = new File(logger.getBaseFileName() + ".idx");
    if (logFile.exists() == false && indexFile.exists() == false) {
      return true;
    }
    BasicFileAttributes attributes;
    if (logFile.exists() == true) {
      attributes = Files.readAttributes(Paths.get(logFile.getAbsolutePath()), BasicFileAttributes.class);
    } else {
      attributes = Files.readAttributes(Paths.get(indexFile.getAbsolutePath()), BasicFileAttributes.class);
    }
    boolean success = true;
    String suffix = getFileDateSuffix(attributes.lastModifiedTime().toMillis());
    if (logFile.exists() == true) {
      File nf = new File(logFile.getParentFile(), logger.getBaseFileName() + suffix + ".log");
      success &= logFile.renameTo(nf);
    }
    if (indexFile.exists() == true) {
      File nf = new File(indexFile.getParentFile(), logger.getBaseFileName() + suffix + ".idx");
      success &= indexFile.renameTo(nf);
    }

    return true;
  }

  public IndexedWriter write(IndexFileLoggingImpl logger, LogWriteEntry lwe) throws IOException

  {
    if (searchAttributes != logger.getSearchAttributes()) {
      // TODO rk wrapp around.
    }
    IndexedWriter writer = this;
    if (logOut.getPosition() > maxSize) {
      writer = overflow(logger);
    }
    writer.writeImpl(logger, lwe);
    return writer;
  }

  IndexedWriter overflow(IndexFileLoggingImpl logger) throws IOException
  {
    close();
    return openWriter(logger);
  }

  protected void writeImpl(IndexFileLoggingImpl logger, LogWriteEntry lwe)
  {
    try {
      writeCurrentPosition(lwe);
      writeLogHeader(lwe);
      writeAttributes(lwe);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }

  }

  private String trimFixSize(String str, int size)
  {
    if (str.length() > size) {
      str = str.substring(0, size);
    }
    str = StringUtils.rightPad(str, size);
    return str;
  }

  private String getAttributeValue(LogWriteEntry lwe, String name)
  {
    for (LogAttribute la : lwe.getAttributes()) {
      if (name.equals(IndexHeader.getNormalizedHeaderName(la.getTypeName())) == true) {
        return la.getValue();
      }
    }
    return "";
  }

  private void writeLogHeader(LogWriteEntry lwe) throws IOException
  {
    for (StdSearchFields sf : StdSearchFields.values()) {
      String s = sf.getFieldExtractor().apply(lwe);
      s = trimFixSize(s, sf.getSize());
      logWriter.write(s);
      logWriter.write("|");
    }
    for (Pair<String, Integer> headerp : indexHeader.headerOrder) {
      String value = getAttributeValue(lwe, headerp.getFirst());
      value = trimFixSize(value, headerp.getSecond());
      logWriter.write(value);
      logWriter.write("|");
    }

  }

  private void writeAttributes(LogWriteEntry lwe) throws IOException
  {
    // TODO rk lange nachricht schreiben
    logWriter.write("\n");
    for (LogAttribute la : lwe.getAttributes()) {
      if (la.isSearchKey() == true) {
        continue;
      }
      writeLongAttribute(lwe, la);
    }
  }

  private void writeLongAttribute(LogWriteEntry lwe, LogAttribute la) throws IOException
  {
    logWriter.write(la.getTypeName());
    logWriter.write("=");
    logWriter.write(la.getValueToWrite(lwe));
    logWriter.write("\n");

  }

  private void writeCurrentPosition(LogWriteEntry lwe) throws IOException
  {
    ByteBuffer bb = ByteBuffer.wrap(new byte[Long.BYTES]);
    bb.putLong(logOut.getPosition());
    idxOut.write(bb.array());
    bb.putLong(lwe.getTimestamp());
  }

  @Override
  public void close() throws IOException
  {
    logWriter.flush();
    logOut.close();
    idxOut.close();
  }

}
