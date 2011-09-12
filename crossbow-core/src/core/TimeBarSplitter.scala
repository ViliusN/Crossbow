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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

/** Receives market quote and trade data messages and uses them to generate `BarOpen` and `BarClose`
  * events. All received messages are forwarded to listeners.
  * @param barSize   size of a bar in milliseconds
  * @param timeZone  time zone used for generated bar events */
class TimeBarSplitter(barSize: Long, timeZone: DateTimeZone) extends DataNode {
  def dependencies = Empty
  val zeroMoment: Long = new DateTime(1900, 1, 1, 0, 0, 0, 0, timeZone) getMillis
  var firstBar = true
  var previousBarTime = 0L

  def receive = {
    case q @ Quote(_, _, _, _, _, time) =>
      checkBar(time)
      dispatch(q)
    case t @ Trade(_, _, _, time) =>
      checkBar(time)
      dispatch(t)
    case m =>
      dispatch(m)
  }

  def checkBar(time: DateTime) {
    val nper: Long = (time.getMillis - zeroMoment) / barSize
    val barTime = zeroMoment + nper * barSize
    if(firstBar) {
      previousBarTime = barTime
      firstBar = false
      dispatch(BarOpen(new DateTime(barTime, timeZone)))
    } else if(barTime != previousBarTime) {
      dispatch(BarClose(new DateTime(previousBarTime + barSize, timeZone)))
      dispatch(BarOpen(new DateTime(barTime, timeZone)))
      previousBarTime = barTime;
    }
  }
}
// TODO test
// TODO kaip uzdaryti paskutini bara?
