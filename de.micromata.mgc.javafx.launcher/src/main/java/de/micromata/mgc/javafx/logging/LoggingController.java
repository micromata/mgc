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

package de.micromata.mgc.javafx.logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import de.micromata.genome.logging.GLog;
import de.micromata.genome.logging.LogExceptionAttribute;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.LoggingServiceManager;
import de.micromata.genome.logging.events.LoggingEventListenerRegistryService;
import de.micromata.genome.logging.loghtmlwindow.LogHtmlWindowServlet;
import de.micromata.genome.util.runtime.RuntimeIOException;
import de.micromata.genome.util.validation.ValMessage;
import de.micromata.genome.util.validation.ValState;
import de.micromata.mgc.javafx.launcher.MgcLauncher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
  private WebView htmlView;

  /**
   * The queue.
   */

  LoggingLogViewAdapter loggingAdapter = new LoggingLogViewAdapter();

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

    WebEngine engine = htmlView.getEngine();
    engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
      if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
        Document doc = engine.getDocument();
        //        if (guiWriteBuffer.isEmpty() == false) {
        //          List<LogWriteEntry> copy = new ArrayList<>(guiWriteBuffer);
        //          guiWriteBuffer.clear();
        //          addToGuiInGui(copy);
        //        }
      }
    });
    engine.setJavaScriptEnabled(true);

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
        "  this.getLoggingConfiguration = function() {\n" +
        "    return loggingAdapter.getLoggingConfiguration();\n" +
        "  };" +
        "  this.logSelect = function(logFormData, callback) {\r\n" +
        "    //console.debug('LogConsoleBackend.logSelect');\n" +
        "    loggingAdapter.logSelect(logFormData, new ALogCallback(callback));\n" +
        "  };\n" +
        "  this.logSelectAttributes = function(logId, callback) {\n" +
        "    loggingAdapter.logSelectAttributes(logId, new ALogCallback(callback));\n" +
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
    LoggingEventListenerRegistryService listenerRegisterService = LoggingServiceManager.get()
        .getLoggingEventListenerRegistryService();

    listenerRegisterService.registerListener(FxLogconsoleLogWriteEntryEventListener.class);
    listenerRegisterService.registerListener(FxLogconsoleLogRegisteredCategoryChangedEventListener.class);
    listenerRegisterService.registerListener(FxLogconsoleLogRegisteredLogAttributesChangedEventListener.class);
  }

  String loadCpResource(String name)
  {
    try (InputStream is = getClass().getResourceAsStream(name)) {
      return IOUtils.toString(is);
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    }
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
    //    sb.append(loadCpResource("/js/jquery-1.12.1.min.js"));
    sb.append(LogHtmlWindowServlet.getJsContent());

    sb.append("\n</script>\n");
    //    String url = getClass().getResource("/js/hello.js").toString();
    //    sb.append("<script type=\"text/JavaScript\" src='" + url + "'></script>\n");

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
    loggingAdapter.addLogEntry(lwe);
  }

  public void adjustWidth(double with)
  {
    double oldWidth = htmlView.getWidth();

    htmlView.setPrefWidth(with - 5);
  }

  public void adjustHeight(double heigth)
  {
    htmlView.setPrefHeight(heigth - 5);
  }

  public void refreshLogConfiguration()
  {
    loggingAdapter.refreshLogConfiguration();
    Platform.runLater(() -> {
      WebEngine engine = htmlView.getEngine();
      engine.executeScript("logViewer.refreshForm();");
    });

  }
}
