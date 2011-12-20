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

import org.joda.time.DateTime
import org.scalatest.FunSuite

class IndicatorListTest  extends FunSuite {
  test("dependencies") {
    class Dummy extends Indicator[Int] {
      def name = "D"
      def dependencies = Empty
      def calculate = Empty
    }
    expect(Set()) {
      val target1 = new Dummy
      val target2 = new Dummy
      val list = new IndicatorList(target1, target2)
      list.dependencies
    }
  }

  test("shallow") {
    class Dummy(deps: Dummy*) extends Indicator[Int] {
      def name = "D"
      def dependencies = deps.toSet
      def calculate = Empty
    }
    val g = new Dummy()
    val h = new Dummy()
    val i = new Dummy()
    val d = new Dummy(g)
    val e = new Dummy(h)
    val f = new Dummy(i)
    val a = new Dummy(d)
    val b = new Dummy(d, e, f)
    val c = new Dummy(i)
    expect(Nil) { (new IndicatorList()).shallow }
    expect(List(g)) { (new IndicatorList(g)).shallow }
    expect(List(g, c)) { (new IndicatorList(g, c)).shallow }
    expect(List(g, c, a)) { (new IndicatorList(g, c, a)).shallow }
    expect(List(g, c, a, b)) { (new IndicatorList(g, c, a, b)).shallow }
  }

  test("deep") {
    class Dummy(deps: Dummy*) extends Indicator[Int] {
      def name = "D"
      def dependencies = deps.toSet
      def calculate = Empty
    }
    val g = new Dummy()
    val h = new Dummy()
    val i = new Dummy()
    val d = new Dummy(g)
    val e = new Dummy(h)
    val f = new Dummy(i)
    val a = new Dummy(d)
    val b = new Dummy(d, e, f)
    val c = new Dummy(i)
    expect(Nil) { (new IndicatorList()).deep }
    expect(List(g)) { (new IndicatorList(g)).deep }
    expect(List(g, i, c)) { (new IndicatorList(g, c)).deep }
    expect(List(g, i, c, d, a)) { (new IndicatorList(g, c, a)).deep }
    expect(List(g, i, c, d, a, h, e, f, b)) { (new IndicatorList(g, c, a, b)).deep }
  }

  test("deepWithHistory") {
    class Dummy(deps: Dummy*) extends Indicator[Int] {
      def name = "D"
      def dependencies = deps.toSet
      def calculate = Empty
    }
    val g = new Dummy()
    val h = new Dummy()
    val i = new Dummy() with History
    val d = new Dummy(g) with History
    val e = new Dummy(h)
    val f = new Dummy(i) with History
    val a = new Dummy(d)
    val b = new Dummy(d, e, f)
    val c = new Dummy(i)
    expect(Nil) { (new IndicatorList()).deepWithHistory }
    expect(Nil) { (new IndicatorList(g)).deepWithHistory }
    expect(List(i)) { (new IndicatorList(g, c)).deepWithHistory }
    expect(List(i, d)) { (new IndicatorList(g, c, a)).deepWithHistory }
    expect(List(i, d, f)) { (new IndicatorList(g, c, a, b)).deepWithHistory }
  }

  test("data forwarding") {
    var counter = 0
    class UpdateOrder(val dependencies: Set[Indicator[_]] = Set.empty) extends Indicator[Double] {
      def name = "D"
      def calculate = {
        case EmptyMessage =>
          counter += 1
          counter - 1
      }
      override def toString = name
    }
    val g = new UpdateOrder()
    val h = new UpdateOrder()
    val i = new UpdateOrder(Set())
    val d = new UpdateOrder(Set(g))
    val e = new UpdateOrder(Set(h))
    val f = new UpdateOrder(Set(i))
    val a = new UpdateOrder(Set(d))
    val b = new UpdateOrder(Set(d, e, f))
    val c = new UpdateOrder(Set(i))
    val lst = new IndicatorList(g, c, a, b)
    lst.send(EmptyMessage)
    expect(0) { g.value }
    expect(1) { i.value }
    expect(2) { c.value }
    expect(3) { d.value }
    expect(4) { a.value }
    expect(5) { h.value }
    expect(6) { e.value }
    expect(7) { f.value }
    expect(8) { b.value }
  }

  test("data forwarding - BarClose") {
    var counter = 0
    class UpdateOrder(val dependencies: Set[Indicator[_]] = Set.empty) extends Indicator[Double] {
      def name = "D"
      def calculate = {
        case _: BarClose =>
          counter += 1
          counter - 1
      }
      override def toString = name
    }
    val g = new UpdateOrder()
    val h = new UpdateOrder()
    val i = new UpdateOrder(Set())
    val d = new UpdateOrder(Set(g))
    val e = new UpdateOrder(Set(h))
    val f = new UpdateOrder(Set(i))
    val a = new UpdateOrder(Set(d))
    val b = new UpdateOrder(Set(d, e, f))
    val c = new UpdateOrder(Set(i))
    val lst = new IndicatorList(g, c, a, b)
    lst.send(new BarClose(new DateTime))
    expect(0) { g.value }
    expect(1) { i.value }
    expect(2) { c.value }
    expect(3) { d.value }
    expect(4) { a.value }
    expect(5) { h.value }
    expect(6) { e.value }
    expect(7) { f.value }
    expect(8) { b.value }
  }

  test("collecting history") {
    case class DummyData(val value: Int) extends Data { def marketTime = DateTime.now }
    class HistoryIndicator extends Indicator[Int] {
      def name = "HI"
      def dependencies = Empty
      var lastData: Option[Data] = None
      def calculate = {
        case DummyData(dd) => dd
        case d: Data => lastData = Some(d); optionalValue
      }
    }
    val ih = new HistoryIndicator with History
    val i = new HistoryIndicator
    val l = new IndicatorList(ih, i)

    expect(Nil) { ih.history.values }
    expect(None) { i.lastData }
    expect(None) { ih.lastData }
    expect(None) { i.optionalValue }
    expect(None) { ih.optionalValue }

    val bc1 = BarClose(new DateTime(1000))
    l.send(bc1)
    expect(List(None)) { ih.history.values }
    expect(Some(bc1)) { i.lastData }
    expect(Some(bc1)) { ih.lastData }
    expect(None) { i.optionalValue }
    expect(None) { ih.optionalValue }

    l.send(DummyData(8))
    expect(List(None)) { ih.history.values }
    expect(Some(bc1)) { i.lastData }
    expect(Some(bc1)) { ih.lastData }
    expect(Some(8)) { i.optionalValue }
    expect(Some(8)) { ih.optionalValue }

    val bc2 = BarClose(new DateTime(2000))
    l.send(bc2)
    expect(List(None, Some(8))) { ih.history.values }
    expect(Some(bc2)) { i.lastData }
    expect(Some(bc2)) { ih.lastData }
    expect(Some(8)) { i.optionalValue }
    expect(Some(8)) { ih.optionalValue }
  }

  test("collecting history - update order at BarClose") {
    class Dummy extends Indicator[DateTime] {
      def name = "D"
      def dependencies = Empty
      def calculate = {
        case bc: BarClose => bc.marketTime
      }
    }
    val i = new Dummy with History
    val l = new IndicatorList(i)
    val t = new DateTime(2000)
    l.send(BarClose(t))
    expect(Some(t)) { i() }
    expect(Some(t)) { i.history.last }
  }

  test("maxRequiredHistory") {
    class Dummy(rh: Int) extends Indicator[Int] {
      def name = "D"
      def dependencies = Empty
      def calculate = Empty
      override def requiredHistory = rh
    }
    expect(8) {
      val l = new IndicatorList(new Dummy(5), new Dummy(-1), new Dummy(0), new Dummy(8))
      l.maxRequiredHistory
    }
    expect(0, "negative values") {
      val l = new IndicatorList(new Dummy(-5), new Dummy(-1))
      l.maxRequiredHistory
    }
    expect(5, "one indicator") {
      val l = new IndicatorList(new Dummy(5))
      l.maxRequiredHistory
    }
    expect(0, "empty list") {
      val l = new IndicatorList()
      l.maxRequiredHistory
    }
  }

  test("truncateHistory") {
    class Dummy(rh: Int) extends Indicator[Int] {
      def name = "D"
      def dependencies = Empty
      def calculate = Empty
      override def requiredHistory = rh
    }
    val i1 = new Dummy(0) with History
    val i2 = new Dummy(1) with History
    val i3 = new Dummy(3) with History
    val l = new IndicatorList(i1, i2, i3)
    i1.set(11)
    i2.set(21)
    l.send(BarClose(new DateTime))
    i1.set(12)
    i2.set(22)
    l.send(BarClose(new DateTime))
    i1.set(13)
    i2.unset()
    i3.set(33)
    l.send(BarClose(new DateTime))
    i1.set(14)
    i3.set(34)
    l.send(BarClose(new DateTime))
    i1.set(15)
    i2.set(25)
    i3.set(35)
    l.send(BarClose(new DateTime))
    i1.set(16)
    i2.set(26)
    i3.set(36)
    l.send(BarClose(new DateTime))

    // Test before truncating
    expect(List(Some(11), Some(12), Some(13), Some(14), Some(15), Some(16))) { i1.history.values }
    expect(List(Some(21), Some(22), None, None, Some(25), Some(26))) { i2.history.values }
    expect(List(None, None, Some(33), Some(34), Some(35), Some(36))) { i3.history.values }

    // Leave more than available history
    l.truncateHistory(10)
    expect(List(Some(11), Some(12), Some(13), Some(14), Some(15), Some(16))) { i1.history.values }
    expect(List(Some(21), Some(22), None, None, Some(25), Some(26))) { i2.history.values }
    expect(List(None, None, Some(33), Some(34), Some(35), Some(36))) { i3.history.values }

    // Leave all available history
    l.truncateHistory(6)
    expect(List(Some(11), Some(12), Some(13), Some(14), Some(15), Some(16))) { i1.history.values }
    expect(List(Some(21), Some(22), None, None, Some(25), Some(26))) { i2.history.values }
    expect(List(None, None, Some(33), Some(34), Some(35), Some(36))) { i3.history.values }

    // Drop 2 oldest values
    l.truncateHistory(4)
    expect(List(Some(13), Some(14), Some(15), Some(16))) { i1.history.values }
    expect(List(None, None, Some(25), Some(26))) { i2.history.values }
    expect(List(Some(33), Some(34), Some(35), Some(36))) { i3.history.values }

    // Trop exactly as maxRequiredHistory
    l.truncateHistory(3)
    expect(List(Some(14), Some(15), Some(16))) { i1.history.values }
    expect(List(None, Some(25), Some(26))) { i2.history.values }
    expect(List(Some(34), Some(35), Some(36))) { i3.history.values }

    // Try to drop less than maxRequiredHistory
    l.truncateHistory(2)
    expect(List(Some(14), Some(15), Some(16))) { i1.history.values }
    expect(List(None, Some(25), Some(26))) { i2.history.values }
    expect(List(Some(34), Some(35), Some(36))) { i3.history.values }

    // Try to drop less than maxRequiredHistory
    l.truncateHistory(0)
    expect(List(Some(14), Some(15), Some(16))) { i1.history.values }
    expect(List(None, Some(25), Some(26))) { i2.history.values }
    expect(List(Some(34), Some(35), Some(36))) { i3.history.values }

    // Try negative value
    l.truncateHistory(-1)
    expect(List(Some(14), Some(15), Some(16))) { i1.history.values }
    expect(List(None, Some(25), Some(26))) { i2.history.values }
    expect(List(Some(34), Some(35), Some(36))) { i3.history.values }
  }

  test("truncateHistory - all indicators without history") {
    class Dummy(rh: Int) extends Indicator[Int] {
      def name = "D"
      def dependencies = Empty
      def calculate = Empty
      override def requiredHistory = rh
    }
    // Indicators without history
    val l = new IndicatorList(new Dummy(0), new Dummy(3), new Dummy(1))
    l.truncateHistory(5)
  }

  test("indicator creation message") {
    class LastMessage extends Indicator[Message] {
      def name = "LastMessage"
      def dependencies = Empty
      def calculate = {
        case d => d
      }
    }
    val target1 = new LastMessage
    val target2 = new LastMessage
    val target3 = new LastMessage
    val list = new IndicatorList(target1, target2)
    expect(Some(IndicatorCreated)) { target1() }
    expect(Some(IndicatorCreated)) { target2() }
    expect(None) { target3() }
  }
}
