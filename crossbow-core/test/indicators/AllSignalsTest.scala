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
    val s1 = new Signal { def name = "S1"; def dependencies = Empty; def calculate = Empty }
    val s2 = new Signal { def name = "S2"; def dependencies = Empty; def calculate = Empty }
    val s3 = new Signal { def name = "S3"; def dependencies = Empty; def calculate = Empty }
    val s = new AllSignals(s1, s2, s3)
    val l = new IndicatorList(s)
    expect("AllSignals(S1; S2; S3)") { s.name }
    expect(Set(s1, s2, s3)) { s.dependencies }
    assert { s.isEmpty }

    l.send(EmptyData)
    assert { s.isEmpty }
    s1.set(Long)
    l.send(EmptyData)
    assert { s.isLong }
    s2.set(Long)
    l.send(EmptyData)
    assert { s.isLong }
    s3.set(Short)
    l.send(EmptyData)
    assert { s.isEmpty }
    s1.set(Short)
    s2.set(Short)
    l.send(EmptyData)
    assert { s.isShort }
    s1.unset()
    l.send(EmptyData)
    assert { s.isShort }
    s2.unset()
    s3.unset()
    l.send(EmptyData)
    assert { s.isEmpty }
  }
  test("AllSignals - one signal") {
    val s1 = new Signal { def name = "S1"; def dependencies = Empty; def calculate = Empty }
    val s = new AllSignals(s1)
    val l = new IndicatorList(s)
    expect("AllSignals(S1)") { s.name }
    expect(Set(s1)) { s.dependencies }
    assert { s.isEmpty }

    l.send(EmptyData)
    assert { s.isEmpty }
    s1.set(Long)
    l.send(EmptyData)
    assert { s.isLong }
    s1.set(Short)
    l.send(EmptyData)
    assert { s.isShort }
    s1.unset()
    l.send(EmptyData)
    assert { s.isEmpty }
  }
  test("AllSignals - empty") {
    val s = new AllSignals()
    val l = new IndicatorList(s)
    expect("AllSignals()") { s.name }
    expect(Set.empty) { s.dependencies }
    expect(None) { s() }
    l.send(EmptyData)
    expect(None) { s() }
  }
}
