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

package lt.norma.crossbow.core.indicators

import lt.norma.crossbow.core._
import lt.norma.crossbow.core.messages._
import org.scalatest.FunSuite

class MostSignalsTest extends FunSuite {
  import Direction._
  test("MostSignals") {
    val s1 = new MutableSignal { def name = "S1"; def dependencies = Empty }
    val s2 = new MutableSignal { def name = "S2"; def dependencies = Empty }
    val s3 = new MutableSignal { def name = "S3"; def dependencies = Empty }
    val s4 = new MutableSignal { def name = "S4"; def dependencies = Empty }
    val s = new MostSignals(s1, s2, s3, s4)
    val l = new IndicatorList(s)
    expect("MostSignals(S1; S2; S3; S4)") { s.name }
    expect(Set(s1, s2, s3, s4)) { s.dependencies }
    assert { s.isFlat }

    l.send(EmptyMessage)
    assert { s.isFlat }
    s1.set(Long)
    l.send(EmptyMessage)
    assert { s.isFlat }
    s2.set(Long)
    l.send(EmptyMessage)
    assert { s.isFlat }
    s3.set(Long)
    l.send(EmptyMessage)
    assert { s.isLong }
    s4.set(Long)
    l.send(EmptyMessage)
    assert { s.isLong }
    s1.set(Short)
    l.send(EmptyMessage)
    assert { s.isLong }
    s2.set(Short)
    l.send(EmptyMessage)
    assert { s.isFlat }
    s2.unset()
    l.send(EmptyMessage)
    assert { s.isFlat }
  }
  test("one signal") {
    val s1 = new MutableSignal { def name = "S1"; def dependencies = Empty }
    val s = new MostSignals(s1)
    val l = new IndicatorList(s)
    expect("MostSignals(S1)") { s.name }
    expect(Set(s1)) { s.dependencies }
    assert { s.isFlat }

    l.send(EmptyMessage)
    assert { s.isFlat }
    s1.set(Long)
    l.send(EmptyMessage)
    assert { s.isLong }
    s1.set(Short)
    l.send(EmptyMessage)
    assert { s.isShort }
    s1.unset()
    l.send(EmptyMessage)
    assert { s.isFlat }
  }
  test("empty list of signals") {
    val s = new MostSignals()
    val l = new IndicatorList(s)
    expect("MostSignals()") { s.name }
    expect(Set.empty) { s.dependencies }
    assert { s.isFlat }
    l.send(EmptyMessage)
    assert { s.isFlat }
  }
}
