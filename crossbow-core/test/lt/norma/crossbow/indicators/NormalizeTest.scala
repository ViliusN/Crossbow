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

import lt.norma.crossbow.core.IndicatorList
import lt.norma.crossbow.messages.EmptyMessage
import lt.norma.crossbow.core.testutils.approx
import org.scalatest.FunSuite

class NormalizeTest extends FunSuite {
  val e = 0.00005

  test("name") {
    val target = new Variable[Double] {
      override def name = "T"
    }
    val indicator = new Normalize(target)
    expect("Normalize(T)") {
      indicator.name
    }
  }

  test("dependencies") {
    val target = new Variable[Double] {
      override def name = "T"
    }
    val indicator = new Normalize(target)
    expect(2) {
      indicator.dependencies.size
    }
  }

  test("calculation") {
    val target = new Variable[Double] {
      override def name = "T"
    }
    val indicator = new Normalize(target)
    val list = new IndicatorList(indicator)
    expect(None) {
      indicator()
    }
    list.send(EmptyMessage)
    expect(None) {
      indicator()
    }
    target.set(5)
    list.send(EmptyMessage)
    approx(0, e) {
      indicator.value
    }
    target.set(6)
    list.send(EmptyMessage)
    approx(20, e) {
      indicator.value
    }
    target.set(0)
    list.send(EmptyMessage)
    approx(-100, e) {
      indicator.value
    }
    target.unset()
    list.send(EmptyMessage)
    expect(None) {
      indicator()
    }
    target.set(5)
    list.send(EmptyMessage)
    approx(0, e) {
      indicator.value
    }
  }

  test("first value is 0") {
    val target = new Variable[Double] {
      override def name = "T"
    }
    val indicator = new Normalize(target)
    val list = new IndicatorList(indicator)
    expect(None) {
      indicator()
    }
    list.send(EmptyMessage)
    expect(None) {
      indicator()
    }
    target.set(0)
    list.send(EmptyMessage)
    assert {
      indicator.value.isNaN
    }
    target.set(5)
    list.send(EmptyMessage)
    assert {
      indicator.value.isInfinity
    }
  }
}
