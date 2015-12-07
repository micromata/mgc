// ///////////////////////////////////////////////////////////////////////////
//
// Project DHL-ParcelOnlinePostage
//
// Author roger@micromata.de
// Created 03.07.2006
// Copyright Micromata 03.07.2006
//
// ///////////////////////////////////////////////////////////////////////////
package de.micromata.genome.logging;

/**
 * The Enum LogLevel.
 *
 * @author roger@micromata.de
 */
public enum LogLevel
{

  /**
   * nur fuer Entwickler.
   */
  Debug(0), //
  /**
   * Grundlegendes Logging der Workflows und Performance-Messungen.
   */
  Trace(1), //
  /**
   * Informationen etwas hoeher als Trace.
   */
  Info(2), //
  /**
   * Informationen zur Nachvollziehbarkeit.
   */
  Note(3), //
  /**
   * Warnung. Fehler, die auch auch aus Misuse entstehen koennen
   */
  Warn(4), //
  /**
   * Fehler. I.d.R. Die Anwendung kann jedoch grundsaetzlich weiterarbeiten.
   */
  Error(5), //
  /**
   * Fataler Fehler. Die Anwendung arbeitet nicht (mehr) korrekt
   */
  Fatal(6)
  /**
   * The level.
   */
  //
  ;
  int level;

  /**
   * Instantiates a new log level.
   *
   * @param ll the ll
   */
  private LogLevel(int ll)
  {
    level = ll;
  }

  public int getLevel()
  {
    return level;
  }

  /**
   * Gets the level from.
   *
   * @param l the l
   * @return the level from
   */
  public static LogLevel getLevelFrom(int l)
  {
    switch (l) {
      case 0:
        return Debug;
      case 1:
        return Trace;
      case 2:
        return Info;
      case 3:
        return Note;
      case 4:
        return Warn;
      case 5:
        return Error;
      case 6:
        return Fatal;
      default:
        return Fatal;
    }
  }

  public boolean isWarnOrWorse()
  {
    return level >= Warn.level;
  }

  public boolean isErrorOrWorse()
  {
    return level >= Error.level;
  }

  public boolean isNoteOrWorse()
  {
    return level >= Note.level;
  }

  public String getName()
  {
    return name();
  }
}
