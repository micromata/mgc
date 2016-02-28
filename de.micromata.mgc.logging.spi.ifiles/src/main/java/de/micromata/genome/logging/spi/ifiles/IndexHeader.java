package de.micromata.genome.logging.spi.ifiles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.util.types.DateUtils;
import de.micromata.genome.util.types.Pair;

/**
 * Format Type(long)Version(long)AbsStartIdx(long)|Name1[50]=(long)
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class IndexHeader
{
  public static final long FILE_TYPE_LENGTH = 8;
  public static final long FILE_VERSION_LENGTH = 1;

  public static final byte[] INDEX_FILE_TYPE = "GCLGFXIT".getBytes();
  public static final byte[] INDEX_FILE_VERSION = "1".getBytes();
  public static final int HEADER_NAME_LENGTH = 32;

  public static enum StdSearchFields
  {
    Time(23, lwe -> DateUtils.getStandardDateTimeFormat().format(new Date(lwe.getTimestamp()))),

    Level(10, lwe -> lwe.getLevel().name()),

    Category(30, lwe -> lwe.getCategory()),

    ShortMessage(32, lwe -> lwe.getMessage())

    ;
    private final int size;
    private final Function<LogWriteEntry, String> fieldExtractor;

    private StdSearchFields(int size, Function<LogWriteEntry, String> fieldExtractor)
    {
      this.size = size;
      this.fieldExtractor = fieldExtractor;
    }

    public int getSize()
    {
      return size;
    }

    public Function<LogWriteEntry, String> getFieldExtractor()
    {
      return fieldExtractor;
    }

  }

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
      String headerName = getNormalizedHeaderName(sf.name());
      ret.searchFieldsOffsets.put(sf.name(), curIdx);
      ret.searchFieldsLength.put(sf.name(), sf.getSize());
      curIdx += sf.getSize();
      ret.headerOrder.add(Pair.make(sf.name(), sf.getSize()));
    }
    for (LogAttributeType lat : attributes) {
      if (lat.isSearchKey() == false) {
        continue;
      }
      int maxs = lat.maxValueSize();
      String headerName = getNormalizedHeaderName(lat.name());
      ret.searchFieldsOffsets.put(headerName, curIdx);
      ret.searchFieldsLength.put(headerName, maxs);
      curIdx += maxs;
      ret.headerOrder.add(Pair.make(headerName, maxs));
    }
    return ret;
  }

  public static String getNormalizedHeaderName(String name)
  {
    return StringUtils.substring(name, 0, HEADER_NAME_LENGTH).toUpperCase();
  }

  public void writeFileHeader(OutputStream os) throws IOException
  {
    ByteBuffer bb = ByteBuffer.wrap(new byte[Long.BYTES]);
    os.write(INDEX_FILE_TYPE);
    os.write(INDEX_FILE_VERSION);
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    for (Pair<String, Integer> headerp : headerOrder) {
      String hn = StringUtils.rightPad(headerp.getFirst(), HEADER_NAME_LENGTH);
      bout.write(hn.getBytes());
      bb.putLong(0, headerp.getSecond());
      bout.write(bb.array());
    }
    byte[] headerar = bout.toByteArray();
    long idxOffset = FILE_TYPE_LENGTH + FILE_VERSION_LENGTH + headerar.length;
    bb.putLong(0, idxOffset);
    os.write(bb.array());
    os.write(headerar);
    os.flush();
  }
}
