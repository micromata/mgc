package de.micromata.mgc.javafx.logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.GenomeLogCategory;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogLevel;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.loghtmlwindow.LogHtmlWindowServlet;
import de.micromata.genome.logging.spi.log4j.RoundList;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.genome.util.validation.ValState;
import de.micromata.mgc.javafx.ControllerService;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 * Logging for GLog.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LoggingController implements Initializable
{
  private static LoggingController INSTANCE = null;

  @FXML
  private ChoiceBox<String> logLevel;
  @FXML
  private VBox frame;

  @FXML
  private TextField filterText;

  @FXML
  private Button filterButton;

  @FXML
  private Button clearButton;

  @FXML
  private CheckBox autoScroll;

  @FXML
  private WebView htmlView;

  private long idGenerator = 0L;

  /**
   * The queue.
   */
  private RoundList<LogWriteEntry> logWriteEntries = new RoundList<>(1000);
  private List<LogWriteEntry> guiWriteBuffer = new ArrayList<>();
  private LoggingLogViewAdapter loggingAdapter = new LoggingLogViewAdapter();

  public static LoggingController getInstance()
  {
    return INSTANCE;
  }

  public LoggingController()
  {

  }

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {

    clearButton.setOnAction(e -> {
      GLog.warn(GenomeLogCategory.System, "No filter here", new LogExceptionAttribute(new RuntimeException()));
    });
    autoScroll.setSelected(true);
    logLevel.setValue(LogLevel.Note.name());
    List<String> levels = Arrays.asList(LogLevel.values()).stream().map(e -> e.name())
        .collect(Collectors.toList());
    logLevel.setItems(FXCollections.observableArrayList(levels));
    logLevel.valueProperty().addListener((comp, oldValue, newValue) -> {
      //      refilterGui();
    });

    WebEngine engine = htmlView.getEngine();
    engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
      if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
        Document doc = engine.getDocument();
        if (guiWriteBuffer.isEmpty() == false) {
          List<LogWriteEntry> copy = new ArrayList<>(guiWriteBuffer);
          guiWriteBuffer.clear();
          addToGuiInGui(copy);
        }
      }
    });
    engine.setJavaScriptEnabled(true);

    String styleheader = getHtmlHeader();
    StringBuilder html = new StringBuilder();
    String init = "  var logCounter = 0; \r\n" +
        "var loggingAdapter;\n" +
        "function ALogCallback(callback) { \n"
        + " this.callback = callback;\n"
        + " this.doCallback = function(entries) {this.callback(entries); }\n"
        + "};" +
        "function LogConsoleBackend(logViewer) {\r\n" +
        "  this.supportsPoll = false;\r\n" +
        "  this.supportsSearch = false;\r\n" +
        "  this.init = function(logViewer) {\r\n" +
        "  this.logViewer = logViewer;\n" +
        "   loggingAdapter.init(this);\n" +
        "  }\r\n" +
        "  this.logPoll = function(lastTime, callback) {\r\n" +
        "    loggingAdapter.logPoll(lastTime, new ALogCallback(callback));\r\n" +
        "  }\r\n" +
        "  this.logSelect = function(logFormData, callback) {\r\n" +
        "\n" +
        "  }\n" +
        "}" +
        "  function logProvider() {\r\n" +
        "    var item = {\r\n" +
        "      logTime : '2006-01-01 12:12',\r\n" +
        "      logLevel : 'Note',\r\n" +
        "      logMessage : 'Hello ' + ++logCounter,\r\n" +
        "      logAttributes : [ {\r\n" +
        "        typeName : \"AKey\",\r\n" +
        "        value : 'A value'\r\n" +
        "      }, {\r\n" +
        "        typeName : \"BKey\",\r\n" +
        "        value : 'B value'\r\n" +
        "      } ]\r\n" +
        "    };\r\n" +
        "    return [ item ];\r\n" +
        "  }\r\n" +

        "var logViewer = new GLogViewer({\r\n" +
        "    logListId : 'glogentries',\r\n" +
        "    formId : 'glogform',\r\n" +
        "    maxItems: 100,\r\n" +
        "    logPollTimeout: 1000,\r\n" +
        "    enableEmbeddedDebugger: false\n" +
        "\r\n" +
        "  });\n" +
        "window.logViewer = logViewer;\n";
    html.append("<html><head>").append(getHtmlHeader())
        .append("\n<script  type=\"text/javascript\">\n").append(init).append("\n</script>\n")
        .append("</head>").append("<body>\r\n");
    html.append(LogHtmlWindowServlet.getGLogHtmlForm());
    html.append("</body>");

    engine.loadContent(html.toString());
    engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
      try {

        JSObject window = (JSObject) engine.executeScript("window");
        window.setMember("loggingAdapter", loggingAdapter);

        engine.executeScript("console.debug = function(message){ loggingAdapter.jsdebug(message); };\n" +
            "console.warn = function(message){ loggingAdapter.jswarn(message); };\n" +
            "console.error = function(message){ loggingAdapter.jserror(message); }; console.debug('console redirect initialized');\n");
        //        JSObject logViewer = (JSObject) engine.executeScript("logViewer");
        //        logViewer.call("setBackend", loggingAdapter);
        engine.executeScript("logViewer.setBackend(new LogConsoleBackend());");
      } catch (RuntimeException ex) {
        ex.printStackTrace();
      }

    });
    INSTANCE = this;
    FxLogconsoleLogWriteEntryEventListener.registerEvent();
  }

  protected String getHtmlHeader()
  {
    StringBuilder sb = new StringBuilder();
    try (InputStream is = LoggingController.class.getResourceAsStream("/loggingweb.css")) {
      String css = IOUtils.toString(is);
      sb.append("\n<style>\n").append(css).append("\n</style>\n");
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
    sb.append("\n<script type=\"text/javascript\">\n");

    sb.append(LogHtmlWindowServlet.getJsContent());

    sb.append("\n</script>\n");
    return sb.toString();
  }

  public void error(String message, Exception ex)
  {
    doLog(new ValMessage(ValState.Error, message, ex));
  }

  public void error(String message)
  {
    doLog(new ValMessage(ValState.Error, message));
  }

  public void warn(String message)
  {
    doLog(new ValMessage(ValState.Warning, message));

  }

  public void info(String message)
  {
    doLog(new ValMessage(ValState.Info, message));

  }

  public void doLog(ValMessage item)
  {
    LogWriteEntry lwe = new LogWriteEntry();
    lwe.setCategory("Launcher");
    lwe.setAttributes(new ArrayList<>());
    lwe.setLevel(GLog.valStateToLogLevel(item.getValState()));
    String message = item.getTranslatedMessage(MgcLauncher.get().getApplication().getTranslateService());
    lwe.setMessage(message);
    if (item.getException() != null) {
      lwe.getAttributes().add(new LogExceptionAttribute(item.getException()));
    }
    lwe.setTimestamp(System.currentTimeMillis());
    doLogImpl(lwe);
  }

  private String getLogClass(LogWriteEntry lwe)
  {
    switch (lwe.getLevel()) {
      case Fatal:
      case Error:
        return " leerror";
      case Warn:
        return " lewarn";
      default:
        return "";
    }
  }

  public void doLogImpl(LogWriteEntry lwe)
  {
    if (lwe.getTimestamp() == 0) {
      lwe.setTimestamp(System.currentTimeMillis());
    }
    synchronized (logWriteEntries) {
      logWriteEntries.add(lwe);
    }
    if (filterLogEntry(lwe) == true) {
      addToGui(Collections.singletonList(lwe));
    }
  }

  private boolean filterLogEntry(LogWriteEntry lwe)
  {
    String sval = logLevel.getValue();
    if (StringUtils.isNotBlank(sval) == true) {
      LogLevel maxLogLevel = LogLevel.valueOf(sval);
      if (lwe.getLevel().getLevel() < maxLogLevel.getLevel()) {
        return false;
      }
      String sf = filterText.getText();
      if (StringUtils.isNotBlank(sf) == true) {
        if (StringUtils.containsIgnoreCase(lwe.getMessage(), sf) == false) {
          return false;
        }
      }
    }
    return true;
  }

  private void addToGui(List<LogWriteEntry> logentries)
  {

    ControllerService.get().runInToolkitThread(() -> {
      addToGuiInGui(logentries);
    });
  }

  private void addToGuiInGui(List<LogWriteEntry> logentries)
  {
    loggingAdapter.addLogEntries(logentries);
    //    WebEngine engine = htmlView.getEngine();

    //    if (true) {
    //      return;
    //    }
    //    WebEngine engine = htmlView.getEngine();
    //    List<LogWriteEntry> lwes = logentries;
    //    Document doc = engine.getDocument();
    //    if (doc == null) {
    //      guiWriteBuffer.addAll(lwes);
    //      return;
    //    } else {
    //      lwes = new ArrayList<>(logentries);
    //      lwes.addAll(guiWriteBuffer);
    //      guiWriteBuffer.clear();
    //    }
    //    Element les = doc.getElementById("logentries");
    //    String lielid = "";
    //    for (LogWriteEntry lwe : lwes) {
    //      lielid = "logentry" + ++idGenerator;
    //
    //      Element liel = createElement(doc, "div", "id", lielid, "class", "loge" + getLogClass(lwe));
    //
    //      ((EventTarget) liel).addEventListener("dblclick", event -> {
    //        toggleLogAttribuesGui(doc, liel, lwe);
    //      }, false);
    //
    //      Element logt = createElement(doc, "div", "class", "logt");
    //      String date = DateUtils.getStandardDateTimeFormat().format(new Date(lwe.getTimestamp()));
    //      logt.appendChild(doc.createTextNode(date));
    //      Element logl = createElement(doc, "div", "class", "logl");
    //      liel.appendChild(logt);
    //      logl.appendChild(doc.createTextNode(lwe.getLevel().name()));
    //      liel.appendChild(logl);
    //      Element logm = createElement(doc, "div", "class", "logm");
    //      logm.appendChild(doc.createTextNode(lwe.getMessage()));
    //      liel.appendChild(logm);
    //      renderAttrs(lwe, doc, liel);
    //
    //      les.appendChild(liel);

    //    }
    //    scrollToBottom(engine, lielid);

  }

  //  private void renderAttrs(LogWriteEntry lwe, Document doc, Element liel)
  //  {
  //    Element logattrs = createElement(doc, "div", "class", "logattrs hidden");
  //    liel.appendChild(logattrs);
  //    if (lwe.getAttributes() == null) {
  //      return;
  //    }
  //    for (LogAttribute la : lwe.getAttributes()) {
  //      Element attr = createElement(doc, "div", "class", "logattr");
  //      Element attrkey = createElement(doc, "div", "class", "logattrkey");
  //      attrkey.appendChild(doc.createTextNode(la.getTypeName()));
  //      attr.appendChild(attrkey);
  //      Element attrvalue = createElement(doc, "div", "class", "logattrvalue");
  //      attrvalue.appendChild(doc.createTextNode(la.getValue()));
  //      attr.appendChild(attrvalue);
  //      logattrs.appendChild(attr);
  //    }
  //  }
  //
  //  private void toggleLogAttribuesGui(Document doc, Element liel, LogWriteEntry lwe)
  //  {
  //    Element lastchild = (Element) liel.getLastChild();
  //    String curClass = lastchild.getAttribute("class");
  //    if (StringUtils.equals(curClass, "logattr hidden")) {
  //      lastchild.setAttribute("class", "logattr");
  //    } else {
  //      lastchild.setAttribute("class", "logattr hidden");
  //    }
  //
  //  }

  //  private void refilterGui()
  //  {
  //    clearGuiLogElements();
  //    List<LogWriteEntry> thoShow = new ArrayList<>();
  //    synchronized (logWriteEntries) {
  //      for (LogWriteEntry lwe : logWriteEntries) {
  //        if (filterLogEntry(lwe) == true) {
  //          thoShow.add(lwe);
  //        }
  //      }
  //    }
  //
  //    addToGui(thoShow);
  //  }
  //
  //  private void clearGuiLogElements()
  //  {
  //    WebEngine engine = htmlView.getEngine();
  //    Document doc = engine.getDocument();
  //    Element les = doc.getElementById("logentries");
  //    Element newle = createElement(doc, "div", "id", "logentries", "class", "logentries");
  //    Node parent = les.getParentNode();
  //    parent.removeChild(les);
  //    parent.appendChild(newle);
  //  }
  //
  //  private void scrollToBottom(WebEngine engine, String lielid)
  //  {
  //    if (autoScroll.isSelected() == true) {
  //      engine.executeScript("document.getElementById(\"" + lielid + "\").scrollIntoView(true)");
  //    }
  //  }
  //
  //  private Element createElement(Document doc, String elName, String... attrs)
  //  {
  //    Element ret = doc.createElement(elName);
  //    for (int i = 0; i < attrs.length; ++i) {
  //      String attrk = attrs[i];
  //      String value = "";
  //      if (attrs.length > i) {
  //        value = attrs[i + 1];
  //      }
  //      ret.setAttribute(attrk, value);
  //      ++i;
  //    }
  //    return ret;
  //  }

  public void adjustWidth(double with)
  {
    double oldWidth = frame.getWidth();

    frame.setPrefWidth(with - 5);
  }

  public void adjustHeight(double heigth)
  {
    frame.setPrefHeight(heigth - 5);
  }
}
