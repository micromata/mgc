package de.micromata.mgc.javafx.launcher.gui.lf5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.log4j.lf5.viewer.LogBrokerMonitor;

import de.micromata.mgc.javafx.ControllerService;
import javafx.embed.swing.SwingNode;

public class MgcLogBrokerMonitor extends LogBrokerMonitor
{
  SwingNode swingNode;

  public MgcLogBrokerMonitor(SwingNode swingNode, List logLevels)
  {
    super(logLevels);
    this.swingNode = swingNode;
  }

  @Override
  public void setFrameSize(int width, int height)
  {
    System.out.println("MgcLogBrokerMonitor.setFrameSize called");
  }

  @Override
  protected JMenuBar createMenuBar()
  {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(createFileMenu());
    menuBar.add(createEditMenu());
    menuBar.add(createLogLevelMenu());
    menuBar.add(createViewMenu());
    menuBar.add(createConfigureMenu());
    //    menuBar.add(createHelpMenu());

    return (menuBar);
  }

  /**
   * jfx
   * 
   * @param with
   */
  public void setWidth(double width)
  {
    //    swingNode.resize(width, swingNode.getLayoutY());
    //    JFrame frame = getMainFrame();
    //    int nwith = (int) width;
    //    int nheight = frame.getHeight();
    //    getMainFrame().setSize(nwith, nheight);
  }

  /**
   * jfc
   * 
   * @param heigth
   */
  public void setHeight(double heigth)
  {

  }

  @Override
  protected JMenu createFileMenu()
  {
    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic('f');

    fileMenu.add(createCloseMI());
    return fileMenu;
  }

  @Override
  protected JMenuItem createCloseMI()
  {
    JMenuItem result = new JMenuItem("Close");
    result.setMnemonic('c');
    result.setAccelerator(KeyStroke.getKeyStroke("control Q"));
    result.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        ControllerService.get().runInToolkitThread(() -> {
          Lf5MainWindowController ctl = Lf5MainWindowController.getController();
          if (ctl != null) {
            ctl.hide();
          }
        });
      }
    });
    return result;
  }

  public JFrame getMainFrame()
  {
    return _logMonitorFrame;
  }

}