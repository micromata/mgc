package de.micromata.genome.logging.loghtmlwindow;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

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

/**
 * Service to work with glogviewer.js.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class LogHtmlWindowServlet extends HttpServlet
{
  private static final Logger LOG = Logger.getLogger(LogHtmlWindowServlet.class);
  private RoundList<LogWriteEntry> logWriteEntries = new RoundList<>(2000);
  static LogHtmlWindowServlet INSTANCE;

  /**
   * Will be called by doPost.
   * 
   * If a valid user to show logs, call the execute method.
   * 
   * @param req
   * @param resp
   * @throws ServletException
   * @throws IOException
   */
  protected abstract void executeWithAuthentifcation(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException;

  @Override
  public void init()
  {
    INSTANCE = this;
    LoggingServiceManager.get().getLoggingEventListenerRegistryService().registerListener(LogHtmlLiveBuffer.class);
  }

  public void addLogEntry(LogWriteEntry le)
  {
    synchronized (logWriteEntries) {
      logWriteEntries.add(le);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    executeWithAuthentifcation(req, resp);
  }

  protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    String cmd = req.getParameter("cmd");
    if ("poll".equals(cmd) == true) {
      poll(req, resp);
    } else if ("getConfiguration".equals(cmd) == true) {
      getConfiguration(req, resp);

    } else if ("search".equals(cmd) == true) {
      filter(req, resp);
    } else {
      error(req, resp, "Unknown command");
    }

  }

  protected void getConfiguration(HttpServletRequest req, HttpServletResponse resp) throws IOException
  {
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

  List<LogWriteEntry> getLogEntries(long lastPollTime)
  {
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

  protected void poll(HttpServletRequest req, HttpServletResponse resp) throws IOException
  {
    String lastLog = req.getParameter("lt");
    long lastPollTime = NumberUtils.toLong(lastLog, 0L);
    List<LogWriteEntry> buffer = getLogEntries(lastPollTime);
    JsonArray ret = new JsonArray();
    for (LogWriteEntry lwe : buffer) {
      ret.add(LogJsonUtils.logEntryToJson(lwe));
    }
    sendResponse(resp, ret);
  }

  protected void filter(HttpServletRequest req, HttpServletResponse resp) throws IOException
  {
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
    List<OrderBy> orderBy = null;
    String startRows = req.getParameter("startRow");
    String maxRows = req.getParameter("maxRow");
    int startRow = NumberUtils.toInt(startRows, 0);
    int maxRow = NumberUtils.toInt(maxRows, 30);
    boolean masterOnly = "true".equals(req.getParameter("masterOnly"));
    JsonArray ret = new JsonArray();
    LoggingServiceManager.get().getLogging().selectLogs(start, end, level, logCategory, logMessage, logAttributes,
        startRow,
        maxRow, orderBy, masterOnly, (logEntry) -> {
          ret.add(LogJsonUtils.logEntryToJson(logEntry));
        });
    sendResponse(resp, ret);
  }

  private void sendResponse(HttpServletResponse resp, JsonValue val) throws IOException
  {
    resp.setContentType("application/json");
    String sr = val.toString();
    ServletOutputStream os = resp.getOutputStream();
    os.write(sr.getBytes("UTF-8"));
    os.flush();
  }

  private void error(HttpServletRequest req, HttpServletResponse resp, String string)
  {
    LOG.warn(string);

  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    doPost(req, resp);
  }

  public static String getJsContent()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(getClassResource("/glogviewer.js"));
    sb.append(getClassResource("/glogform.js"));
    sb.append(getClassResource("/glogbackend.js"));
    return sb.toString();
  }

  public static String getCssContent()
  {
    return getClassResource("/loggingweb.css");
  }

  public static String getClassResource(String name)
  {
    try (InputStream is = LogHtmlWindowServlet.class.getResourceAsStream(name)) {
      return IOUtils.toString(is);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
  }

  public static String getGLogHtmlForm()
  {
    return getClassResource("/glogviewerform.html");
  }
}
