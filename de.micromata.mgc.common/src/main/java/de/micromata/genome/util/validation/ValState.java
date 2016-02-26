package de.micromata.genome.util.validation;

/**
 * A validation state.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public enum ValState
{
  /**
   * It is not validated.
   */
  Unvalidated(0), //
  /**
   * it is ok.
   */
  Ok(1), //
  /**
   * With information to user.
   */
  Info(2),
  /**
   * An important Note to user. May has to confirm it.
   */
  Note(3), //
  /**
   * has warnings.
   */
  Warning(4), //
  /**
   * has errors.
   */
  Error(5), //
  /**
   * As an error, so the underlying entity cannot be persisted.
   */
  BlockingError(6) //
  /**
   * The level.
   */
  ;
  /**
   * level of validation.
   */
  private int level;

  /**
   * Instantiates a new usm validation state.
   *
   * @param level the level
   */
  private ValState(int level)
  {
    this.level = level;
  }

  /**
   * Example in Case of warn and error returns error.
   *
   * @param other the other
   * @return the more serios state
   */
  public ValState combine(ValState other)
  {
    if (other.level > level) {
      return other;
    }
    return this;
  }

  /**
   * Continue validation.
   *
   * @return true, if successful
   */
  public boolean continueValidation()
  {
    return level < Error.level;
  }

  /**
   * Checks for error or worse.
   *
   * @return true, if successful
   */
  public boolean isErrorOrWorse()
  {
    return level >= Error.level;
  }

  /**
   * Checks if is blocking or worse.
   *
   * @return true, if is blocking or worse
   */
  public boolean isBlockingOrWorse()
  {
    return level >= BlockingError.level;
  }

  /**
   * Checks for warn or worse.
   *
   * @return true, if successful
   */
  public boolean isWarningOrWorse()
  {
    return level >= Warning.level;
  }

  /**
   * Checks if this level is equal or worse.
   *
   * @param other the other
   * @return true, if is equal or worse
   */
  public boolean isEqualOrWorse(ValState other)
  {
    return level >= other.getLevel();
  }

  public boolean isWorse(ValState other)
  {
    return level > other.getLevel();
  }

  /**
   * Checks if this level is equal or better.
   *
   * @param other the other
   * @return true, if is equal or better
   */
  public boolean isEqualOrBetter(ValState other)
  {
    return level <= other.getLevel();
  }

  public int getLevel()
  {
    return level;
  }
}
