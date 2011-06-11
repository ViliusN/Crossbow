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
import org.joda.time.DateTime
import org.scalatest.FunSuite

class CumulativeValueTest extends FunSuite {
  class I(n: String) extends Indicator[Double] {
    def name = n
    def dependencies = Empty
    def calculate = Empty
  }

  test("CumulativeValue indicator") {
    val i1 = new I("A")
    val i = new CumulativeValue(i1)
    val l = new IndicatorList(i)

    expect("Cumulative(A)") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }

    l.send(BarClose(new DateTime))
    expect(None) { i() }

    i1.set(1)
    l.send(BarClose(new DateTime))
    expect(1) { i.value }

    l.send(BarClose(new DateTime))
    expect(2) { i.value }

    i1.set(0.001)
    l.send(BarClose(new DateTime))
    expect(2.001) { i.value }

    i1.unset()
    l.send(BarClose(new DateTime))
    expect(None) { i() }

    i1.set(100)
    l.send(BarClose(new DateTime))
    expect(102.001) { i.value }
  }
}
