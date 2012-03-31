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

import lt.norma.crossbow.core.{ Empty, IndicatorList }
import lt.norma.crossbow.messages.EmptyMessage
import lt.norma.crossbow.messages.EmptyMessage
import org.scalatest.FunSuite

class SumTest extends FunSuite {

  class I(n: String) extends MutableIndicator[Double] {
    def name = n

    def dependencies = Empty
  }

  test("Sum indicator") {
    val i1 = new I("A")
    val i2 = new I("B")
    val i3 = new I("C")
    val i = new Sum(i1, i2, i3)
    val l = new IndicatorList(i)

    expect("(A + B + C)") {
      i.name
    }
    expect(Set(i1, i2, i3)) {
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

    i2.set(2)
    l.send(EmptyMessage)
    expect(None) {
      i()
    }

    i3.set(4)
    l.send(EmptyMessage)
    expect(7) {
      i.value
    }

    i3.set(20)
    l.send(EmptyMessage)
    expect(23) {
      i.value
    }

    i3.unset()
    l.send(EmptyMessage)
    expect(None) {
      i()
    }

    i3.set(10)
    l.send(EmptyMessage)
    expect(13) {
      i.value
    }

    i1.set(2)
    l.send(EmptyMessage)
    expect(14) {
      i.value
    }
  }

  test("One indicator") {
    val i1 = new I("A")
    val i = new Sum(i1)
    val l = new IndicatorList(i)

    expect("(A)") {
      i.name
    }
    expect(Set(i1)) {
      i.dependencies
    }
    expect(None) {
      i()
    }

    i1.set(15)
    l.send(EmptyMessage)
    expect(15) {
      i.value
    }
  }

  test("No indicators") {
    val i = new Sum()
    val l = new IndicatorList(i)

    expect("()") {
      i.name
    }
    expect(Set()) {
      i.dependencies
    }
    expect(None) {
      i()
    }

    l.send(EmptyMessage)
    expect(None) {
      i()
    }
  }
  test("initialization") {
    val target1 = new I("A")
    target1.set(123)
    val target2 = new I("B")
    target2.set(456)
    val sum = new Sum(target1, target2)
    val list = new IndicatorList(sum)
    expect("(A + B)") {
      sum.name
    }
    expect(2) {
      sum.dependencies.size
    }
    expect(Some(579)) {
      sum()
    }
  }
  test("alternative constructor") {
    val target = new I("A")
    val sum = new Sum(target, 500)
    val list = new IndicatorList(sum)
    expect("(A + 500.0)") {
      sum.name
    }
    expect(2) {
      sum.dependencies.size
    }
    expect(None) {
      sum()
    }
    target.set(28.5)
    list.send(EmptyMessage)
    expect(Some(528.5)) {
      sum()
    }
    target.set(1)
    list.send(EmptyMessage)
    expect(Some(501)) {
      sum()
    }
    target.unset()
    list.send(EmptyMessage)
    expect(None) {
      sum()
    }
  }
}
