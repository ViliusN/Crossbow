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

class DataListenerTest extends FunSuite {
  test("Inline data listener") {
    var lastData: Option[Message] = None
    val dp = new DataProvider { }
    val l = Listener { case d => lastData = Some(d) }
    dp.add(l)
    expect(None) { lastData }
    dp.dispatch(DummyData(55))
    expect(Some(DummyData(55))) { lastData }
    dp.remove(l)
    dp.dispatch(DummyData(66))
    expect(Some(DummyData(55))) { lastData }
  }

  test("Empty receiver") {
    class L extends Listener {
      def dependencies = Empty
      def receive = Empty
    }

    val l = new L
    l.send(new Data { })
  }

  case class DummyData(value: Int) extends Data
  case class DummyData2(value: Int) extends Data
}
