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
import org.scalatest.FunSuite

class SumTest extends FunSuite {
  class I(n: String) extends Indicator[Double] {
    def name = n
    def dependencies = Empty
    def calculate = Empty
  }

  test("Sum indicator") {
    val i1 = new I("A")
    val i2 = new I("B")
    val i3 = new I("C")
    val i = new Sum(i1, i2, i3)
    val l = new IndicatorList(i)

    expect("Sum(A; B; C)") { i.name }
    expect(Set(i1, i2, i3)) { i.dependencies }
    expect(None) { i() }

    l.send(new Data { })
    expect(None) { i() }

    i1.set(1)
    l.send(new Data { })
    expect(None) { i() }

    i2.set(2)
    l.send(new Data { })
    expect(None) { i() }

    i3.set(4)
    l.send(new Data { })
    expect(7) { i.value }

    i3.set(20)
    l.send(new Data { })
    expect(23) { i.value }

    i3.unset()
    l.send(new Data { })
    expect(None) { i() }

    i3.set(10)
    l.send(new Data { })
    expect(13) { i.value }

    i1.set(2)
    l.send(new Data { })
    expect(14) { i.value }
  }

  test("One indicator") {
    val i1 = new I("A")
    val i = new Sum(i1)
    val l = new IndicatorList(i)

    expect("Sum(A)") { i.name }
    expect(Set(i1)) { i.dependencies }
    expect(None) { i() }

    i1.set(15)
    l.send(new Data { })
    expect(15) { i.value }
  }

  test("No indicators") {
    val i = new Sum()
    val l = new IndicatorList(i)

    expect("Sum()") { i.name }
    expect(Set()) { i.dependencies }
    expect(None) { i() }

    l.send(new Data { })
    expect(None) { i() }
  }
}
