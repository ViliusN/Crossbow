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

/*import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.scalatest.FunSuite

class TimeBarSplitterTest extends FunSuite {
  val zone = DateTimeZone.forID("America/New_York")
  test("creation") {
    val tbs = new TimeBarSplitter(60000, zone)
    expect(new DateTime(1900, 1, 1, 0, 0, 0, 0, zone) getMillis) { tbs.zeroMoment }
    expect(true) { tbs.firstBar }
    expect(0) { tbs.previousBarTime }
  }
  test("first bar") {
    val s = Stock("MSFT", Nasdaq, "USD")
    var lastMessage: Option[Message] = None
    val l = Listener {
      case m => lastMessage = Some(m)
    }
    val tbs = new TimeBarSplitter(60 * 60 * 1000, zone)
    tbs.add(l)
    tbs.send(Quote(s, 30.05, 100000, 30.02, 500, new DateTime(2011, 3, 4, 7, 9, 8, 155, zone)))
    l
  }
}*/
// TODO
