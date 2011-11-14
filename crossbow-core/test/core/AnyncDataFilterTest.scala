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

class AnyncDataFilterTest extends FunSuite {
  test("Asynchronous data filter") {
    var counter = 1
    case class D(v: Int) extends Data { def time = DateTime.now }
    class LFast extends Listener {
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
    class LSlow extends Listener {
      def dependencies = Empty
      var lastData: Option[Message] = None
      var order = 0
      def receive = {
        case d =>
          Thread.sleep(200);
          lastData = Some(d)
          order = counter
          counter += 1
      }
    }

    val fast = new LFast
    val slow = new LSlow
    val async = new AsyncDataFilter
    async.add(slow)

    val d1 = D(1)
    slow.send(d1)
    fast.send(d1)
    expect(d1) { slow.lastData.get }
    expect(1) { slow.order }
    expect(d1) { fast.lastData.get }
    expect(2) { fast.order }

    val d2 = D(2)
    async.send(d2)
    fast.send(d2)
    Thread.sleep(500)
    expect(d2) { slow.lastData.get }
    expect(4) { slow.order }
    expect(d2) { fast.lastData.get }
    expect(3) { fast.order }
  }
}
