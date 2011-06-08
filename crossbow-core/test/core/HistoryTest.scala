/*
 * Copyright 2011 Vilius Normantas <code@norma.lt>
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

class HistoryTest extends FunSuite {
  class Dummy extends Indicator[Int] with HistoryHolder[Int] {
    def name = "D"
    def dependencies = Empty
    def calculate = Empty
  }

  def updateHistory(indicator: Indicator[_]) = indicator match {
    case h: History => h.update()
    case _ =>
  }

  test("History") {
    val i = new Dummy
    val ih = new Dummy with History

    expect(Nil) { ih.history }
    expect(None) { ih.last }

    updateHistory(ih)
    updateHistory(i)
    expect(1) { ih.history.size }
    expect(None) { ih.last }

    ih.set(5)
    expect(None) { ih.last }
    expect(List(None)) { ih.history }
    ih.update()
    expect(List(None, Some(5))) { ih.history }
    expect(Some(5)) { ih.last }

    ih.update()
    expect(List(None, Some(5), Some(5))) { ih.history }
    expect(5) { ih.last.get }
  }

  test("lastSet method") {
    val ih = new Dummy with History
    expect(None) { ih.lastSet }

    updateHistory(ih)
    expect(None) { ih.lastSet }

    ih.set(5)
    updateHistory(ih)
    expect(Some(5)) { ih.lastSet }
    ih.set(6)
    updateHistory(ih)
    expect(Some(6)) { ih.lastSet }

    ih.unset()
    updateHistory(ih)
    expect(Some(6)) { ih.lastSet }
    updateHistory(ih)
    expect(Some(6)) { ih.lastSet }
    updateHistory(ih)
    expect(Some(6)) { ih.lastSet }

    ih.set(7)
    updateHistory(ih)
    expect(Some(7)) { ih.lastSet }

    ih.unset()
    updateHistory(ih)
    expect(Some(7)) { ih.lastSet }
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
    expect(None) { ih.last }

    val v = new A(8.2)
    ih.set(v)
    expect(None) { ih.last }
    expect(List(None)) { ih.history }
    ih.update()
    expect(List(None, Some(v))) { ih.history }
    expect(Some(v)) { ih.last }
    expect(16.4) { ih.last.get.d2 }

    ih.update()
    expect(List(None, Some(v), Some(v))) { ih.history }
    expect(Some(v)) { ih.last }
  }

  test("History values to string conversion") {
    expect("N/A,-8,9,N/A,10") {
      class MyIndicator extends Indicator[Int] with History {
        def name = "M"
        def dependencies = Empty
        def calculate = Empty
      }
      val i = new MyIndicator
      i.update()
      i.set(-8)
      i.update()
      i.set(9)
      i.update()
      i.unset()
      i.update()
      i.set(10)
      i.update()
      i.historyStrings mkString(",")
    }
    expect("[no val.]  [-8]  [9]  [no val.]  [10]") {
      class MyIndicator extends Indicator[Int] with History {
        def name = "M"
        def dependencies = Empty
        def calculate = Empty
        override def valueToString(v: Option[Int]): String =
          v map { "["+_.toString+"]" } getOrElse("[no val.]")
      }
      val i = new MyIndicator
      i.update()
      i.set(-8)
      i.update()
      i.set(9)
      i.update()
      i.unset()
      i.update()
      i.set(10)
      i.update()
      i.historyStrings mkString("  ")
    }
  }

  test("Take n last values") {
    val ih = new Dummy with History
    expect(Nil) { ih.take(10) }

    ih.update()
    expect(List(None)) { ih.take(1) }
    expect(List(None)) { ih.take(10) }
    expect(Nil) { ih.take(0) }

    ih.set(5)
    ih.update()
    expect(List(Some(5))) { ih.take(1) }
    expect(List(None, Some(5))) { ih.take(2) }
    expect(List(None, Some(5))) { ih.take(10) }

    ih.set(6)
    ih.update()
    expect(List(Some(5), Some(6))) { ih.take(2) }
    expect(List(None, Some(5), Some(6))) { ih.take(10) }

    ih.unset()
    ih.update()
    expect(List(Some(6), None)) { ih.take(2) }
    expect(List(None, Some(5), Some(6), None)) { ih.take(10) }

    ih.set(7)
    ih.update()
    expect(List(None, Some(7))) { ih.take(2) }
    expect(List(None, Some(5), Some(6), None, Some(7))) { ih.take(10) }
  }
}
