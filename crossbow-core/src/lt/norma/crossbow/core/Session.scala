/*
 * Copyright 2010-2011 Vilius Normantas <code@norma.lt>
 *
 * This file is part of Crossbow library.
 *
 * Crossbow is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Crossbow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Crossbow.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

package lt.norma.crossbow.core

import org.joda.time.LocalTime

/** Represents trading session. Use `LocalTime.MIDNIGHT` for start or end times to represent open
  * ended sessions.*/
case class Session(name: String, startTime: LocalTime, startInclusive: Boolean,
    endTime: LocalTime, endInclusive: Boolean) {
  def isOpenAt(time: LocalTime) = {
    import Session.{ after, before }
    if (endTime == LocalTime.MIDNIGHT) {
      after(time, startTime, startInclusive) || endInclusive && time.equals(LocalTime.MIDNIGHT)
    } else if (endTime.isAfter(startTime)) {
      after(time, startTime, startInclusive) && before(time, endTime, endInclusive)
    } else {
      after(time, startTime, startInclusive) || before(time, endTime, endInclusive)
    }
  }
}

object Session {
  /** Creates an unnamed trading session. */
  def apply(startTime: LocalTime, startInclusive: Boolean, endTime: LocalTime,
      endInclusive: Boolean): Session =
    Session("", startTime, startInclusive, endTime, endInclusive)

  /** Creates a trading session with inclusive opening time and non-inclusive closing time. */
  def apply(name: String, startTime: LocalTime, endTime: LocalTime): Session =
    Session(name, startTime, true, endTime, false)

  /** Creates an unnamed trading session with inclusive opening time and non-inclusive closing
    * time. */
  def apply(startTime: LocalTime, endTime: LocalTime): Session =
    Session(startTime, true, endTime, false)

  private def after(time: LocalTime, limitTime: LocalTime, inclusive: Boolean): Boolean = {
    time.isAfter(limitTime) || inclusive && time == limitTime
  }

  private def before(time: LocalTime, limitTime: LocalTime, inclusive: Boolean): Boolean = {
    time.isBefore(limitTime) || inclusive && time == limitTime
  }
}