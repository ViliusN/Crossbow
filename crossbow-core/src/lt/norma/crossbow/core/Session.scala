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

/** Represents trading session. */
trait Session {
  def name: String

  def isOpenAt(time: LocalTime): Boolean
}

object Session {

  /** Trading session which is open for 24 hours. */
  private case class TwentyFourHourSession(name: String) extends Session {
    def isOpenAt(time: LocalTime) = true
  }

  /** Trading session limited by specified trading hours. Use `LocalTime.MIDNIGHT` for start or end
    * times to represent open ended sessions. */
  private case class TimeSession(name: String, startTime: LocalTime, endTime: LocalTime)
    extends Session {
    def isOpenAt(time: LocalTime) = {
      if (endTime == LocalTime.MIDNIGHT) {
        !time.isBefore(startTime)
      } else if (endTime.isAfter(startTime)) {
        !(time.isBefore(startTime) || time.isAfter(endTime))
      } else {
        !(time.isBefore(startTime) && time.isAfter(endTime))
      }
    }
  }

  /** Creates a trading session with specified start and end times, and optional name. Use
    * `LocalTime.MIDNIGHT` for start or end times to represent open ended sessions. When start and
    * end times are equal, 24 hour session is returned. */
  def apply(name: String = "", startTime: LocalTime, endTime: LocalTime) {
    if (startTime == endTime) {
      TwentyFourHourSession(name)
    } else {
      TimeSession(name, startTime, endTime)
    }
  }

  /** Creates 24 hour trading session with specified name. */
  def apply(name: String): Session = TwentyFourHourSession(name)

  /** Creates unnamed 24 hour trading session. */
  def apply(): Session = TwentyFourHourSession("")
}
