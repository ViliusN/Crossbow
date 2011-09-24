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
import lt.norma.crossbow.testutils._
import org.scalatest.FunSuite

class DivideTest extends FunSuite {
  class I(n: String) extends Indicator[Double] {
    def name = n
    def dependencies = Empty
    def calculate = Empty
  }
  val e = 0.00001
  test("alternative constructor") {
    val target1 = new I("A")
    target1.set(123)
    val target2 = new I("B")
    target2.set(3)
    val div = new Divide(target1, target2)
    val list = new IndicatorList(div)
    expect("A / B") { div.name }
    expect(2) { div.dependencies.size }
    approx(41, e) { div.value }
  }
  test("calculation") {
    val i1 = new I("A")
    val i2 = new I("B")
    val i = new Divide(i1, i2)
    val l = new IndicatorList(i)

    expect("A / B") { i.name }
    expect(Set(i1, i2)) { i.dependencies }
    expect(None) { i() }

    l.send(new Data { })
    expect(None) { i() }

    i1.set(1)
    l.send(new Data { })
    expect(None) { i() }

    i2.set(5)
    l.send(new Data { })
    approx(0.2, e) { i.value }

    i1.set(123.123)
    i2.set(0.123)
    l.send(new Data { })
    approx(1001, e) { i.value }

    i1.unset()
    l.send(new Data { })
    expect(None) { i() }

    i1.set(80)
    i2.set(25)
    l.send(new Data { })
    approx(3.2, e) { i.value }

    i2.unset()
    l.send(new Data { })
    expect(None) { i() }
  }
  test("unset values - both") {
    val target1 = new I("A")
    val target2 = new I("B")
    val division = new Divide(target1, target2)
    val list = new IndicatorList(division)
    expect(None) { division() }
  }
  test("unset values - first") {
    val target1 = new I("A")
    val target2 = new I("B")
    target2.set(8)
    val division = new Divide(target1, target2)
    val list = new IndicatorList(division)
    expect(None) { division() }
  }
  test("unset values - second") {
    val target1 = new I("A")
    target1.set(8)
    val target2 = new I("B")
    val division = new Divide(target1, target2)
    val list = new IndicatorList(division)
    expect(None) { division() }
  }
  test("zero values - both") {
    val target1 = new I("A")
    target1.set(0)
    val target2 = new I("B")
    target2.set(0)
    val division = new Divide(target1, target2)
    val list = new IndicatorList(division)
    expect(None) { division() }
  }
  test("zero values - first") {
    val target1 = new I("A")
    target1.set(0)
    val target2 = new I("B")
    target2.set(8)
    val division = new Divide(target1, target2)
    val list = new IndicatorList(division)
    expect(Some(0)) { division() }
  }
  test("zero values - second") {
    val target1 = new I("A")
    target1.set(9)
    val target2 = new I("B")
    target2.set(0)
    val division = new Divide(target1, target2)
    val list = new IndicatorList(division)
    expect(None) { division() }
  }
}
