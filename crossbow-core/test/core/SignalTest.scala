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

class SignalTest extends FunSuite {
  test("Signal") {
    import Direction._

    val s = new Signal { def name = "s1"; def dependencies = Empty; def calculate = Empty }
    expect(None) { s() }
    assert { !s.isLong }
    assert { !s.isShort }
    assert { s.isFlat }

    s.set(Long)
    expect(Long) { s.value }
    assert { s.isLong }
    assert { !s.isShort }
    assert { !s.isFlat }

    s.set(Short)
    expect(Short) { s.value }
    assert { !s.isLong }
    assert { s.isShort }
    assert { !s.isFlat }

    s.unset()
    expect(None) { s() }
    assert { !s.isLong }
    assert { !s.isShort }
    assert { s.isFlat }
  }
  test("Conversion to string") {
    val s1 = new Signal { def name = "S1"; def dependencies = Empty; def calculate = Empty }
    expect("Flat") { s1.valueToString }
    expect("S1: Flat") { s1.toString }
    s1.set(Direction.Long)
    expect("Long") { s1.valueToString }
    expect("S1: Long") { s1.toString }
    s1.set(Direction.Short)
    expect("Short") { s1.valueToString }
    expect("S1: Short") { s1.toString }
  }
}
