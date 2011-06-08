/*
 * Copyright 2011 Vilius Normantas <code@norma.lt>
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
import lt.norma.crossbow.testutils._
import org.joda.time.DateTime
import org.scalatest.FunSuite

class RsiTest extends FunSuite {
  class I(n: String) extends Indicator[Double] {
    def name = n
    def dependencies = Empty
    def calculate = Empty
  }

  test("RSI indicator") {
    val i1 = new I("A") with History
    val i = new Rsi(5, i1)
    val l = new IndicatorList(i)
    val e = 0.000005

    expect("RSI(5; A)") { i.name }
    expect(2) { i.dependencies.size }
    expect(None) { i() }

    l.send(new BarClose(new DateTime))
    expect(None) { i() }
    l.send(new BarClose(new DateTime))
    expect(None) { i() }
    l.send(new BarClose(new DateTime))
    expect(None) { i() }
    l.send(new BarClose(new DateTime))
    expect(None) { i() }

    i1.set(80)
    l.send(new BarClose(new DateTime))
    expect(None) { i() }
    i1.set(90)
    l.send(new BarClose(new DateTime))
    expect(None) { i() }
    i1.set(80)
    l.send(new BarClose(new DateTime))
    expect(None) { i() }
    i1.set(110)
    l.send(new BarClose(new DateTime))
    expect(None) { i() }
    i1.set(90)
    l.send(new BarClose(new DateTime))
    expect(None) { i() }

    i1.set(90)
    l.send(new BarClose(new DateTime))
    approx(54.16667, e) { i.value }
    i1.set(90)
    l.send(new BarClose(new DateTime))
    approx(54.16667, e) { i.value }
    i1.set(110)
    l.send(new BarClose(new DateTime))
    approx(79.77011, e) { i.value }
    i1.set(120)
    l.send(new BarClose(new DateTime))
    approx(85.74322, e) { i.value }
    i1.set(130)
    l.send(new BarClose(new DateTime))
    approx(90.11930, e) { i.value }
    i1.set(145.01)
    l.send(new BarClose(new DateTime))
    approx(94.15721, e) { i.value }

    i1.unset()
    l.send(new BarClose(new DateTime))
    expect(None) { i() }

    i1.set(100)
    l.send(new BarClose(new DateTime))
    approx(33.17520, e) { i.value }
    i1.set(110)
    l.send(new BarClose(new DateTime))
    approx(45.03813, e) { i.value }

    i1.unset()
    l.send(new BarClose(new DateTime))
    expect(None) { i() }
    l.send(new BarClose(new DateTime))
    expect(None) { i() }
    l.send(new BarClose(new DateTime))
    expect(None) { i() }

    i1.set(90)
    l.send(new BarClose(new DateTime))
    approx(29.38735, e) { i.value }
    i1.set(80)
    l.send(new BarClose(new DateTime))
    approx(23.31172, e) { i.value }
    i1.set(60)
    l.send(new BarClose(new DateTime))
    approx(14.38792, e) { i.value }
    i1.set(70)
    l.send(new BarClose(new DateTime))
    approx(33.48464, e) { i.value }
    i1.set(170)
    l.send(new BarClose(new DateTime))
    approx(84.69474, e) { i.value }
    i1.set(92)
    l.send(new BarClose(new DateTime))
    approx(44.55785, e) { i.value }
    i1.set(80)
    l.send(new BarClose(new DateTime))
    approx(40.16530, e) { i.value }
    i1.set(75)
    l.send(new BarClose(new DateTime))
    approx(37.83422, e) { i.value }
    i1.set(80)
    l.send(new BarClose(new DateTime))
    approx(42.81270, e) { i.value }
    i1.set(110)
    l.send(new BarClose(new DateTime))
    approx(66.76617, e) { i.value }
    i1.set(115)
    l.send(new BarClose(new DateTime))
    approx(69.91637, e) { i.value }
  }

  test("RSI indicator - invalid period") {
    intercept[IllegalArgumentException] { new Rsi(0, new I("A") with History)  }
    intercept[IllegalArgumentException] { new Rsi(-5, new I("A") with History)  }
  }
}
