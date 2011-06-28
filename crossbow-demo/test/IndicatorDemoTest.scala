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

object TestUtils {
  import org.scalatest.TestFailedException

  def approx(expected: Double, e: Double)(value: Double) {
    if(value < expected - e || value > expected + e) throw new TestFailedException(
      "Expected "+expected+" ±"+e+", but got "+value, 0)
  }
}
