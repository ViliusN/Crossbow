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
  class Dummy(deps: Dummy*) extends Indicator[Int] {
    def name = "D"
    def dependencies = deps.toSet
    def calculate = Empty
  }

  test("IndicatorList") {
    val g = new Dummy()
    val h = new Dummy()
    val i = new Dummy()
    val d = new Dummy(g)
    val e = new Dummy(h)
    val f = new Dummy(i)
    val a = new Dummy(d)
    val b = new Dummy(d, e, f)
    val c = new Dummy(i)

    expect(Nil) { (new IndicatorList()).list }
    expect(Nil) { (new IndicatorList()).deepList }

    expect(List(g)) { (new IndicatorList(g)).list }
    expect(List(g)) { (new IndicatorList(g)).deepList }

    expect(List(g, c)) { (new IndicatorList(g, c)).list }
    expect(List(g, i, c)) { (new IndicatorList(g, c)).deepList }

    expect(List(g, c, a)) { (new IndicatorList(g, c, a)).list }
    expect(List(g, i, c, d, a)) { (new IndicatorList(g, c, a)).deepList }

    expect(List(g, c, a, b)) { (new IndicatorList(g, c, a, b)).list }
    expect(List(g, i, c, d, a, h, e, f, b)) { (new IndicatorList(g, c, a, b)).deepList }
  }

  test("IndicatorList - history") {
    case class DummyData(val value: Int) extends Data
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

  test("IndicatorList - data forwarding") {
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
    val s = Stock("MSFT", Exchange.nasdaq, "USD")
    val t = Trade(s, 30.05, 100000, new DateTime(2011, 3, 4, 7, 9, 8, 155, Settings.timeZone))
    lst.send(t)

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

  test("Receiving data from provider") {
    val l1 = new Listener {
      var lastData: Option[Data] = None
      def dependencies = Set.empty
      def receive = {
        case d: Data => lastData = Some(d)
      }
    }
    val l2 = new Listener {
      var lastData: Option[Data] = None
      def dependencies = Set(l1)
      def receive = {
        case d: Data => lastData = Some(d)
      }
    }
    val i1 = new Indicator[Data] {
      def name = "I"
      def dependencies = Empty
      def calculate = {
        case d: Data => d
      }
    }
    val i2 = new Indicator[Data] {
      def name = "I"
      def dependencies = Set(i1)
      def calculate = {
        case d: Data => d
      }
    }

    val lst = new IndicatorList(i2)
    val dp = new DataProvider { }
    dp.add(l2)
    dp.add(lst)
    val data = new Data { }

    dp.dispatch(data)

    expect(data) { l1.lastData.get }
    expect(data) { l2.lastData.get }
    expect(data) { i1.value }
    expect(data) { i2.value }
  }

  object Counter { var n = 0 }
  class UpdateOrder(val dependencies: Set[Indicator[_]] = Set.empty) extends Indicator[Double] {
    def name = "D"
    def calculate = {
      case _ =>
        val n = Counter.n
        Counter.n += 1
        n
    }
    override def toString = name
  }
}
