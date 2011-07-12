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

package lt.norma.crossbow.indicators

import lt.norma.crossbow.core._
import org.scalatest.FunSuite

class AllSignalsTest extends FunSuite {
  import Direction._
  test("AllSignals") {
    val s1 = new VariableSignal { override def name = "S1" }
    val s2 = new VariableSignal { override def name = "S2" }
    val s3 = new VariableSignal { override def name = "S3" }
    val s = new AllSignals(s1, s2, s3)
    val l = new IndicatorList(s)
    expect("AllSignals(S1; S2; S3)") { s.name }
    expect(Set(s1, s2, s3)) { s.dependencies }
    assert { s.isFlat }

    l.send(EmptyData)
    assert { s.isFlat }
    s1.set(Long)
    l.send(EmptyData)
    assert { s.isFlat }
    s2.set(Long)
    l.send(EmptyData)
    assert { s.isFlat }
    s3.set(Long)
    l.send(EmptyData)
    assert { s.isLong }
    s1.set(Short)
    l.send(EmptyData)
    assert { s.isFlat }
    s2.set(Short)
    l.send(EmptyData)
    assert { s.isFlat }
    s3.set(Short)
    l.send(EmptyData)
    assert { s.isShort }
    s1.unset()
    l.send(EmptyData)
    assert { s.isFlat }
    s2.unset()
    s3.unset()
    l.send(EmptyData)
    assert { s.isFlat }
  }
  test("one signal") {
    val s1 = new Signal { def name = "S1"; def dependencies = Empty; def calculate = Empty }
    val s = new AllSignals(s1)
    val l = new IndicatorList(s)
    expect("AllSignals(S1)") { s.name }
    expect(Set(s1)) { s.dependencies }
    assert { s.isFlat }

    l.send(EmptyData)
    assert { s.isFlat }
    s1.set(Long)
    l.send(EmptyData)
    assert { s.isLong }
    s1.set(Short)
    l.send(EmptyData)
    assert { s.isShort }
    s1.unset()
    l.send(EmptyData)
    assert { s.isFlat }
  }
  test("empty list of signals") {
    val s = new AllSignals()
    val l = new IndicatorList(s)
    expect("AllSignals()") { s.name }
    expect(Set.empty) { s.dependencies }
    assert { s.isFlat }
    l.send(EmptyData)
    assert { s.isFlat }
  }
}
