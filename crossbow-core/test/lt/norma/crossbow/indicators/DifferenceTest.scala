package lt.norma.crossbow.indicators

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

import lt.norma.crossbow.core._
import lt.norma.crossbow.messages._
import org.scalatest.FunSuite
import lt.norma.crossbow.indicators.{Difference, MutableIndicator}
import lt.norma.crossbow.messages.EmptyMessage

class DifferenceTest extends FunSuite {

  class I(n: String) extends MutableIndicator[Double] {
    def name = n

    def dependencies = Empty
  }

  test("alternative constructor") {
    val target1 = new I("A")
    target1.set(123)
    val target2 = new I("B")
    target2.set(456)
    val diff = new Difference(target1, target2)
    val list = new IndicatorList(diff)
    expect("(A - B)") {
      diff.name
    }
    expect(2) {
      diff.dependencies.size
    }
    expect(Some(-333)) {
      diff()
    }
  }
  test("Difference indicator") {
    val i1 = new I("A")
    val i2 = new I("B")
    val i = new Difference(i1, i2)
    val l = new IndicatorList(i)

    expect("(A - B)") {
      i.name
    }
    expect(Set(i1, i2)) {
      i.dependencies
    }
    expect(None) {
      i()
    }

    l.send(EmptyMessage)
    expect(None) {
      i()
    }

    i1.set(1)
    l.send(EmptyMessage)
    expect(None) {
      i()
    }

    i2.set(5)
    l.send(EmptyMessage)
    expect(-4) {
      i.value
    }

    i1.set(123.123)
    i2.set(0.123)
    l.send(EmptyMessage)
    expect(123) {
      i.value
    }

    i1.unset()
    l.send(EmptyMessage)
    expect(None) {
      i()
    }

    i1.set(80)
    i2.set(65)
    l.send(EmptyMessage)
    expect(15) {
      i.value
    }

    i2.unset()
    l.send(EmptyMessage)
    expect(None) {
      i()
    }

    i1.set(0)
    i2.set(0)
    l.send(EmptyMessage)
    expect(0) {
      i.value
    }
  }
}
