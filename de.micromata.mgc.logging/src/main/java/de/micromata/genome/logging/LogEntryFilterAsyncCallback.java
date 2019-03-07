package de.micromata.genome.logging;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This is used when the {@link Logging#underlyingClientIsAsync()} is true.
 * @author Sebastian Hardt (s.hardt@micromata.de)
 */
public class LogEntryFilterAsyncCallback extends LogEntryFilterCallback
{

  /**
   * Future which handles the async call of the underlying logging client.
   */
  private final CompletableFuture<Void> future = new CompletableFuture<>();

  /**
   * Instantiates a new log entry filter callback.
   *
   * @param next         the next
   * @param logConfigDAO the log config dao
   * @param maxRows      the max rows
   */
  public LogEntryFilterAsyncCallback(final LogEntryCallback next, final LogConfigurationDAO logConfigDAO, final int maxRows)
  {
    super(next, logConfigDAO, maxRows);
  }

  /**
   * This must be called when the underlying client has converted all the log entries for the callback.
   */
  public void done()
  {
    future.complete(null);
  }

  /**
   * This is called when the {@link Logging#underlyingClientIsAsync()}  is true
   * @throws ExecutionException when an error happened by the execution
   * @throws InterruptedException  when an error happened while interrupting
   */
  public void doGet() throws ExecutionException, InterruptedException
  {
     future.get();
  }

}
