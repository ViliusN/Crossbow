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

import lt.norma.crossbow.core.IndicatorList
import lt.norma.crossbow.messages.EmptyMessage
import lt.norma.crossbow.messages.EmptyMessage
import org.scalatest.FunSuite
import lt.norma.crossbow.indicators.{SumSet, Variable}

class SumSetTest extends FunSuite {
  test("SumSet indicator") {
    val i1 = new Variable[Double] {
      override def name = "A"
    }
    val i2 = new Variable[Double] {
      override def name = "B"
    }
    val i3 = new Variable[Double] {
      override def name = "C"
    }
    val i = new SumSet(i1, i2, i3)
    val l = new IndicatorList(i)
    expect("SumSet(A; B; C)") {
      i.name
    }
    expect(1) {
      i.dependencies.size
    }
    expect(None) {
      i()
    }
    expect(None) {
      l.send(EmptyMessage)
      i()
    }
    expect(1) {
      i1.set(1)
      l.send(EmptyMessage)
      i.value
    }
    expect(3) {
      i2.set(2)
      l.send(EmptyMessage)
      i.value
    }
    expect(7) {
      i3.set(4)
      l.send(EmptyMessage)
      i.value
    }
    expect(23) {
      i3.set(20)
      l.send(EmptyMessage)
      i.value
    }
    expect(3) {
      i3.unset()
      l.send(EmptyMessage)
      i.value
    }
    expect(13) {
      i3.set(10)
      l.send(EmptyMessage)
      i.value
    }
    expect(14) {
      i1.set(2)
      l.send(EmptyMessage)
      i.value
    }
    expect(None) {
      i1.unset()
      i2.unset()
      i3.unset()
      l.send(EmptyMessage)
      i()
    }
  }
  test("one indicator") {
    val i1 = new Variable[Double] {
      override def name = "A"
    }
    val i = new SumSet(i1)
    val l = new IndicatorList(i)
    expect("SumSet(A)") {
      i.name
    }
    expect(1) {
      i.dependencies.size
    }
    expect(None) {
      i()
    }
    expect(15) {
      i1.set(15)
      l.send(EmptyMessage)
      i.value
    }
  }
  test("no indicators") {
    val i = new SumSet()
    val l = new IndicatorList(i)
    expect("SumSet()") {
      i.name
    }
    expect(1) {
      i.dependencies.size
    }
    expect(None) {
      i()
    }
    expect(None) {
      l.send(EmptyMessage)
      i()
    }
  }
}
