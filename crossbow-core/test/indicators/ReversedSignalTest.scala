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

class ReversedSignalTest extends FunSuite {
  import Direction._
  test("ReversedSignal") {
    val s1 = new MutableSignal { def name = "S1"; def dependencies = Empty }
    val s = new ReversedSignal(s1)
    val l = new IndicatorList(s)
    expect("Reversed(S1)") { s.name }
    expect(Set(s1)) { s.dependencies }
    assert { !s.isSet }

    l.send(EmptyMessage)
    expect(None) { s() }
    s1.set(Long)
    l.send(EmptyMessage)
    expect(Some(Short)) { s() }
    s1.set(Short)
    l.send(EmptyMessage)
    expect(Some(Long)) { s() }
    s1.unset()
    l.send(EmptyMessage)
    expect(None) { s() }
  }
}
