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

import lt.norma.crossbow.core.IndicatorList
import lt.norma.crossbow.core.messages.{ BarClose, EmptyMessage, SessionClose, SessionOpen }
import org.joda.time.DateTime
import org.scalatest.FunSuite

class SessionBarCountTest extends FunSuite {
  test("name") {
    val indicator = new SessionBarCount
    expect("Session Bar Count") { indicator.name }
  }
  test("dependencies") {
    val indicator = new SessionBarCount
    expect(Set()) { indicator.dependencies }
  }
  test("default value") {
    val indicator = new SessionBarCount
    expect(Some(0)) { indicator() }
  }
  test("calculation") {
    val indicator = new SessionBarCount
    val list = new IndicatorList(indicator)
    expect(Some(0)) { indicator() }
    list.send(BarClose(new DateTime))
    expect(Some(1)) { indicator() }
    list.send(EmptyMessage)
    list.send(BarClose(new DateTime))
    expect(Some(2)) { indicator() }

    indicator.unset()
    expect(Some(0)) { indicator() }
    list.send(BarClose(new DateTime))
    list.send(BarClose(new DateTime))
    list.send(BarClose(new DateTime))
    list.send(BarClose(new DateTime))
    list.send(BarClose(new DateTime))
    expect(Some(5)) { indicator() }

    list.send(SessionClose(new DateTime))
    expect(Some(5)) { indicator() }
    list.send(BarClose(new DateTime))
    expect(Some(6)) { indicator() }

    list.send(SessionOpen(new DateTime))
    expect(Some(0)) { indicator() }
    list.send(BarClose(new DateTime))
    expect(Some(1)) { indicator() }
  }
}
