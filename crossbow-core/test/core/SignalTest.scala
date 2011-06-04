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

import org.joda.time.DateTime
import org.scalatest.FunSuite

class SignalTest  extends FunSuite {
  test("Signal") {
    import Direction._

    class MySignal extends Signal {
      def name = "s1"
      def dependencies = Empty
      def calculate = Empty
    }

    val s = new MySignal
    expect(None) { s() }
    assert { !s.isLong }
    assert { !s.isShort }

    s.set(Long)
    expect(Long) { s.value }
    assert { s.isLong }
    assert { !s.isShort }

    s.set(Short)
    expect(Short) { s.value }
    assert { !s.isLong }
    assert { s.isShort }

    s.unset()
    expect(None) { s() }
    assert { !s.isLong }
    assert { !s.isShort }
  }
}
