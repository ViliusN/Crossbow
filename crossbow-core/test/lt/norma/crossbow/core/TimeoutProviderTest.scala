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

import org.joda.time.{DateTime, Duration}
import org.scalatest.FunSuite
import TimeoutProvider._
import lt.norma.crossbow.core.testutils._

class TimeoutProviderTest extends FunSuite {
  test("dependencies") {
    val provider = new TimeoutProvider()
    expect(Set.empty) {
      provider.dependencies
    }
  }

  test("timeout") {
    val provider = new TimeoutProvider()
    val start = Start(DateTime.now.plusMillis(100))
    var received = false;
    val listener = Listener {
      case Timeout(sr) =>
        approx(DateTime.now.getMillis, 10) { start.endTime.getMillis }
        assert { sr eq start }
        received = true;
      case _ =>
        fail()
    }
    provider.add(listener)
    provider.send(start)
    assert { !received }
    Thread.sleep(300)
    assert { received }
  }

  test("timeout - two requests") {
    val provider = new TimeoutProvider()
    val start1 = Start(DateTime.now.plusMillis(100))
    val start2 = Start(DateTime.now.plusMillis(200))
    var received1 = false;
    var received2 = false;
    val listener = Listener {
      case Timeout(`start1`) =>
        approx(DateTime.now.getMillis, 10) { start1.endTime.getMillis }
        received1 = true;
      case Timeout(`start2`) =>
        approx(DateTime.now.getMillis, 10) { start2.endTime.getMillis }
        received2 = true;
      case _ =>
        fail
    }
    provider.add(listener)
    provider.send(start1)
    provider.send(start2)
    assert { !received1 }
    assert { !received2 }
    Thread.sleep(120)
    assert { received1 }
    assert { !received2 }
    Thread.sleep(120)
    assert { received1 }
    assert { received2 }
  }

  test("timeout - cancel request") {
    val provider = new TimeoutProvider()
    val start = Start(DateTime.now.plusSeconds(1))
    val listener = Listener {
      case _ =>
        fail
    }
    provider.add(listener)
    provider.send(start)
    provider.send(Cancel(start))
  }

  test("timeout - cancel two requests") {
    val provider = new TimeoutProvider()
    val start1 = Start(DateTime.now.plusSeconds(1))
    val start2 = Start(DateTime.now.plusSeconds(2))
    val listener = Listener {
      case _ =>
        fail
    }
    provider.add(listener)
    provider.send(start1)
    provider.send(start2)
    provider.send(Cancel(start1))
    provider.send(Cancel(start2))
  }

  test("Start - alternative constructor") {
    val start = Start(new Duration(1000))
    approx(DateTime.now.getMillis + 1000, 100) {
      start.endTime.getMillis
    }
  }
}
