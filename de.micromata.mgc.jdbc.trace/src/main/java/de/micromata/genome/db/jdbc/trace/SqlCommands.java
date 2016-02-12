package de.micromata.genome.db.jdbc.trace;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import de.micromata.genome.util.types.Pair;

/**
 * The Class SqlCommands.
 *
 * @author roger
 */
public class SqlCommands
{

  /**
   * The uncommited commands.
   */
  private List<Pair<Savepoint, List<TraceSqlArguments>>> uncommitedCommands = new ArrayList<Pair<Savepoint, List<TraceSqlArguments>>>();

  /**
   * Adds the trace.
   *
   * @param sqlArgs the sql args
   */
  public void addTrace(TraceSqlArguments sqlArgs)
  {
    if (uncommitedCommands.isEmpty() == true) {
      pushSavePoint(null);
    }
    uncommitedCommands.get(uncommitedCommands.size() - 1).getSecond().add(sqlArgs);
  }

  /**
   * Clear savepoint.
   *
   * @param sv the sv
   */
  public void clearSavepoint(Savepoint sv)
  {
    int i = uncommitedCommands.size() - 1;
    for (; i >= 0; --i) {
      Savepoint sp = uncommitedCommands.get(i).getFirst();
      uncommitedCommands.remove(i);
      if (sp == sv) {
        break;
      }
    }

  }

  /**
   * Find savepoint index.
   *
   * @param sv the sv
   * @return the int
   */
  public int findSavepointIndex(Savepoint sv)
  {
    int i = uncommitedCommands.size() - 1;
    for (; i >= 0; --i) {
      if (uncommitedCommands.get(i).getFirst() == sv) {
        return i;
      }
    }
    return i;
  }

  /**
   * Clear.
   */
  public void clear()
  {
    uncommitedCommands.clear();
  }

  /**
   * Push save point.
   *
   * @param sp the sp
   */
  public void pushSavePoint(Savepoint sp)
  {
    uncommitedCommands.add(new Pair<Savepoint, List<TraceSqlArguments>>(sp, new ArrayList<TraceSqlArguments>()));
  }

  public List<Pair<Savepoint, List<TraceSqlArguments>>> getUncommitedCommands()
  {
    return uncommitedCommands;
  }

  public void setUncommitedCommands(List<Pair<Savepoint, List<TraceSqlArguments>>> uncommitedCommands)
  {
    this.uncommitedCommands = uncommitedCommands;
  }

}
