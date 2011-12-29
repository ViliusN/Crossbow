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

package lt.norma.crossbow.core

import org.scalatest.FunSuite

class HistoricalValuesTest extends FunSuite {
  test("initial values should be empty") {
    val hw = new HistoricalValues({ () => Some(0) }, { v: Option[Int] => v.toString })
    expect(Nil) { hw.values }
  }

  test("update") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    hw.update
    expect(List(None)) { hw.values }
    value = Some(15)
    hw.update
    expect(List(None, Some(15))) { hw.values }
    hw.update
    expect(List(None, Some(15), Some(15))) { hw.values }
    value = None
    hw.update
    expect(List(None, Some(15), Some(15), None)) { hw.values }
    value = Some(-5)
    hw.update
    expect(List(None, Some(15), Some(15), None, Some(-5))) { hw.values }
  }

  test("size") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    expect(0) { hw.size }
    value = Some(15)
    hw.update
    expect(1) { hw.size }
    hw.update
    expect(2) { hw.size }
    value = None
    hw.update
    expect(3) { hw.size }
  }

  test("isEmpty") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    expect(true) { hw.isEmpty }
    value = Some(15)
    hw.update
    expect(false) { hw.isEmpty }
    hw.update
    expect(false) { hw.isEmpty }
    value = None
    hw.update
    expect(false) { hw.isEmpty }
  }

  test("valueAt") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    value = Some(1)
    hw.update
    value = Some(2)
    hw.update
    value = Some(3)
    hw.update
    expect(Some(3)) { hw.valueAt(0) }
    expect(Some(2)) { hw.valueAt(1) }
    expect(Some(1)) { hw.valueAt(2) }
  }

  test("valueAt - index out of bounds") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    value = Some(1)
    hw.update
    value = Some(2)
    hw.update
    value = Some(3)
    hw.update
    expect(None) { hw.valueAt(-100) }
    expect(None) { hw.valueAt(-2) }
    expect(None) { hw.valueAt(-1) }
    expect(None) { hw.valueAt(3) }
    expect(None) { hw.valueAt(4) }
    expect(None) { hw.valueAt(100) }
  }

  test("valueAt - empty history") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    expect(None) { hw.valueAt(-100) }
    expect(None) { hw.valueAt(-2) }
    expect(None) { hw.valueAt(-1) }
    expect(None) { hw.valueAt(0) }
    expect(None) { hw.valueAt(1) }
    expect(None) { hw.valueAt(2) }
    expect(None) { hw.valueAt(3) }
    expect(None) { hw.valueAt(4) }
    expect(None) { hw.valueAt(100) }
  }

  test("last") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    expect(None) { hw.last }
    value = Some(15)
    hw.update
    expect(Some(15)) { hw.last }
    value = Some(5)
    hw.update
    expect(Some(5)) { hw.last }
    value = None
    hw.update
    expect(None) { hw.last }
  }

  test("lastSet") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    expect(None) { hw.lastSet }
    value = Some(15)
    hw.update
    expect(Some(15)) { hw.lastSet }
    value = None
    hw.update
    expect(Some(15)) { hw.lastSet }
    value = Some(5)
    hw.update
    expect(Some(5)) { hw.lastSet }
    value = None
    hw.update
    expect(Some(5)) { hw.lastSet }
  }

  test("take") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    expect(Nil) { hw.take(5) }
    expect(Nil) { hw.take(2) }
    expect(Nil) { hw.take(1) }
    expect(Nil) { hw.take(0) }
    expect(Nil) { hw.take(-1) }
    value = Some(15)
    hw.update
    expect(List(Some(15))) { hw.take(5) }
    expect(List(Some(15))) { hw.take(2) }
    expect(List(Some(15))) { hw.take(1) }
    expect(Nil) { hw.take(0) }
    expect(Nil) { hw.take(-1) }
    value = Some(5)
    hw.update
    expect(List(Some(15), Some(5))) { hw.take(5) }
    expect(List(Some(15), Some(5))) { hw.take(2) }
    expect(List(Some(5))) { hw.take(1) }
    expect(Nil) { hw.take(0) }
    expect(Nil) { hw.take(-1) }
    value = None
    hw.update
    expect(List(Some(15), Some(5), None)) { hw.take(5) }
    expect(List(Some(5), None)) { hw.take(2) }
    expect(List(None)) { hw.take(1) }
    expect(Nil) { hw.take(0) }
    expect(Nil) { hw.take(-1) }
    value = Some(3)
    hw.update
    expect(List(Some(15), Some(5), None, Some(3))) { hw.take(5) }
    expect(List(None, Some(3))) { hw.take(2) }
    expect(List(Some(3))) { hw.take(1) }
    expect(Nil) { hw.take(0) }
    expect(Nil) { hw.take(-1) }
  }

  test("takeSet") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    expect(Nil) { hw.takeSet(5) }
    expect(Nil) { hw.takeSet(2) }
    expect(Nil) { hw.takeSet(1) }
    expect(Nil) { hw.takeSet(0) }
    expect(Nil) { hw.takeSet(-1) }
    value = None
    hw.update
    expect(Nil) { hw.takeSet(5) }
    expect(Nil) { hw.takeSet(2) }
    expect(Nil) { hw.takeSet(1) }
    expect(Nil) { hw.takeSet(0) }
    expect(Nil) { hw.takeSet(-1) }
    value = Some(15)
    hw.update
    expect(List(Some(15))) { hw.takeSet(5) }
    expect(List(Some(15))) { hw.takeSet(2) }
    expect(List(Some(15))) { hw.takeSet(1) }
    expect(Nil) { hw.takeSet(0) }
    expect(Nil) { hw.takeSet(-1) }
    value = None
    hw.update
    expect(List(Some(15))) { hw.takeSet(5) }
    expect(List(Some(15))) { hw.takeSet(2) }
    expect(List(Some(15))) { hw.takeSet(1) }
    expect(Nil) { hw.takeSet(0) }
    expect(Nil) { hw.takeSet(-1) }
    value = Some(5)
    hw.update
    expect(List(Some(15), Some(5))) { hw.takeSet(5) }
    expect(List(Some(15), Some(5))) { hw.takeSet(2) }
    expect(List(Some(5))) { hw.takeSet(1) }
    expect(Nil) { hw.takeSet(0) }
    expect(Nil) { hw.takeSet(-1) }
    value = None
    hw.update
    expect(List(Some(15), Some(5))) { hw.takeSet(5) }
    expect(List(Some(15), Some(5))) { hw.takeSet(2) }
    expect(List(Some(5))) { hw.takeSet(1) }
    expect(Nil) { hw.takeSet(0) }
    expect(Nil) { hw.takeSet(-1) }
    value = Some(3)
    hw.update
    expect(List(Some(15), Some(5), Some(3))) { hw.takeSet(5) }
    expect(List(Some(5), Some(3))) { hw.takeSet(2) }
    expect(List(Some(3))) { hw.takeSet(1) }
    expect(Nil) { hw.takeSet(0) }
    expect(Nil) { hw.takeSet(-1) }
  }

  test("truncate") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value }, { v: Option[Int] => v.toString })
    hw.truncate(0)
    expect(Nil) { hw.values }
    value = Some(5)
    hw.update
    value = None
    hw.update
    value = Some(7)
    hw.update
    value = Some(8)
    hw.update
    expect(List(Some(5), None, Some(7), Some(8))) { hw.values }
    hw.truncate(5)
    expect(List(Some(5), None, Some(7), Some(8))) { hw.values }
    hw.truncate(4)
    expect(List(Some(5), None, Some(7), Some(8))) { hw.values }
    hw.truncate(3)
    expect(List(None, Some(7), Some(8))) { hw.values }
    hw.truncate(1)
    expect(List(Some(8))) { hw.values }
    hw.truncate(0)
    expect(Nil) { hw.values }
  }

  test("valuesToStrings") {
    var value: Option[Int] = None
    val hw = new HistoricalValues({ () => value },
      { v: Option[Int] => v.map("#"+_.toString).getOrElse("-") })
    expect(Nil) { hw.valuesToStrings }
    value = Some(5)
    hw.update
    value = None
    hw.update
    value = Some(7)
    hw.update
    value = Some(8)
    hw.update
    expect(List("#5", "-", "#7", "#8")) { hw.valuesToStrings }
  }
}
