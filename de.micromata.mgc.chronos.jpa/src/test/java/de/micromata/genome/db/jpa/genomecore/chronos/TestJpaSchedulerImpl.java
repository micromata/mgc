package de.micromata.genome.db.jpa.genomecore.chronos;

import de.micromata.genome.chronos.manager.SchedulerManager;

public class TestJpaSchedulerImpl extends JpaSchedulerImpl
{
  private static SchedulerManager manager = null;

  @Override
  public SchedulerManager getSchedulerManager()
  {
    if (manager != null) {
      return manager;

    }
    manager = new SchedulerManager();
    manager.init();
    return manager;

  }
}
