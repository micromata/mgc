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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.types.Pair;

/**
 * Format Type(long)Version(long)AbsStartIdx(long)|Name1[50]=(long)
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IndexHeader
{
  public static final int FILE_TYPE_LENGTH = 8;
  public static final int FILE_VERSION_LENGTH = 1;

  public static final byte[] INDEX_FILE_TYPE = "GCLGFXIT".getBytes();
  public static final byte[] INDEX_FILE_VERSION = "1".getBytes();
  public static final int HEADER_NAME_LENGTH = 32;
  /**
   * time + offset
   */
  public static final int ROW_LENGTH = Long.BYTES + Integer.BYTES;

  byte[] fileType;
  byte[] fileVersion;
  long created;
  int indexDirectoryIdx;
  int startIndex;
  int headerLength;
  /**
   * name -> size
   */
  List<Pair<String, Integer>> headerOrder = new ArrayList<>();
  Map<String, Integer> searchFieldsLength = new HashMap<>();
  Map<String, Integer> searchFieldsOffsets = new HashMap<>();

  public static IndexHeader getIndexHeader(Collection<LogAttributeType> attributes)
  {
    IndexHeader ret = new IndexHeader();
    int curIdx = 0;

    for (StdSearchFields sf : StdSearchFields.values()) {
      //      String headerName = getNormalizedHeaderName(sf.name());
      int hoffs = curIdx;
      ret.searchFieldsOffsets.put(sf.name(), hoffs);
      ret.searchFieldsLength.put(sf.name(), sf.getSize());
      ret.headerOrder.add(Pair.make(sf.name(), sf.getSize()));
      curIdx += sf.getSize() + 1;
    }
    for (LogAttributeType lat : attributes) {
      if (lat.isSearchKey() == false) {
        continue;
      }
      int maxs = lat.maxValueSize();
      String headerName = getNormalizedHeaderName(lat.name());
      int hoffs = curIdx;
      ret.searchFieldsOffsets.put(headerName, hoffs);
      ret.searchFieldsLength.put(headerName, maxs);
      ret.headerOrder.add(Pair.make(headerName, maxs));
      curIdx += maxs + 1;
    }
    return ret;
  }

  public static String getNormalizedHeaderName(String name)
  {
    return StringUtils.substring(name, 0, HEADER_NAME_LENGTH).toUpperCase();
  }

  public void writeFileHeader(OutputStream os, File indexFile, IndexDirectory indexDirectory) throws IOException
  {
    indexDirectoryIdx = indexDirectory.createNewLogIdxFile(indexFile);
    ByteBuffer lbb = ByteBuffer.wrap(new byte[Long.BYTES]);
    ByteBuffer ibb = ByteBuffer.wrap(new byte[Integer.BYTES]);
    os.write(INDEX_FILE_TYPE);
    os.write(INDEX_FILE_VERSION);
    lbb.putLong(0, System.currentTimeMillis());
    os.write(lbb.array());
    ibb.putInt(0, indexDirectoryIdx);
    os.write(ibb.array());
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    for (Pair<String, Integer> headerp : headerOrder) {
      String hn = StringUtils.rightPad(headerp.getFirst(), HEADER_NAME_LENGTH);
      bout.write(hn.getBytes());
      ibb.putInt(0, headerp.getSecond());
      bout.write(ibb.array());
    }
    byte[] headerar = bout.toByteArray();
    int idxOffset = FILE_TYPE_LENGTH + FILE_VERSION_LENGTH + Long.BYTES /* timestamp */
        + Integer.BYTES /** indexDirectory */
        + Integer.BYTES /* indexOfset */
        + headerar.length;
    ibb.putInt(0, idxOffset);
    os.write(ibb.array());
    os.write(headerar);
    os.flush();
  }

  public static IndexHeader openIndexHeader(MappedByteBuffer mem, long fileSize)
  {
    IndexHeader ret = new IndexHeader();
    ret.parse(mem, fileSize);
    return ret;
  }

  private void parse(MappedByteBuffer mem, long fileSize)
  {
    fileType = new byte[INDEX_FILE_TYPE.length];
    fileVersion = new byte[INDEX_FILE_VERSION.length];
    //    mem.position(0);
    int pos = 0;
    NIOUtils.getBytes(mem, pos, fileType);
    pos += fileType.length;
    NIOUtils.getBytes(mem, pos, fileVersion);
    pos += fileVersion.length;
    created = mem.getLong(pos);
    pos += Long.BYTES;
    indexDirectoryIdx = mem.getInt(pos);
    pos += Integer.BYTES;
    startIndex = mem.getInt(pos);
    pos += Integer.BYTES;

    int max = startIndex - (Integer.BYTES + HEADER_NAME_LENGTH);
    int offset = 0;
    while (pos < max) {
      String headerName = NIOUtils.readAsciiString(mem, pos, HEADER_NAME_LENGTH);
      pos += HEADER_NAME_LENGTH;
      int fieldLength = mem.getInt(pos);
      pos += Integer.BYTES;
      headerName = StringUtils.trim(headerName);
      headerOrder.add(Pair.make(headerName, fieldLength));
      searchFieldsLength.put(headerName, fieldLength);
      searchFieldsOffsets.put(headerName, offset);
      offset += fieldLength + 1;
    }
  }

  /**
   * 
   * @param start
   * @param end
   * @param mem
   * @param filesize
   * @return startoffset, endidx
   */
  public List<Pair<Integer, Integer>> getCandiates(Timestamp start, Timestamp end, MappedByteBuffer mem, int filesize)
  {
    List<Pair<Integer, Integer>> ret = new ArrayList<>();

    int pos = filesize - ROW_LENGTH;
    while (pos >= 0) {
      long logt = mem.getLong(pos);
      if (start != null && start.getTime() > logt) {
        continue;
      }
      if (end != null && end.getTime() < logt) {
        break;
      }
      int offset = mem.getInt(pos + Long.BYTES);
      int endOfset = -1;
      if (pos + ROW_LENGTH + Integer.BYTES < filesize) {
        endOfset = mem.getInt(pos + Long.BYTES + ROW_LENGTH);
      }
      if (offset == endOfset) {
        // oops
        System.out.println("Oops");
      }
      ret.add(Pair.make(offset, endOfset));
      pos -= ROW_LENGTH;
    }
    return ret;
  }

  public String readSearchFromLog(Integer offset, RandomAccessFile file, String name) throws IOException
  {
    Integer fieldoffset = searchFieldsOffsets.get(name);
    if (fieldoffset == null) {
      return null;
    }
    file.seek(offset.intValue() + fieldoffset.intValue());
    byte[] fieldBuffer = new byte[searchFieldsLength.get(name)];
    file.readFully(fieldBuffer);
    String ret = new String(fieldBuffer);
    ret = ret.trim();
    return ret;

  }

  public void writLogEntryPosition(LogWriteEntry lwe, int position, OutputStream idxOut) throws IOException
  {
    ByteBuffer ibb = ByteBuffer.wrap(new byte[Integer.BYTES]);
    ByteBuffer lbb = ByteBuffer.wrap(new byte[Long.BYTES]);
    lbb.putLong(lwe.getTimestamp());
    idxOut.write(lbb.array());
    ibb.putInt(position);
    idxOut.write(ibb.array());
    idxOut.flush();
  }
}
