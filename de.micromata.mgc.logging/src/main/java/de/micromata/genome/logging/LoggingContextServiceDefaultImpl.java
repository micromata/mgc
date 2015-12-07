package de.micromata.genome.logging;

import de.micromata.genome.util.runtime.HostUtils;

public class LoggingContextServiceDefaultImpl implements LogginContextService
{

  @Override
  public String getCurrentUserName()
  {
    return "anon";
  }

  @Override
  public String getRunContextId()
  {
    return HostUtils.getRunContext();
  }

}
