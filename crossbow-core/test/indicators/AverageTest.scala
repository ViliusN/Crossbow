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
import lt.norma.crossbow.testutils._
import org.scalatest.FunSuite

class AverageTest extends FunSuite {
  class I(n: String) extends MutableIndicator[Double] {
    def name = n
    def dependencies = Empty
  }

  test("Average indicator") {
    val e = 0.00001
    val i1 = new I("A")
    val i2 = new I("B")
    val i3 = new I("C")
    val i = new Average(i1, i2, i3)
    val l = new IndicatorList(i)

    expect("Average(A; B; C)") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }

    l.send(EmptyMessage)
    expect(None) { i() }

    i1.set(1)
    l.send(EmptyMessage)
    expect(None) { i() }

    i2.set(2)
    l.send(EmptyMessage)
    expect(None) { i() }

    i3.set(4)
    l.send(EmptyMessage)
    approx(2.33333, e) { i.value }

    i3.set(20)
    l.send(EmptyMessage)
    approx(7.66666, e) { i.value }

    i3.unset()
    l.send(EmptyMessage)
    expect(None) { i() }

    i3.set(10)
    l.send(EmptyMessage)
    approx(4.33333, e) { i.value }

    i1.set(2)
    l.send(EmptyMessage)
    approx(4.66666, e) { i.value }
  }

  test("One indicator") {
    val i1 = new I("A")
    val i = new Average(i1)
    val l = new IndicatorList(i)

    expect("Average(A)") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }

    i1.set(15)
    l.send(EmptyMessage)
    expect(15) { i.value }
  }

  test("No indicators") {
    val i = new Average()
    val l = new IndicatorList(i)

    expect("Average()") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }

    l.send(EmptyMessage)
    expect(None) { i() }
  }
}
