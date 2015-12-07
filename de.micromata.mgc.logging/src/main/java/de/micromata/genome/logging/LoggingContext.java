/////////////////////////////////////////////////////////////////////////////
//
// Project   DHL-ParcelOnlinePostage
//
// Author    roger@micromata.de
// Created   08.08.2006
// Copyright Micromata 08.08.2006
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Thread local LoggingContext.
 * 
 * Used to store default LogAttributes, which will be automatically filled later
 * 
 * @author roger@micromata.de
 * 
 */
public class LoggingContext
{

  /**
   * The request url.
   */
  private String requestUrl;

  /**
   * The session id.
   */
  private String sessionId;

  /**
   * The current sql.
   */
  private String currentSql = null;

  /**
   * The current sql args.
   */
  private Object[] currentSqlArgs = null;

  /**
   * The server name.
   */
  private String serverName;

  /**
   * The attributes.
   */
  private Map<LogAttributeType, LogAttribute> attributes = null;

  /**
   * The current context.
   */
  private static ThreadLocal<LoggingContext> currentContext = new ThreadLocal<LoggingContext>();

  public static LoggingContext getContext()
  {
    return currentContext.get();
  }

  /**
   * Clear context.
   */
  public static void clearContext()
  {
    currentContext.set(null);
  }

  /**
   * Creates the new context.
   */
  public static void createNewContext()
  {
    currentContext.set(new LoggingContext());
  }

  /**
   * Sorgt dafuer, dass ein Logging Context erzeugt wird.
   *
   * @return true wenn bereits ein LoggingContext vorhanden war
   */
  public static boolean ensureContext()
  {
    if (currentContext.get() != null) {
      return true;
    }
    currentContext.set(new LoggingContext());
    return false;
  }

  public static LoggingContext getEnsureContext()
  {
    ensureContext();
    LoggingContext ctx = currentContext.get();
    if (ctx.attributes == null) {
      ctx.attributes = new HashMap<LogAttributeType, LogAttribute>();
    }
    return ctx;
  }

  public String getRequestUrl()
  {
    return requestUrl;
  }

  public void setRequestUrl(String requestUrl)
  {
    this.requestUrl = requestUrl;
  }

  public String getSessionId()
  {
    return sessionId;
  }

  public void setSessionId(String sessionId)
  {
    this.sessionId = sessionId;
  }

  /**
   * Sets the current sql.
   *
   * @param sql the sql
   * @param args the args
   */
  public static void setCurrentSql(String sql, Object[] args)
  {
    LoggingContext ctx = getContext();
    if (ctx == null) {
      return;
    }
    ctx.currentSql = sql;
    ctx.currentSqlArgs = args;
  }

  /**
   * Reset current sql.
   */
  public static void resetCurrentSql()
  {
    LoggingContext ctx = getContext();
    if (ctx == null) {
      return;
    }
    ctx.currentSql = null;
    ctx.currentSqlArgs = null;
  }

  /**
   * Inits the from request.
   *
   * @param req the req
   */
  public void initFromRequest(HttpServletRequest req)
  {
    if (req == null) {
      return;
    }
    initFromSession(req.getSession(false));
    setRequestUrl(req.getRequestURI());
  }

  /**
   * Inits the from session.
   *
   * @param session the session
   */
  public void initFromSession(HttpSession session)
  {
    if (session == null) {
      String runContext = LoggingServiceManager.get().getLoggingContextService().getRunContextId();
      setSessionId(runContext);
      return;
    }
    setSessionId(session.getId());
  }

  public Map<LogAttributeType, LogAttribute> getAttributes()
  {
    return attributes;
  }

  /**
   * Fuegt ein Attribute zu dem Logging Context hinzu. Kontext wird ggf. erzeugt.
   * 
   * @param attr LogAttribute
   */
  public static void pushLogAttribute(LogAttribute attr)
  {
    if (attr == null) {
      return;
    }
    LoggingContext ctx = getEnsureContext();
    ctx.attributes.put(attr.getType(), attr);
  }

  /**
   * Convinience for de.micromata.genome.logging.LoggingContext.pushLogAttribute(LogAttribute)
   *
   * @param type the type
   * @param data the data
   */
  public static void pushLogAttribute(LogAttributeType type, String data)
  {
    pushLogAttribute(new LogAttribute(type, data));
  }

  /**
   * Entfernt ein Attribute von dem Logging Context. Falls kein Kontext vorhanden, macht gar nichts
   * 
   * @param attr LogAttribute
   */
  public static void popLogAttribute(LogAttribute attr)
  {
    if (attr == null) {
      return;
    }
    popLogAttribute(attr.getType());
  }

  /**
   * Entfernt ein Attribute von dem Logging Context. Falls kein Kontext vorhanden, macht gar nichts
   *
   * @param attrType the attr type
   */
  public static void popLogAttribute(LogAttributeType attrType)
  {
    if (attrType == null) {
      return;
    }
    LoggingContext ctx = currentContext.get();
    if (ctx == null) {
      return;
    }
    if (ctx.attributes == null) {
      return;
    }
    ctx.attributes.remove(attrType);
  }

  public String getCurrentSql()
  {
    return currentSql;
  }

  public Object[] getCurrentSqlArgs()
  {
    return currentSqlArgs;
  }

  public String getServerName()
  {
    return serverName;
  }

  public void setServerName(String serverName)
  {
    this.serverName = serverName;
  }
}
