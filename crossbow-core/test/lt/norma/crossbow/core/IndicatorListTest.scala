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

import lt.norma.crossbow.core._
import lt.norma.crossbow.indicators._
import lt.norma.crossbow.messages.{ Message, Data, EmptyMessage, BarClose }
import org.joda.time.DateTime
import org.scalatest.FunSuite

class IndicatorListTest extends FunSuite {
  test("dependencies") {
    class DemoIndicator extends Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None
    }
    val target1 = new DemoIndicator
    val target2 = new DemoIndicator
    val list = new IndicatorList(target1, target2)
    expect(Set()) {
      list.dependencies
    }
  }

  test("shallow") {
    class DemoIndicator(deps: Indicator[_]*) extends Indicator[Int] {
      def name = ""

      def dependencies = deps.toSet

      def optionalValue = None
    }
    val g = new DemoIndicator()
    val h = new DemoIndicator()
    val i = new DemoIndicator()
    val d = new DemoIndicator(g)
    val e = new DemoIndicator(h)
    val f = new DemoIndicator(i)
    val a = new DemoIndicator(d)
    val b = new DemoIndicator(d, e, f)
    val c = new DemoIndicator(i)
    expect(Nil) {
      (new IndicatorList()).shallow
    }
    expect(List(g)) {
      (new IndicatorList(g)).shallow
    }
    expect(List(g, c)) {
      (new IndicatorList(g, c)).shallow
    }
    expect(List(g, c, a)) {
      (new IndicatorList(g, c, a)).shallow
    }
    expect(List(g, c, a, b)) {
      (new IndicatorList(g, c, a, b)).shallow
    }
  }

  test("deep") {
    class DemoIndicator(deps: Indicator[_]*) extends Indicator[Int] {
      def name = ""

      def dependencies = deps.toSet

      def optionalValue = None
    }
    val g = new DemoIndicator()
    val h = new DemoIndicator()
    val i = new DemoIndicator()
    val d = new DemoIndicator(g)
    val e = new DemoIndicator(h)
    val f = new DemoIndicator(i)
    val a = new DemoIndicator(d)
    val b = new DemoIndicator(d, e, f)
    val c = new DemoIndicator(i)
    expect(Nil) {
      (new IndicatorList()).deep
    }
    expect(List(g)) {
      (new IndicatorList(g)).deep
    }
    expect(List(g, i, c)) {
      (new IndicatorList(g, c)).deep
    }
    expect(List(g, i, c, d, a)) {
      (new IndicatorList(g, c, a)).deep
    }
    expect(List(g, i, c, d, a, h, e, f, b)) {
      (new IndicatorList(g, c, a, b)).deep
    }
  }

  test("deepWithHistory") {
    class DemoIndicator(deps: Indicator[_]*) extends Indicator[Int] {
      def name = ""

      def dependencies = deps.toSet

      def optionalValue = None
    }
    val g = new DemoIndicator()
    val h = new DemoIndicator()
    val i = new DemoIndicator() with History
    val d = new DemoIndicator(g) with History
    val e = new DemoIndicator(h)
    val f = new DemoIndicator(i) with History
    val a = new DemoIndicator(d)
    val b = new DemoIndicator(d, e, f)
    val c = new DemoIndicator(i)
    expect(Nil) {
      (new IndicatorList()).deepWithHistory
    }
    expect(Nil) {
      (new IndicatorList(g)).deepWithHistory
    }
    expect(List(i)) {
      (new IndicatorList(g, c)).deepWithHistory
    }
    expect(List(i, d)) {
      (new IndicatorList(g, c, a)).deepWithHistory
    }
    expect(List(i, d, f)) {
      (new IndicatorList(g, c, a, b)).deepWithHistory
    }
  }

  test("deepWithBasicListener") {
    class DemoIndicator(deps: Indicator[_]*) extends Indicator[Int] {
      def name = ""

      def dependencies = deps.toSet

      def optionalValue = None
    }
    class DemoListenerIndicator(deps: Indicator[_]*) extends ListenerIndicator[Int] {
      def name = ""

      def dependencies = deps.toSet

      def receive = Empty
    }
    val g = new DemoIndicator()
    val h = new DemoIndicator()
    val i = new DemoListenerIndicator()
    val d = new DemoListenerIndicator(g)
    val e = new DemoIndicator(h)
    val f = new DemoListenerIndicator(i)
    val a = new DemoIndicator(d)
    val b = new DemoIndicator(d, e, f)
    val c = new DemoIndicator(i)
    expect(Nil) {
      (new IndicatorList()).deepWithBasicListener
    }
    expect(Nil) {
      (new IndicatorList(g)).deepWithBasicListener
    }
    expect(List(i)) {
      (new IndicatorList(g, c)).deepWithBasicListener
    }
    expect(List(i, d)) {
      (new IndicatorList(g, c, a)).deepWithBasicListener
    }
    expect(List(i, d, f)) {
      (new IndicatorList(g, c, a, b)).deepWithBasicListener
    }
  }

  test("data forwarding") {
    var counter = 0
    class UpdateOrder(deps: Indicator[_]*) extends ListenerIndicator[Int] {
      def name = ""

      def dependencies = deps.toSet

      def receive = {
        case EmptyMessage =>
          counter += 1
          set(counter - 1)
      }

      override def toString = name
    }
    val g = new UpdateOrder()
    val h = new UpdateOrder()
    val i = new UpdateOrder()
    val d = new UpdateOrder(g)
    val e = new UpdateOrder(h)
    val f = new UpdateOrder(i)
    val a = new UpdateOrder(d)
    val b = new UpdateOrder(d, e, f)
    val c = new UpdateOrder(i)
    val lst = new IndicatorList(g, c, a, b)
    lst.send(EmptyMessage)
    expect(0) {
      g.value
    }
    expect(1) {
      i.value
    }
    expect(2) {
      c.value
    }
    expect(3) {
      d.value
    }
    expect(4) {
      a.value
    }
    expect(5) {
      h.value
    }
    expect(6) {
      e.value
    }
    expect(7) {
      f.value
    }
    expect(8) {
      b.value
    }
  }

  test("data forwarding - BarClose") {
    var counter = 0
    class UpdateOrder(deps: Indicator[_]*) extends ListenerIndicator[Int] {
      def name = ""

      def dependencies = deps.toSet

      def receive = {
        case _: BarClose =>
          counter += 1
          set(counter - 1)
      }

      override def toString = name
    }
    val g = new UpdateOrder()
    val h = new UpdateOrder()
    val i = new UpdateOrder()
    val d = new UpdateOrder(g)
    val e = new UpdateOrder(h)
    val f = new UpdateOrder(i)
    val a = new UpdateOrder(d)
    val b = new UpdateOrder(d, e, f)
    val c = new UpdateOrder(i)
    val lst = new IndicatorList(g, c, a, b)
    lst.send(new BarClose(new DateTime))
    expect(0) {
      g.value
    }
    expect(1) {
      i.value
    }
    expect(2) {
      c.value
    }
    expect(3) {
      d.value
    }
    expect(4) {
      a.value
    }
    expect(5) {
      h.value
    }
    expect(6) {
      e.value
    }
    expect(7) {
      f.value
    }
    expect(8) {
      b.value
    }
  }

  test("collecting history") {
    case class DummyData(value: Int) extends Data {
      def marketTime = DateTime.now
    }
    val li = new ListenerIndicator[Int] with History {
      def name = ""

      def dependencies = Empty

      def receive = {
        case DummyData(dd) => set(dd)
      }
    }
    val fi = new FunctionalIndicator[Int] with History {
      def name = ""

      def dependencies = Empty

      var i = 0

      def calculate = {
        i += 1; Some(i - 1)
      }
    }
    val list = new IndicatorList(li, fi)
    expect(Nil) {
      li.history.values
    }
    expect(None) {
      li()
    }
    expect(Nil) {
      fi.history.values
    }
    expect(Some(0)) {
      fi()
    }

    list.send(BarClose(new DateTime(0)))
    expect(List(None)) {
      li.history.values
    }
    expect(None) {
      li()
    }
    expect(List(Some(1))) {
      fi.history.values
    }
    expect(Some(2)) {
      fi()
    }

    list.send(DummyData(8))
    list.send(BarClose(new DateTime(1000)))
    expect(List(None, Some(8))) {
      li.history.values
    }
    expect(Some(8)) {
      li()
    }
    expect(List(Some(1), Some(3))) {
      fi.history.values
    }
    expect(Some(4)) {
      fi()
    }
  }

  test("maxRequiredHistory") {
    class DemoIndicator(rh: Int) extends Indicator[Int] {
      def name = ""

      def dependencies = Empty

      def optionalValue = None

      override def requiredHistory = rh
    }
    expect(8) {
      val l = new IndicatorList(new DemoIndicator(5), new DemoIndicator(-1), new DemoIndicator(0),
        new DemoIndicator(8))
      l.maxRequiredHistory
    }
    expect(0, "negative values") {
      val l = new IndicatorList(new DemoIndicator(-5), new DemoIndicator(-1))
      l.maxRequiredHistory
    }
    expect(5, "one indicator") {
      val l = new IndicatorList(new DemoIndicator(5))
      l.maxRequiredHistory
    }
    expect(0, "empty list") {
      val l = new IndicatorList()
      l.maxRequiredHistory
    }
  }

  test("collecting history - update order at BarClose") {
    var order = 0
    class DemoIndicator extends ListenerIndicator[Int] {
      def name = ""

      def dependencies = Empty

      def receive = {
        case bc: BarClose =>
          order += 1
          set(order)
      }

      set(0)
    }
    val indicator = new DemoIndicator with History
    val list = new IndicatorList(indicator)

    list.send(BarClose(new DateTime()))
    expect(Some(1)) {
      indicator()
    }
    expect(Some(1)) {
      indicator.history.last
    }
  }

  test("truncateHistory") {
    class DemoIndicator(rh: Int) extends MutableIndicator[Int] {
      def name = ""

      def dependencies = Empty

      override def requiredHistory = rh
    }
    val i1 = new DemoIndicator(0) with History
    val i2 = new DemoIndicator(1) with History
    val i3 = new DemoIndicator(3) with History
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
    expect(List(Some(11), Some(12), Some(13), Some(14), Some(15), Some(16))) {
      i1.history.values
    }
    expect(List(Some(21), Some(22), None, None, Some(25), Some(26))) {
      i2.history.values
    }
    expect(List(None, None, Some(33), Some(34), Some(35), Some(36))) {
      i3.history.values
    }

    // Leave more than available history
    l.truncateHistory(10)
    expect(List(Some(11), Some(12), Some(13), Some(14), Some(15), Some(16))) {
      i1.history.values
    }
    expect(List(Some(21), Some(22), None, None, Some(25), Some(26))) {
      i2.history.values
    }
    expect(List(None, None, Some(33), Some(34), Some(35), Some(36))) {
      i3.history.values
    }

    // Leave all available history
    l.truncateHistory(6)
    expect(List(Some(11), Some(12), Some(13), Some(14), Some(15), Some(16))) {
      i1.history.values
    }
    expect(List(Some(21), Some(22), None, None, Some(25), Some(26))) {
      i2.history.values
    }
    expect(List(None, None, Some(33), Some(34), Some(35), Some(36))) {
      i3.history.values
    }

    // Drop 2 oldest values
    l.truncateHistory(4)
    expect(List(Some(13), Some(14), Some(15), Some(16))) {
      i1.history.values
    }
    expect(List(None, None, Some(25), Some(26))) {
      i2.history.values
    }
    expect(List(Some(33), Some(34), Some(35), Some(36))) {
      i3.history.values
    }

    // Drop exactly as maxRequiredHistory
    l.truncateHistory(3)
    expect(List(Some(14), Some(15), Some(16))) {
      i1.history.values
    }
    expect(List(None, Some(25), Some(26))) {
      i2.history.values
    }
    expect(List(Some(34), Some(35), Some(36))) {
      i3.history.values
    }

    // Try to drop less than maxRequiredHistory
    l.truncateHistory(2)
    expect(List(Some(14), Some(15), Some(16))) {
      i1.history.values
    }
    expect(List(None, Some(25), Some(26))) {
      i2.history.values
    }
    expect(List(Some(34), Some(35), Some(36))) {
      i3.history.values
    }

    // Try to drop less than maxRequiredHistory
    l.truncateHistory(0)
    expect(List(Some(14), Some(15), Some(16))) {
      i1.history.values
    }
    expect(List(None, Some(25), Some(26))) {
      i2.history.values
    }
    expect(List(Some(34), Some(35), Some(36))) {
      i3.history.values
    }

    // Try negative value
    l.truncateHistory(-1)
    expect(List(Some(14), Some(15), Some(16))) {
      i1.history.values
    }
    expect(List(None, Some(25), Some(26))) {
      i2.history.values
    }
    expect(List(Some(34), Some(35), Some(36))) {
      i3.history.values
    }
  }

  test("truncateHistory - all indicators without history") {
    class DemoIndicator(rh: Int) extends MutableIndicator[Int] {
      def name = ""

      def dependencies = Empty

      override def requiredHistory = rh
    }
    // Indicators without history
    val l = new IndicatorList(new DemoIndicator(0), new DemoIndicator(3), new DemoIndicator(1))
    l.truncateHistory(5)
  }

  test("indicator creation message") {
    class LastMessage extends ListenerIndicator[Message] {
      def name = ""

      def dependencies = Empty

      def receive = {
        case m => set(m)
      }
    }
    val target1 = new LastMessage
    val target2 = new LastMessage
    val target3 = new LastMessage
    val list = new IndicatorList(target1, target2)
    expect(Some(IndicatorList.IndicatorCreated)) {
      target1()
    }
    expect(Some(IndicatorList.IndicatorCreated)) {
      target2()
    }
    expect(None) {
      target3()
    }
  }
}
