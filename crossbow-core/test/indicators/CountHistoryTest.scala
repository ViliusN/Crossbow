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

class CountHistoryTest extends FunSuite {
  class I(n: String) extends MutableIndicator[Double] {
    def name = n
    def dependencies = Empty
  }

  test("CountHistory indicator") {
    val i1 = new I("A")
    val i = new CountHistory(i1)
    val l = new IndicatorList(i)

    expect("Count History(A)") { i.name }
    expect(Set(i1)) { i.dependencies }
    expect(0) { i.value }

    l.send(new BarClose(new DateTime))
    expect(0) { i.value }
    l.send(new BarClose(new DateTime))
    expect(0) { i.value }
    l.send(new BarClose(new DateTime))
    expect(0) { i.value }

    i1.set(0.5)
    l.send(new BarClose(new DateTime))
    expect(1) { i.value }

    i1.set(1)
    l.send(new BarClose(new DateTime))
    expect(2) { i.value }
    l.send(new BarClose(new DateTime))
    expect(3) { i.value }

    i1.unset()
    l.send(new BarClose(new DateTime))
    expect(3) { i.value }
    l.send(new BarClose(new DateTime))
    expect(3) { i.value }

    i1.set(1.5)
    l.send(new BarClose(new DateTime))
    expect(4) { i.value }
  }
}
