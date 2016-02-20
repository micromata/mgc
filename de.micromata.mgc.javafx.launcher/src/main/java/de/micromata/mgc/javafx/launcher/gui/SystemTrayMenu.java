/**
 *  Project: VLS
 *  Copyright(c) 2015 by Deutsche Post AG
 *  All rights reserved.
 */
package de.micromata.mgc.javafx.launcher.gui;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import de.micromata.mgc.javafx.launcher.MgcLauncher;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The Class SystemTrayMenu.
 * 
 * @author Roger Kommer (roger.kommer.extern@micromata.de)
 */
public class SystemTrayMenu
{

  /**
   * The Constant LOG.
   */
  private static final Logger LOG = Logger.getLogger(SystemTrayMenu.class);
  /**
   * The window.
   */
  final Stage stage;

  /**
   * The tray icon.
   */
  TrayIcon trayIcon;

  /**
   * Instantiates a new system tray menu.
   * 
   * @param window the window
   */
  public SystemTrayMenu(Stage window)
  {
    this.stage = window;
  }

  /**
   * Priority to message type.
   * 
   * @param priority the priority
   * @return the message type
   */
  private MessageType priorityToMessageType(Priority priority)
  {
    switch (priority.toInt()) {
      case Priority.WARN_INT:
        return MessageType.WARNING;
      case Priority.ERROR_INT:
        return MessageType.ERROR;
      case Priority.INFO_INT:
      default:
        return MessageType.INFO;
    }
  }

  /**
   * Sets the systray message.
   * 
   * @param priority the priority
   * @param message the message
   */
  public void setSystrayMessage(final Priority priority, final String message)
  {
    if (trayIcon == null) {
      return;
    }
    EventQueue.invokeLater(new Runnable()
    {

      @Override
      public void run()
      {
        String titel = translate("traymenu.statusPopupTitel");
        trayIcon.displayMessage(titel, message, priorityToMessageType(priority));
      }

    });
  }

  /**
   * Sets system tray icon.
   * 
   * @param icon the icon
   * @param text the text
   */
  public void setSystemTrayIcon(final SystemtrayIcons icon, final String text)
  {
    if (SystemTray.isSupported() == false) {
      return;
    }
    EventQueue.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        trayIcon.setImage(createImage(icon.getIcon(), text != null ? text : "GNM"));
      }

    });

  }

  private String translate(String key)
  {
    return MgcLauncher.get().getApplication().getTranslateService().translate(key);
  }

  /**
   * Creates the and show gui.
   */
  public void createAndShowGUI()
  {
    //Check the SystemTray support
    if (SystemTray.isSupported() == false) {
      LOG.info("Systemtray Menu not supported on this platform.");
      return;
    }
    final PopupMenu popup = new PopupMenu();
    final SystemTray tray = SystemTray.getSystemTray();

    // Create a popup menu components
    MenuItem aboutItem = new MenuItem(translate("traymenu.menu.about"));

    MenuItem showItem = new MenuItem(translate("traymenu.menu.showWindow"));
    MenuItem exitItem = new MenuItem(translate("traymenu.menu.exit"));

    //Add components to popup menu
    popup.add(aboutItem);

    popup.addSeparator();

    popup.add(showItem);
    popup.add(exitItem);

    trayIcon = new TrayIcon(createImage(SystemtrayIcons.Standard.getIcon(), "DHL VLS Pollingclient"));
    trayIcon.setPopupMenu(popup);

    try {
      tray.add(trayIcon);
    } catch (AWTException e) {
      LOG.error("TrayIcon could not be added: " + e.getMessage());
      return;
    }

    createTrayMenuItemActionListener(aboutItem, showItem, exitItem);
  }

  private void createTrayMenuItemActionListener(MenuItem aboutItem, MenuItem showItem, MenuItem exitItem)
  {
    trayIcon.addActionListener((e) -> {
      Platform.runLater(() -> {
        stage.show();
      });
    });

    aboutItem.addActionListener((e) -> {
      Platform.runLater(() -> {
        LauncherService.get().showAboutDialog();
      });
    });

    showItem.addActionListener((e) -> {
      MgcLauncher.showMainWindow();
    });
    ;

    exitItem.addActionListener(e -> {
      Platform.runLater(() -> {
        LauncherService.get().showConfirmDialog("Exit application", "Exit application",
            () -> LauncherService.get().shutdown());
      });
    });

  }

  /**
   * Creates the image.
   * 
   * @param path the path
   * @param description the description
   * @return the image
   */
  protected static Image createImage(String path, String description)
  {
    URL imageURL = SystemTrayMenu.class.getResource(path);

    if (imageURL == null) {
      return null;
    } else {
      return (new ImageIcon(imageURL, description)).getImage();
    }
  }
}
