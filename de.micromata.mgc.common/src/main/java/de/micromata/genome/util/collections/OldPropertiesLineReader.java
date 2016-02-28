package de.micromata.genome.util.collections;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * The Class LineReader.
 */
/*
 * Read in a "logical line" from an InputStream/Reader, skip all comment and blank lines and filter out those leading
 * whitespace characters ( , and ) from the beginning of a "natural line". Method returns the char length of the
 * "logical line" and stores the line in "lineBuf".
 */
class OldPropertiesLineReader
{

  /**
   * The in byte buf.
   */
  byte[] inByteBuf;

  /**
   * The in char buf.
   */
  char[] inCharBuf;

  /**
   * The line buf.
   */
  char[] lineBuf = new char[1024];

  /**
   * The in limit.
   */
  int inLimit = 0;

  /**
   * The in off.
   */
  int inOff = 0;

  /**
   * The in stream.
   */
  InputStream inStream;

  /**
   * The reader.
   */
  Reader reader;

  /**
   * Instantiates a new line reader.
   *
   * @param inStream the in stream
   * @param orderedProperties TODO
   */
  public OldPropertiesLineReader(InputStream inStream)
  {
    this.inStream = inStream;
    inByteBuf = new byte[8192];
  }

  /**
   * Instantiates a new line reader.
   *
   * @param reader the reader
   * @param orderedProperties TODO
   */
  public OldPropertiesLineReader(Reader reader)
  {
    this.reader = reader;
    inCharBuf = new char[8192];
  }

  /**
   * Read line.
   *
   * @return the int
   * @throws IOException Signals that an I/O exception has occurred.
   */
  int readLine() throws IOException
  {
    int len = 0;
    char c = 0;

    boolean skipWhiteSpace = true;
    boolean isCommentLine = false;
    boolean isNewLine = true;
    boolean appendedLineBegin = false;
    boolean precedingBackslash = false;
    boolean skipLF = false;

    while (true) {
      if (inOff >= inLimit) {
        inLimit = (inStream == null) ? reader.read(inCharBuf)
            : inStream.read(inByteBuf);
        inOff = 0;
        if (inLimit <= 0) {
          if (len == 0 || isCommentLine) {
            return -1;
          }
          return len;
        }
      }
      if (inStream != null) {
        //The line below is equivalent to calling a
        //ISO8859-1 decoder.
        c = (char) (0xff & inByteBuf[inOff++]);
      } else {
        c = inCharBuf[inOff++];
      }
      if (skipLF) {
        skipLF = false;
        if (c == '\n') {
          continue;
        }
      }
      if (skipWhiteSpace) {
        if (c == ' ' || c == '\t' || c == '\f') {
          continue;
        }
        if (!appendedLineBegin && (c == '\r' || c == '\n')) {
          continue;
        }
        skipWhiteSpace = false;
        appendedLineBegin = false;
      }
      if (isNewLine) {
        isNewLine = false;
        if (c == '#' || c == '!') {
          isCommentLine = true;
          continue;
        }
      }

      if (c != '\n' && c != '\r') {
        lineBuf[len++] = c;
        if (len == lineBuf.length) {
          int newLength = lineBuf.length * 2;
          if (newLength < 0) {
            newLength = Integer.MAX_VALUE;
          }
          char[] buf = new char[newLength];
          System.arraycopy(lineBuf, 0, buf, 0, lineBuf.length);
          lineBuf = buf;
        }
        //flip the preceding backslash flag
        if (c == '\\') {
          precedingBackslash = !precedingBackslash;
        } else {
          precedingBackslash = false;
        }
      } else {
        // reached EOL
        if (isCommentLine || len == 0) {
          isCommentLine = false;
          isNewLine = true;
          skipWhiteSpace = true;
          len = 0;
          continue;
        }
        if (inOff >= inLimit) {
          inLimit = (inStream == null)
              ? reader.read(inCharBuf)
              : inStream.read(inByteBuf);
          inOff = 0;
          if (inLimit <= 0) {
            return len;
          }
        }
        if (precedingBackslash) {
          len -= 1;
          //skip the leading whitespace characters in following line
          skipWhiteSpace = true;
          appendedLineBegin = true;
          precedingBackslash = false;
          if (c == '\r') {
            skipLF = true;
          }
        } else {
          return len;
        }
      }
    }
  }
}