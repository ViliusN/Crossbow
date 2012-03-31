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

package lt.norma.crossbow.indicators

import lt.norma.crossbow.core._
import lt.norma.crossbow.messages._
import org.joda.time.DateTime
import org.scalatest.FunSuite
import lt.norma.crossbow.indicators.BarCount

class BarCountTest extends FunSuite {
  test("BarCount test") {
    val i = new BarCount
    expect("Bar Count") {
      i.name
    }
    expect(Set.empty) {
      i.dependencies
    }
    expect(0) {
      i.value
    }

    i.send(BarClose(new DateTime))
    expect(1) {
      i.value
    }

    i.send(BarClose(new DateTime))
    expect(2) {
      i.value
    }

    i.unset()
    expect(0) {
      i.value
    }

    i.send(BarClose(new DateTime))
    i.send(BarClose(new DateTime))
    i.send(BarClose(new DateTime))
    i.send(BarClose(new DateTime))
    i.send(BarClose(new DateTime))
    expect(5) {
      i.value
    }
  }
}
