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

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.Pair;

/**
 * TODO enable reaattach into existant base name, if limit is not reached or searchkeys are not different.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IndexedWriter implements Closeable
{
  /**
   * if change this to utf-8 makes it a little bit more complicate.
   */
  public static final Charset logCharset = Charsets.ISO_8859_1;
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
  private IndexDirectory indexDirectory;

  public IndexedWriter(IndexFileLoggingImpl logger, File logFile, File indexFile, long maxSize)
  {
    this.logFile = logFile;
    this.indexFile = indexFile;
    this.maxSize = maxSize;
    this.indexDirectory = logger.indexDirectory;

  }

  public boolean isOpen()
  {
    return idxOut != null;
  }

  public void open(IndexFileLoggingImpl logger) throws IOException
  {
    if (idxOut != null) {
      return;
    }
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
    logWriter = new OutputStreamWriter(logOut, logCharset);
    idxOut = createIndexFile(logger);

  }

  protected OutputStream createIndexFile(IndexFileLoggingImpl logger) throws IOException, FileNotFoundException
  {
    indexHeader = IndexHeader.getIndexHeader(searchAttributes);

    FileOutputStream fout = new FileOutputStream(indexFile, true);
    indexHeader.writeFileHeader(fout, indexFile, logger.indexDirectory);
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
      boolean ixren = indexFile.renameTo(nf);
      if (ixren == true) {
        logger.indexDirectory.renameFile(indexFile, nf);
      }
      success &= ixren;
    }

    return true;
  }

  public IndexedWriter write(IndexFileLoggingImpl logger, LogWriteEntry lwe) throws IOException
  {
    IndexedWriter writer = this;
    if (isOpen() == true) {
      if (searchAttributes != logger.getSearchAttributes()) {
        writer = overflow(logger);
      }

      if (writer.logOut.getPosition() > maxSize) {
        writer = writer.overflow(logger);
      }
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
      open(logger);
      writeCurrentPosition(lwe);
      writeLogHeader(lwe);
      writeAttributes(lwe);
      // had to flush, so PosTrackingOuputStream have no position
      logWriter.flush();
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
    // write attributes, but standard fields are already written.
    for (int i = StdSearchFields.values().length; i < indexHeader.headerOrder.size(); ++i) {
      Pair<String, Integer> headerp = indexHeader.headerOrder.get(i);
      String value = getAttributeValue(lwe, headerp.getFirst());
      value = trimFixSize(value, headerp.getSecond());
      logWriter.write(value);
      logWriter.write("|");
    }

  }

  private void writeAttributes(LogWriteEntry lwe) throws IOException
  {
    // TODO rk lange nachricht schreiben
    logWriter.write("\n@message: ");
    writeLongValue(lwe.getMessage());
    for (LogAttribute la : lwe.getAttributes()) {
      if (la.isSearchKey() == true) {
        continue;
      }
      writeLongAttribute(lwe, la);
    }
  }

  private void writeLongValue(String value) throws IOException
  {

    String rest = StringUtils.replace(value, "\r", "");
    int nlidx = rest.indexOf('\n');
    if (nlidx == -1) {
      logWriter.write(value);
      logWriter.write("\n");
      return;
    }
    boolean firstLine = true;
    while (nlidx != -1) {
      String left = rest.substring(0, nlidx + 1);
      if (firstLine == false) {
        logWriter.write('\t');
      }
      logWriter.write(left);
      rest = rest.substring(nlidx + 1);
      nlidx = rest.indexOf('\n');
      firstLine = false;
    }
    if (rest.length() > 0) {
      logWriter.write('\t');
      logWriter.write(rest);
      logWriter.write('\n');
    }
  }

  private void writeLongAttribute(LogWriteEntry lwe, LogAttribute la) throws IOException
  {
    logWriter.write("@");
    logWriter.write(la.getTypeName());
    logWriter.write(": ");
    writeLongValue(la.getValueToWrite(lwe));
    //    logWriter.write("\n");

  }

  private void writeCurrentPosition(LogWriteEntry lwe) throws IOException
  {
    indexHeader.writLogEntryPosition(lwe, logOut.getPosition(), idxOut);
    indexDirectory.updateDate(indexHeader.indexDirectoryIdx, lwe.getTimestamp());

  }

  @Override
  public void close() throws IOException
  {
    if (logWriter != null) {
      logWriter.flush();
    }
    IOUtils.closeQuietly(logOut);
    IOUtils.closeQuietly(idxOut);
  }

}
