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

class IndicatorHistoryTest extends FunSuite {
  class Dummy extends Indicator[Int] {
    def name = "D"
    def dependencies = Empty
    def calculate = Empty
  }
  def updateHistory(indicator: Indicator[_]) = indicator match {
    case ih: History => ih.history.update()
    case _ =>
  }

  test("IndicatorHistory") {
    val i = new Dummy
    val ih = new Dummy with History

    expect(Nil) { ih.history.values }
    expect(None) { ih.history.last }

    updateHistory(ih)
    updateHistory(i)
    expect(1) { ih.history.size }
    expect(None) { ih.history.last }

    ih.set(5)
    expect(None) { ih.history.last }
    expect(List(None)) { ih.history.values }
    ih.history.update()
    expect(List(None, Some(5))) { ih.history.values }
    expect(Some(5)) { ih.history.last }

    ih.history.update()
    expect(List(None, Some(5), Some(5))) { ih.history.values }
    expect(5) { ih.history.last.get }
  }

  test("size and isEmpty methods") {
    val ih = new Dummy with History
    expect(0) { ih.history.size }
    assert { ih.history.isEmpty }

    updateHistory(ih)
    expect(1) { ih.history.size }
    assert { ih.history.isEmpty == false }
    updateHistory(ih)
    expect(2) { ih.history.size }
    assert { ih.history.isEmpty == false }

    ih.set(5)
    ih.history.update()
    expect(3) { ih.history.size }
    assert { ih.history.isEmpty == false }

    ih.history.update()
    expect(4) { ih.history.size }
    assert { ih.history.isEmpty == false }

    ih.unset()
    ih.history.update()
    expect(5) { ih.history.size }
    assert { ih.history.isEmpty == false }
  }

  test("lastSet method") {
    val ih = new Dummy with History
    expect(None) { ih.history.lastSet }

    updateHistory(ih)
    expect(None) { ih.history.lastSet }

    ih.set(5)
    updateHistory(ih)
    expect(Some(5)) { ih.history.lastSet }
    ih.set(6)
    updateHistory(ih)
    expect(Some(6)) { ih.history.lastSet }

    ih.unset()
    updateHistory(ih)
    expect(Some(6)) { ih.history.lastSet }
    updateHistory(ih)
    expect(Some(6)) { ih.history.lastSet }
    updateHistory(ih)
    expect(Some(6)) { ih.history.lastSet }

    ih.set(7)
    updateHistory(ih)
    expect(Some(7)) { ih.history.lastSet }

    ih.unset()
    updateHistory(ih)
    expect(Some(7)) { ih.history.lastSet }
  }

  test("History of custom types") {
    class A(val d: Double) { def d2 = d * 2 }
    class DummyA extends Indicator[A] with HistoryHolder[A] {
      def name = "DA"
      def dependencies = Empty
      def calculate = Empty
    }

    val i = new DummyA
    val ih = new DummyA with History

    assert { ih.history.isEmpty }

    updateHistory(ih)
    updateHistory(i)
    expect(1) { ih.history.size }
    expect(None) { ih.history.last }

    val v = new A(8.2)
    ih.set(v)
    expect(None) { ih.history.last }
    expect(List(None)) { ih.history.values }
    ih.history.update()
    expect(List(None, Some(v))) { ih.history.values }
    expect(Some(v)) { ih.history.last }
    expect(16.4) { ih.history.last.get.d2 }

    ih.history.update()
    expect(List(None, Some(v), Some(v))) { ih.history.values }
    expect(Some(v)) { ih.history.last }
  }

  test("History values to string conversion") {
    expect("N/A,-8,9,N/A,10") {
      class MyIndicator extends Indicator[Int] with History {
        def name = "M"
        def dependencies = Empty
        def calculate = Empty
      }
      val i = new MyIndicator
      i.history.update()
      i.set(-8)
      i.history.update()
      i.set(9)
      i.history.update()
      i.unset()
      i.history.update()
      i.set(10)
      i.history.update()
      i.history.valuesToStrings mkString(",")
    }
    expect("[no val.]  [-8]  [9]  [no val.]  [10]") {
      class MyIndicator extends Indicator[Int] with History {
        def name = "M"
        def dependencies = Empty
        def calculate = Empty
        override def valueToString(v: Int): String = "["+v+"]"
        override def valueNotSetString = "[no val.]"
      }
      val i = new MyIndicator
      i.history.update()
      i.set(-8)
      i.history.update()
      i.set(9)
      i.history.update()
      i.unset()
      i.history.update()
      i.set(10)
      i.history.update()
      i.history.valuesToStrings mkString("  ")
    }
  }

  test("Take n last values") {
    val ih = new Dummy with History
    expect(Nil) { ih.history.take(10) }

    ih.history.update()
    expect(List(None)) { ih.history.take(1) }
    expect(List(None)) { ih.history.take(10) }
    expect(Nil) { ih.history.take(0) }

    ih.set(5)
    ih.history.update()
    expect(List(Some(5))) { ih.history.take(1) }
    expect(List(None, Some(5))) { ih.history.take(2) }
    expect(List(None, Some(5))) { ih.history.take(10) }

    ih.set(6)
    ih.history.update()
    expect(List(Some(5), Some(6))) { ih.history.take(2) }
    expect(List(None, Some(5), Some(6))) { ih.history.take(10) }

    ih.unset()
    ih.history.update()
    expect(List(Some(6), None)) { ih.history.take(2) }
    expect(List(None, Some(5), Some(6), None)) { ih.history.take(10) }

    ih.set(7)
    ih.history.update()
    expect(List(None, Some(7))) { ih.history.take(2) }
    expect(List(None, Some(5), Some(6), None, Some(7))) { ih.history.take(10) }
  }

  test("Take n last set values") {
    val ih = new Dummy with History
    expect(Nil) { ih.history.takeSet(10) }

    ih.history.update()
    expect(Nil) { ih.history.takeSet(1) }
    expect(Nil) { ih.history.takeSet(10) }
    expect(Nil) { ih.history.takeSet(0) }

    ih.set(5)
    ih.history.update()
    expect(List(Some(5))) { ih.history.takeSet(1) }
    expect(List(Some(5))) { ih.history.takeSet(2) }
    expect(List(Some(5))) { ih.history.takeSet(10) }

    ih.set(6)
    ih.history.update()
    expect(List(Some(5), Some(6))) { ih.history.takeSet(2) }
    expect(List(Some(5), Some(6))) { ih.history.takeSet(10) }

    ih.unset()
    ih.history.update()
    expect(List(Some(5), Some(6))) { ih.history.takeSet(2) }
    expect(List(Some(5), Some(6))) { ih.history.takeSet(10) }

    ih.set(7)
    ih.history.update()
    expect(List(Some(6), Some(7))) { ih.history.takeSet(2) }
    expect(List(Some(5), Some(6), Some(7))) { ih.history.takeSet(10) }

    ih.set(8)
    ih.history.update()
    expect(List(Some(7), Some(8))) { ih.history.takeSet(2) }
    expect(List(Some(6), Some(7), Some(8))) { ih.history.takeSet(3) }
    expect(List(Some(5), Some(6), Some(7), Some(8))) { ih.history.takeSet(10) }
  }
}

class HistoryHolderTest extends FunSuite {
  class Dummy extends Indicator[Int] {
    def name = "D"
    def dependencies = Empty
    def calculate = Empty
  }

  test("HistoryHolder") {
    val ih = new Dummy with History
    val i = new Dummy

    assert { ih.hasHistory }
    ih.history

    assert { i.hasHistory == false }
    intercept[Exception] {  i.history }
  }
}
