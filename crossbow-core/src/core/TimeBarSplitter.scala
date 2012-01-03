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

/*package lt.norma.crossbow.core

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

class TimeBarSplitter(val barSize: Duration, var sessionOpenTime: Option[DateTime] = None)
    extends DataNode {
  def dependencies = Empty
  var previousBarTime = new DateTime(0)
  var firstBar = true

  def receive = {
    case so @ SessionOpen(time) =>
      sessionOpenTime = Some(time)
    case data: Data if(sessionOpenTime.isDefined) =>
      val periodCount = (data.marketTime.getMillis - sessionOpenTime.getMillis) / barSize.getMillis
      val barTime = new DateTime()
      zeroMoment + periodCount * barSize.getMillis
      if(firstBar) {
        //previousBarTime =
      }
  }

  /*

  def receive = {
    case data: Data =>
      // On data message, check for bar open/close events and forward the data message
      checkBar(data.marketTime)
      dispatch(data)
    case m =>
      // Forward all other messages
      dispatch(m)
  }

  def checkBar(time: DateTime) {
    val barTime = zeroMoment + periodCount * barSize.getMillis
    if(firstBar) {
      previousBarTime = barTime
      firstBar = false
      dispatch(BarOpen(new DateTime(barTime, timeZone)))
    } else if(barTime != previousBarTime) {
      dispatch(BarClose(new DateTime(previousBarTime + barSize, timeZone)))
      dispatch(BarOpen(new DateTime(barTime, timeZone)))
      previousBarTime = barTime;
    }
  }*/
}*/
// TODO
