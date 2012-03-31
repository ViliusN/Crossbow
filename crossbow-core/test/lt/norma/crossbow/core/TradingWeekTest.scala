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

import org.joda.time.DateTimeConstants.{ MONDAY, TUESDAY, WEDNESDAY, SUNDAY }
import org.joda.time.LocalTime
import org.scalatest.FunSuite

class TradingWeekTest extends FunSuite {
  test("isTradingDay") {
    val tw = TradingWeek(Map(
      MONDAY -> List(Session("", new LocalTime(9, 30), new LocalTime(16, 0))),
      TUESDAY -> List(Session("", new LocalTime(9, 30), new LocalTime(16, 0))),
      WEDNESDAY -> Nil
    ))
    assert(tw.isTradingDay(MONDAY))
    assert(tw.isTradingDay(TUESDAY))
    assert(!tw.isTradingDay(WEDNESDAY))
    assert(!tw.isTradingDay(SUNDAY))
    assert(!tw.isTradingDay(0))
    assert(!tw.isTradingDay(8))
  }

  test("creation - invalid days of week") {
    intercept[Exception] {
      TradingWeek(Map(-1 -> List(Session("", new LocalTime(9, 30), new LocalTime(16, 0)))))
    }
    intercept[Exception] {
      TradingWeek(Map(0 -> List(Session("", new LocalTime(9, 30), new LocalTime(16, 0)))))
    }
    intercept[Exception] {
      TradingWeek(Map(8 -> List(Session("", new LocalTime(9, 30), new LocalTime(16, 0)))))
    }
    intercept[Exception] {
      TradingWeek(Map(100 -> List(Session("", new LocalTime(9, 30), new LocalTime(16, 0)))))
    }
  }
}
