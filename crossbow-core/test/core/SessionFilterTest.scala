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

class SessionFilterTest extends FunSuite {
  test("SessionFilter") {
    val l = new DataListener {
      var lastData: Option[Data] = None
      def dependencies = Empty
      def receive = {
        case data => lastData = Some(data)
      }
    }
    val f = new SessionFilter()
    f.add(l)
    expect(None) { l.lastData }

    f.send(EmptyData)
    expect(None) { l.lastData }

    val so = SessionOpen(new DateTime)
    f.send(so)
    expect(Some(so)) { l.lastData }

    f.send(EmptyData)
    expect(Some(EmptyData)) { l.lastData }

    val sc = SessionClose(new DateTime)
    f.send(sc)
    expect(Some(sc)) { l.lastData }

    f.send(EmptyData)
    expect(Some(sc)) { l.lastData }
  }
}
