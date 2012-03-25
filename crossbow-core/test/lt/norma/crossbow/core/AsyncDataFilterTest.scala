package lt.norma.crossbow.core

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

import lt.norma.crossbow.messages._
import org.joda.time.DateTime
import org.scalatest.FunSuite
import lt.norma.crossbow.messages.{Message, Data}

class AsyncDataFilterTest extends FunSuite {
  var n = 0

  class DummyListener extends Listener {
    def dependencies = Empty

    def receive = Empty

    override val toString = {
      n += 1; "listener" + n
    }
  }

  test("add one listener by constructor") {
    val listener = new DummyListener
    val filter = new AsyncDataFilter(listener)
    expect(List(listener)) {
      filter.listeners
    }
  }

  test("add multiple listeners by constructor") {
    val listener1 = new DummyListener
    val listener2 = new DummyListener
    val listener3 = new DummyListener
    val filter = new AsyncDataFilter(listener1, listener2, listener3)
    expect(Set(listener1, listener2, listener3)) {
      filter.listeners.toSet
    }
  }

  test("add no listeners by constructor") {
    val filter = new AsyncDataFilter()
    expect(Nil) {
      filter.listeners
    }
  }

  test("send data asynchronously") {
    case class D(v: Int) extends Data {
      def marketTime = DateTime.now
    }
    var counter = 1
    val fast = new Listener {
      def dependencies = Empty

      var lastData: Option[Message] = None
      var order = 0

      def receive = {
        case d =>
          lastData = Some(d)
          order = counter
          counter += 1
      }
    }
    val slow = new Listener {
      def dependencies = Empty

      var lastData: Option[Message] = None
      var order = 0

      def receive = {
        case d =>
          Thread.sleep(100);
          lastData = Some(d)
          order = counter
          counter += 1
      }
    }
    val async = new AsyncDataFilter(slow)

    // Send message in normal order - slow listener receives data first
    val d1 = D(1)
    slow.send(d1)
    fast.send(d1)
    expect(Some(d1)) {
      slow.lastData
    }
    expect(1) {
      slow.order
    }
    expect(Some(d1)) {
      fast.lastData
    }
    expect(2) {
      fast.order
    }

    // Send message asynchronously - slow listener receives data last
    val d2 = D(2)
    async.send(d2)
    fast.send(d2)
    Thread.sleep(200)
    expect(Some(d2)) {
      slow.lastData
    }
    expect(4) {
      slow.order
    }
    expect(Some(d2)) {
      fast.lastData
    }
    expect(3) {
      fast.order
    }
  }
}
