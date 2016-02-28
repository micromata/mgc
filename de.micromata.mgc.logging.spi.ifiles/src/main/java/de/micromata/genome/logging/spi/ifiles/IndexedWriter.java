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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.spi.ifiles.IndexHeader.StdSearchFields;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.DateUtils;
import de.micromata.genome.util.types.Pair;

public class IndexedWriter implements Closeable

{
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

  public IndexedWriter(IndexFileLoggingImpl logger, File logFile, File indexFile, long maxSize)
  {
    this.logFile = logFile;
    this.indexFile = indexFile;
    this.maxSize = maxSize;
    searchAttributes = logger.getSearchAttributes();
    checkDirExists();
    openOuts(logger);
  }

  private void openOuts(IndexFileLoggingImpl logger)
  {
    try {
      OutputStream out = new FileOutputStream(logFile);
      if (bufferSize > 0) {
        out = new BufferedOutputStream(out, bufferSize);
      }
      logOut = new PosTrackingOutputStream(out);
      logWriter = new OutputStreamWriter(logOut);
      idxOut = createIndexFile(logger);

    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }

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

  static IndexedWriter openWriter(IndexFileLoggingImpl logger)
  {
    String timestamp = DateUtils.getStandardDateTimeFormat().format(new Date());
    timestamp = timestamp.replace(':', '_');
    String newFileName = logger.getBaseFileName() + "_" + timestamp;
    IndexedWriter writer = new IndexedWriter(logger, new File(logger.getLogDir(), newFileName + ".log"),
        new File(logger.getLogDir(), newFileName + ".idx"), logger.getSizeLimit());

    return writer;

  }

  public IndexedWriter write(IndexFileLoggingImpl logger, LogWriteEntry lwe)
  {
    if (searchAttributes != logger.getSearchAttributes()) {
      // TODO rk wrapp around.
    }

    writeImpl(logger, lwe);
    return this;
  }

  protected void writeImpl(IndexFileLoggingImpl logger, LogWriteEntry lwe)
  {
    List<LogAttribute> las = lwe.getAttributes();
    try {
      writeCurrentPosition();
      writeLogHeader(lwe);
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

  private void writeCurrentPosition() throws IOException
  {
    ByteBuffer bb = ByteBuffer.wrap(new byte[Long.BYTES]);
    bb.putLong(logOut.getPosition());
    idxOut.write(bb.array());
  }

  @Override
  public void close() throws IOException
  {
    logWriter.flush();
    logOut.close();
    idxOut.close();
  }

}
