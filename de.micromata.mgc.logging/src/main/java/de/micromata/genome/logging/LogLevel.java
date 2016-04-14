//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

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

  public static LogLevel fromString(String str, LogLevel defaultValue)
  {
    for (LogLevel ll : values()) {
      if (ll.name().equals(str) == true) {
        return ll;
      }
    }
    return defaultValue;
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
