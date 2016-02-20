/**
 *  Project: VLS
 *  Copyright(c) 2015 by Deutsche Post AG
 *  All rights reserved.
 */
package de.micromata.mgc.javafx.launcher.gui;

/**
 * Icons to show in Systemtray.
 * 
 * @author Roger Kommer (roger.kommer.extern@micromata.de)
 * 
 */
public enum SystemtrayIcons
{

  /**
   * The Standard.
   */
  Standard("/style/systray_ok.gif"),

  /**
   * The Error.
   */
  Error("/style/systray_error.gif");

  /**
   * The icon.
   */
  private final String icon;

  /**
   * Instantiates a new systemtray icons.
   * 
   * @param icon the icon
   */
  private SystemtrayIcons(String icon)
  {
    this.icon = icon;
  }

  public String getIcon()
  {
    return icon;
  }
}
