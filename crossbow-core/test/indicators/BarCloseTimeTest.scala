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

package lt.norma.crossbow.core.indicators

import lt.norma.crossbow.core._
import lt.norma.crossbow.core.messages._
import org.joda.time.DateTime
import org.scalatest.FunSuite

class BarCloseTimeTest extends FunSuite {
  test("BarCloseTime") {
    val i = new BarCloseTime
    expect("Bar Close Time") { i.name }
    expect(Set.empty) { i.dependencies }
    expect(None) { i.optionalValue }

    val t1 = new DateTime(2009, 01, 25, 13, 15, 0, 0, Settings.timeZone)
    i.send(BarClose(t1))
    expect(t1) { i.value }
    expect("2009-01-25 13:15:00") { i.valueToString() }

    val t2 = new DateTime(2009, 01, 25, 13, 30, 0, 0)
    i.send(BarClose(t2))
    expect(t2) { i.value }
  }
}
