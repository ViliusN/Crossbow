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

package lt.norma.crossbow.demo

import lt.norma.crossbow.core._
import lt.norma.crossbow.indicators._
import org.joda.time.DateTime
import org.scalatest.FunSuite
import TestUtils._

class Indicator1Test extends FunSuite {
  val i = new Indicator1
  expect("My first indicator") { i.name }
  expect(Set.empty) { i.dependencies }

  expect(None) { i.optionalValue }
  expect(None) { i() }
  intercept[Indicator.ValueNotSetException] { i.value }
  assert { !i.isSet }

  i.set(1.5)
  expect(Some(1.5)) { i.optionalValue }
  expect(Some(1.5)) { i() }
  expect(1.5) { i.value }
  assert { i.isSet }

  i.unset()
  expect(None) { i.optionalValue }
  assert { !i.isSet }
}

class Indicator2Test extends FunSuite {
  val i = new Indicator2
  expect("Last price squared") { i.name }
  expect(Set.empty) { i.dependencies }
  expect(None) { i() }

  val s = Stock("MSFT", Exchange.nasdaq, "USD")
  i.send(Trade(s, 24.63, 10000, new DateTime))
  expect(606.6369) { i.value }

  i.send(Quote(s, 24.65, 10000, 24.63, 10000, new DateTime))
  expect(606.6369) { i.value }

  i.send(SessionClose(new DateTime))
  assert { !i.isSet }
  expect(None) { i() }

  i.send(Quote(s, 24.65, 10000, 24.63, 10000, new DateTime))
  assert { !i.isSet }

  i.send(Trade(s, 30.0, 10000, new DateTime))
  expect(900) { i.value }
}

class Indicator3Test extends FunSuite {
  val target1 = new Indicator1 { override def name = "T1" }
  val target2 = new Indicator1 { override def name = "T2" }
  val i = new Indicator3(target1, target2)
  val list = new IndicatorList(i)
  expect("T1 + T2") { i.name }
  expect(Set(target1, target2)) { i.dependencies }
  expect(None) { i() }

  list.send(new Data { })
  expect(None) { i() }

  target1.set(5)
  list.send(new Data { })
  expect(None) { i() }

  target1.set(4)
  target2.set(7)
  list.send(new Data { })
  expect(11) { i.value }

  target1.unset()
  list.send(new Data { })
  expect(None) { i() }
}

class Indicator4Test extends FunSuite {
  val s = Stock("MSFT", Exchange.nasdaq, "USD")
  val i = new Indicator4(s)
  expect("Number of ticks for MSFT") { i.name }
  expect(Set.empty) { i.dependencies }
  expect(0) { i.value }

  i.send(Trade(s, 24.63, 10000, new DateTime))
  expect(1) { i.value }
  i.send(Trade(s, 24.63, 10000, new DateTime))
  expect(2) { i.value }
  i.send(Trade(s, 24.63, 10000, new DateTime))
  expect(3) { i.value }
  val sOther = Stock("GOOG", Exchange.nasdaq, "USD")
  i.send(Trade(sOther, 24.63, 10000, new DateTime))
  expect(3) { i.value }

  i.send(BarOpen(new DateTime))
  expect(0) { i.value }
  i.send(Trade(s, 24.63, 10000, new DateTime))
  expect(1) { i.value }

  i.unset()
  expect(0) { i.value }
}

class Indicator5Test extends FunSuite {
  val s = Stock("MSFT", Exchange.nasdaq, "USD")
  val i = new Indicator5(s)
  val list = new IndicatorList(i)
  val e = 0.005
  expect("Up-tick percent (MSFT)") { i.name }
  expect(2) { i.dependencies.size }
  expect(None) { i() }

  list.send(Trade(s, 24.63, 10000, new DateTime))
  expect(None) { i() }
  list.send(Trade(s, 24.63, 10000, new DateTime))
  list.send(Trade(s, 24.63, 10000, new DateTime))
  expect(None) { i() }
  list.send(Trade(s, 24.65, 10000, new DateTime))
  approx(100, e) { i.value }
  list.send(Trade(s, 24.66, 10000, new DateTime))
  approx(100, e) { i.value }
  list.send(Trade(s, 24.66, 10000, new DateTime))
  approx(100, e) { i.value }
  list.send(Trade(s, 24.64, 10000, new DateTime))
  approx(66.67, e) { i.value }
  list.send(Trade(s, 24.64, 10000, new DateTime))
  approx(66.67, e) { i.value }
  list.send(Trade(s, 24.63, 10000, new DateTime))
  approx(50, e) { i.value }
  list.send(Trade(s, 24.62, 10000, new DateTime))
  approx(40, e) { i.value }

  list.send(BarOpen(new DateTime))
  expect(None) { i() }
  list.send(Trade(s, 24.63, 10000, new DateTime))
  expect(None) { i() }
  list.send(Trade(s, 24.62, 10000, new DateTime))
  approx(0, e) { i.value }
  list.send(Trade(s, 24.64, 10000, new DateTime))
  approx(50, e) { i.value }
}

class Indicator2HistoryTest extends FunSuite {
  val ih = new Indicator2 with History
  val i = new Indicator2
  val list = new IndicatorList(i, ih)
  val e = 0.005

  intercept[Exception] { i.history }
  assert { !i.hasHistory }
  assert { ih.hasHistory }

  assert { ih.history.isEmpty }
  val s = Stock("MSFT", Exchange.nasdaq, "USD")
  list.send(Trade(s, 2, 10000, new DateTime))
  list.send(Trade(s, 3, 10000, new DateTime))
  list.send(Trade(s, 4, 10000, new DateTime))
  assert { ih.history.isEmpty }
  list.send(BarClose(new DateTime))
  expect(1) { ih.history.size }
  approx(16, e) { ih.history.last.get }

  list.send(Trade(s, 5, 10000, new DateTime))
  list.send(BarClose(new DateTime))
  approx(25, e) { ih.history.last.get }
  list.send(Trade(s, 6, 10000, new DateTime))
  list.send(BarClose(new DateTime))
  approx(36, e) { ih.history.last.get }
  ih.unset()
  list.send(BarClose(new DateTime))
  expect(None) { ih.history.last }
  approx(36, e) { ih.history.lastSet.get }
  list.send(Trade(s, 7, 10000, new DateTime))
  list.send(BarClose(new DateTime))
  approx(49, e) { ih.history.last.get }
  expect(5) { ih.history.size }

  expect(List(Some(36), None, Some(49))) { ih.history.take(3) }
  expect(List(Some(16), Some(25), Some(36), None, Some(49))) { ih.history.take(20) }
  expect(List(Some(25), Some(36), Some(49))) { ih.history.takeSet(3) }
  expect(List(Some(16), Some(25), Some(36), None, Some(49))) { ih.history.values }
}

class IndicatorToStringTest extends FunSuite {
  val i1 = new Indicator1 { override def name = "I1" }
  expect("N/A") { i1.valueToString }

  i1.set(0.5)
  expect("0.5") { i1.valueToString }
  expect("I1: 0.5") { i1.toString }

  expect("123.0") { i1.valueToString(123.0) }
  expect("N/A") { i1.valueToString("sss") }

  val i2 = new Indicator1 {
    override def name = "I2"
    override def valueToString(v: Double) = "["+v+"]"
    override def valueNotSetString = "[not available]"
    override def toString = "my value is "+valueToString()
  }
  expect("[not available]") { i2.valueToString }
  expect("my value is [not available]") { i2.toString }
  i2.set(456)
  expect("[456.0]") { i2.valueToString }
  expect("my value is [456.0]") { i2.toString }
  expect("[123.45]") { i2.valueToString(123.45) }
}

class CombiningIndicatorsTest extends FunSuite {
  val i1 = new Variable[Double] { override def name = "I1" }
  val i2 = new Variable[Double] { override def name = "I2" }
  val i3 = new Variable[Double] { override def name = "I3" }

  val sum = new Sum(i1, i2)
  val avg = new Average(i1, i2, i3)
  val diff = new Difference(i1, i2)
  val list = new IndicatorList(sum, avg, diff)

  expect("Sum(I1; I2)") { sum.name }
  expect("Average(I1; I2; I3)") { avg.name }
  expect("Difference(I1; I2)") { diff.name }

  assert { sum.isEmpty }
  assert { avg.isEmpty }
  assert { diff.isEmpty }
  list.send(EmptyData)
  assert { sum.isEmpty }
  assert { avg.isEmpty }
  assert { diff.isEmpty }
  i1.set(5)
  list.send(EmptyData)
  assert { sum.isEmpty }
  assert { avg.isEmpty }
  assert { diff.isEmpty }

  i2.set(6)
  list.send(EmptyData)
  expect(11) { sum.value }
  assert { avg.isEmpty }
  expect(-1) { diff.value }

  i1.set(9)
  i3.set(8)
  list.send(EmptyData)
  expect(15) { sum.value }
  approx(7.7, 0.05) { avg.value }
  expect(3) { diff.value }
}
