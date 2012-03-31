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

import lt.norma.crossbow.messages._
import lt.norma.crossbow.core._
import lt.norma.crossbow.testutils._
import org.scalatest.FunSuite
import lt.norma.crossbow.indicators.{AverageSet, Variable}
import lt.norma.crossbow.messages.EmptyMessage

class AverageSetTest extends FunSuite {
  test("name") {
    val tartget1 = new Variable[Double] {
      override def name = "A"
    }
    val tartget2 = new Variable[Double] {
      override def name = "B"
    }
    val tartget3 = new Variable[Double] {
      override def name = "C"
    }
    val indicator = new AverageSet(tartget1, tartget2, tartget3)
    expect("AverageSet(A; B; C)") {
      indicator.name
    }
  }

  test("AverageSet indicator") {
    val e = 0.00001
    val i1 = new Variable[Double] {
      override def name = "A"
    }
    val i2 = new Variable[Double] {
      override def name = "B"
    }
    val i3 = new Variable[Double] {
      override def name = "C"
    }
    val i = new AverageSet(i1, i2, i3)
    val l = new IndicatorList(i)
    expect("AverageSet(A; B; C)") {
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
    approx(1, e) {
      i1.set(1)
      l.send(EmptyMessage)
      i.value
    }
    approx(1.5, e) {
      i2.set(2)
      l.send(EmptyMessage)
      i.value
    }
    approx(2.33333, e) {
      i3.set(4)
      l.send(EmptyMessage)
      i.value
    }
    approx(7.66666, e) {
      i3.set(20)
      l.send(EmptyMessage)
      i.value
    }
    approx(1.5, e) {
      i3.unset()
      l.send(EmptyMessage)
      i.value
    }
    approx(1, e) {
      i2.unset()
      l.send(EmptyMessage)
      i.value
    }
    expect(None) {
      i1.unset()
      l.send(EmptyMessage)
      i()
    }
  }
  test("one indicator") {
    val i1 = new Variable[Double] {
      override def name = "A"
    }
    val i = new AverageSet(i1)
    val l = new IndicatorList(i)
    expect("AverageSet(A)") {
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
    val i = new AverageSet()
    val l = new IndicatorList(i)
    expect("AverageSet()") {
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
