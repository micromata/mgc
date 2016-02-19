package de.micromata.genome.util.runtime;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.lang.StringUtils;

import de.micromata.genome.util.bean.PrivateBeanUtils;
import de.micromata.genome.util.runtime.LocalSettings;

/**
 * Utility to dynamically extend the class path.
 * 
 * TODO RK read Local Settings.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class DynamicClassPath
{
  public static void initClassPath(LocalSettings localSettings)
  {
    String cp = localSettings.getProperty("sys.classpathext");
    if (StringUtils.isBlank(cp) == true) {
      return;
    }
    String[] cpl = StringUtils.split(cp, ",");
    URL[] urls = new URL[cpl.length];
    for (int i = 0; i < cpl.length; ++i) {
      URL url = createClUrl(new File(cpl[i]));
      urls[i] = url;
    }
    boolean extendSystemCl = false;
    if (extendSystemCl == false) {
      addContextClassPathes(urls);
    } else {
      for (String c : cpl) {
        URL url = createClUrl(new File(c));
        addSystemClassPath(url);
      }
    }
    // Class cls = Class.forName("org.springbyexample.jdbc.datasource.InitializingBasicDataSource", true, Thread.currentThread()
    // .getContextClassLoader());
  }

  public static void addSystemClassPath(URL url)
  {
    URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    PrivateBeanUtils.invokeMethod(sysloader, "addURL", url);
  }

  public static void addContextClassPathes(URL[] urls)
  {
    URLClassLoader urlClassLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
    Thread.currentThread().setContextClassLoader(urlClassLoader);
  }

  public static URL createClUrl(File f)
  {
    try {
      URL ret;
      if (f.isDirectory() == true) {
        ret = f.toURI().toURL();
      } else {
        // ret = new URL("jar", "", f.getAbsolutePath());
        ret = f.toURI().toURL();
      }
      String s = ret.toExternalForm();
      return ret;
    } catch (MalformedURLException ex) {
      throw new RuntimeException("Cannot extend class oath because of invalid url: " + f.toURI());
    }
  }
}
