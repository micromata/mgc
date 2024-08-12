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

package de.micromata.genome.logging.loghtmlwindow;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.micromata.genome.logging.LogAttributeType;
import de.micromata.genome.logging.LogCategory;
import de.micromata.genome.logging.LogConfigurationDAO;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.Logging.OrderBy;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.logging.spi.log4j.RoundList;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.types.Pair;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

/**
 * Service to work with glogviewer.js.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public abstract class LogHtmlWindowServlet extends HttpServlet {

  private static final Logger LOG = Logger.getLogger(LogHtmlWindowServlet.class);
  private RoundList<LogWriteEntry> logWriteEntries = new RoundList<>(2000);
  static LogHtmlWindowServlet INSTANCE;

  /**
   * Will be called by doPost.
   *
   * If a valid user to show logs, call the execute method.
   *
   * @param req the servlet request
   * @param resp the servlet response
   * @throws ServletException exception thrown when an error happened while executing this method
   * @throws IOException exception thrown when an error happened while executing this method
   */
  protected abstract void executeWithAuthentifcation(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException;

  @Override
  public void init() {
    INSTANCE = this;
    LoggingServiceManager.get().getLoggingEventListenerRegistryService().registerListener(LogHtmlLiveBuffer.class);
  }

  public void addLogEntry(LogWriteEntry le) {
    synchronized (logWriteEntries) {
      logWriteEntries.add(le);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    executeWithAuthentifcation(req, resp);
  }

  protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String cmd = req.getParameter("cmd");
    if ("poll".equals(cmd) == true) {
      poll(req, resp);
    } else if ("getConfiguration".equals(cmd) == true) {
      getConfiguration(req, resp);

    } else if ("search".equals(cmd) == true) {
      filter(req, resp);
    } else if ("logSelectAttributes".equals(cmd) == true) {
      logSelectAttributes(req, resp);
    } else {
      error(req, resp, "Unknown command");
    }

  }

  private void logSelectAttributes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Logging logging = LoggingServiceManager.get().getLogging();
    String id = req.getParameter("id");
    Object logid = logging.parseLogId(id);
    List<Object> ids = Arrays.asList(logid);
    JsonArray arr = new JsonArray();
    logging.selectLogs(ids, false, row -> {
      arr.add(LogJsonUtils.logEntryToJson(logging, row, true));
    });
    sendResponse(resp, arr);
  }

  protected void getConfiguration(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    JsonObject ret = new JsonObject();
    Logging logging = LoggingServiceManager.get().getLogging();
    ret.add("supportsSearch", logging.supportsSearch());
    ret.add("supportsFulltextSearch", logging.supportsFulltextSearch());
    JsonArray arr = new JsonArray();

    for (LogCategory lc : logging.getRegisteredCategories()) {
      arr.add(lc.getFqName());
    }
    ret.add("loggingCategories", arr);
    arr = new JsonArray();
    for (LogAttributeType at : logging.getRegisteredAttributes()) {
      arr.add(at.name());
    }
    ret.add("attributes", arr);
    arr = new JsonArray();
    for (LogAttributeType at : logging.getSearchAttributes()) {
      arr.add(at.name());
    }
    ret.add("searchAttributes", arr);

    LogConfigurationDAO logconfig = LoggingServiceManager.get().getLogConfigurationDAO();
    LogLevel th = logconfig.getThreshold();
    ret.add("threshold", th.getName());

    sendResponse(resp, ret);
  }

  List<LogWriteEntry> getLogEntries(long lastPollTime) {
    synchronized (logWriteEntries) {
      List<LogWriteEntry> ret = new ArrayList<>();
      for (LogWriteEntry le : logWriteEntries) {
        if (le.getTimestamp() <= lastPollTime) {
          continue;
        }
        ret.add(le);
      }
      return ret;
    }
  }

  protected void poll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String lastLog = req.getParameter("lt");
    long lastPollTime = NumberUtils.toLong(lastLog, 0L);
    List<LogWriteEntry> buffer = getLogEntries(lastPollTime);
    JsonArray ret = new JsonArray();
    for (LogWriteEntry lwe : buffer) {
      ret.add(LogJsonUtils.logEntryToJson(lwe));
    }
    sendResponse(resp, ret);
  }

  protected void filter(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String logMessage = req.getParameter("logMessage");
    Integer level = null;
    String logLevel = req.getParameter("logLevel");
    if (StringUtils.isNotBlank(logLevel) == true) {
      level = LogLevel.fromString(logLevel, LogLevel.Note).getLevel();
    }
    String logCategory = req.getParameter("logCategory");

    Timestamp start = null;
    Timestamp end = null;
    List<Pair<String, String>> logAttributes = null;
    String logAttribute1Type = req.getParameter("logAttribute1Type");
    String logAttribute1Value = req.getParameter("logAttribute1Value");
    String logAttribute2Type = req.getParameter("logAttribute2Type");
    String logAttribute2Value = req.getParameter("logAttribute2Value");
    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);

    String fromDate = req.getParameter("fromDate");
    String toDate = req.getParameter("toDate");
    if (StringUtils.length(fromDate) == "yyyy-MM-ddTHH:mm:ss.SSSZ".length()) {
      try {
        start = new Timestamp(sd.parse(fromDate).getTime());
      } catch (ParseException ex) {
        LOG.warn("Cannot parse Logging fromDate: " + fromDate + ": " + ex.getMessage());
      }
    }
    if (StringUtils.length(toDate) == "yyyy-MM-ddTHH:mm:ss.SSSZ".length()) {
      try {
        end = new Timestamp(sd.parse(toDate).getTime());
      } catch (ParseException ex) {
        LOG.warn("Cannot parse Logging fromDate: " + toDate + ": " + ex.getMessage());
      }
    }
    if (StringUtils.isNotBlank(logAttribute1Type) && StringUtils.isNotBlank(logAttribute1Value)) {
      logAttributes = new ArrayList<>();
      logAttributes.add(Pair.make(logAttribute1Type, logAttribute1Value));
    }
    if (StringUtils.isNotBlank(logAttribute2Type) && StringUtils.isNotBlank(logAttribute2Value)) {
      if (logAttributes == null) {
        logAttributes = new ArrayList<>();
      }
      logAttributes.add(Pair.make(logAttribute2Type, logAttribute2Value));
    }
    List<OrderBy> orderBy = new ArrayList<>();
    String orderCol = req.getParameter("orderBy");
    if (StringUtils.isNotBlank(orderCol) == true) {
      Boolean desc = Boolean.valueOf(req.getParameter("desc"));
      orderBy.add(new OrderBy(orderCol, desc));
    } else {
      orderBy.add(new OrderBy("modifiedAt", true));
    }
    String startRows = req.getParameter("startRow");
    String maxRows = req.getParameter("maxRow");
    int startRow = NumberUtils.toInt(startRows, 0);
    int maxRow = NumberUtils.toInt(maxRows, 30);
    boolean allAttrs = "true".equals(req.getParameter("allAttrs"));
    JsonArray ret = new JsonArray();
    Logging logging = LoggingServiceManager.get().getLogging();
    logging.selectLogs(start, end, level, logCategory, logMessage, logAttributes,
        startRow,
        maxRow, orderBy, allAttrs == false, (logEntry) -> {
          ret.add(LogJsonUtils.logEntryToJson(logging, logEntry, allAttrs));
        });
    sendResponse(resp, ret);
  }

  private void sendResponse(HttpServletResponse resp, JsonValue val) throws IOException {
    resp.setContentType("application/json");
    String sr = val.toString();
    ServletOutputStream os = resp.getOutputStream();
    os.write(sr.getBytes("UTF-8"));
    os.flush();
  }

  private void error(HttpServletRequest req, HttpServletResponse resp, String string) {
    LOG.warn(string);

  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doPost(req, resp);
  }

  public static String getJsContent() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClassResource("/glogviewer.js"));
    sb.append(getClassResource("/glogform.js"));
    sb.append(getClassResource("/glogbackend.js"));
    return sb.toString();
  }

  public static String getCssContent() {
    return getClassResource("/loggingweb.css");
  }

  public static String getClassResource(String name) {
    try (InputStream is = LogHtmlWindowServlet.class.getResourceAsStream(name)) {
      return IOUtils.toString(is, Charset.defaultCharset());
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  public static String getGLogHtmlForm() {
    return getClassResource("/glogviewerform.html");
  }
}
