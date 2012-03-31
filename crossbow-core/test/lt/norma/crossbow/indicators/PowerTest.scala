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

import lt.norma.crossbow.messages.EmptyMessage
import lt.norma.crossbow.messages.EmptyMessage
import lt.norma.crossbow.core.{testutils, IndicatorList}
import lt.norma.crossbow.testutils.approx
import org.scalatest.FunSuite
import lt.norma.crossbow.indicators.{Power, Variable}

class PowerTest extends FunSuite {
  val e = 0.00005

  test("name") {
    val target = new Variable(5.0) {
      override def name = "A"
    }
    val power = new Variable(5.0) {
      override def name = "B"
    }
    val indicator = new Power(target, power)
    expect("Power(A; B)") {
      indicator.name
    }
  }

  test("dependencies") {
    val target = new Variable(5.0)
    val power = new Variable(5.0)
    val indicator = new Power(target, power)
    expect(Set(target, power)) {
      indicator.dependencies
    }
  }

  test("calculation") {
    val target = new Variable[Double]
    val power = new Variable[Double]
    val indicator = new Power(target, power)
    val list = new IndicatorList(indicator)
    approx(16, e) {
      target.set(4)
      power.set(2)
      list.send(EmptyMessage)
      indicator.value
    }
    approx(2, e) {
      target.set(8)
      power.set(1.0 / 3)
      list.send(EmptyMessage)
      indicator.value
    }
    approx(1, e) {
      target.set(1)
      power.set(1)
      list.send(EmptyMessage)
      indicator.value
    }
    approx(1, e) {
      target.set(0)
      power.set(0)
      list.send(EmptyMessage)
      indicator.value
    }
  }

  test("calculation - invalid values") {
    val target = new Variable[Double]
    val power = new Variable[Double]
    val indicator = new Power(target, power)
    val list = new IndicatorList(indicator)
    expect(None) {
      target.set(-0.0001)
      power.set(0.5)
      list.send(EmptyMessage)
      indicator()
    }
    expect(None) {
      target.set(1)
      power.set(Double.PositiveInfinity)
      list.send(EmptyMessage)
      indicator()
    }
  }

  test("calculation - empty target and/or power") {
    val target = new Variable[Double]
    val power = new Variable[Double]
    val indicator = new Power(target, power)
    val list = new IndicatorList(indicator)
    expect(None) {
      target.unset()
      power.unset()
      list.send(EmptyMessage)
      indicator()
    }
    expect(None) {
      target.set(2)
      power.unset()
      list.send(EmptyMessage)
      indicator()
    }
    expect(None) {
      target.unset()
      power.set(2)
      list.send(EmptyMessage)
      indicator()
    }
  }

  test("alternative constructor") {
    val target = new Variable[Double](5) {
      override def name = "T"
    }
    val i = new Power(target, 2)
    val list = new IndicatorList(i)
    expect("Power(T; 2.0)") {
      i.name
    }
    expect(Some(25)) {
      i()
    }
  }
}
