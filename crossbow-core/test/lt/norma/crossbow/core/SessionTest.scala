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

import org.joda.time.LocalTime
import org.scalatest.FunSuite

class SessionTest extends FunSuite {
  test("creation by factory method - without name") {
    val s = Session(new LocalTime(9, 30), false, new LocalTime(16, 0), true)
    assert { !s.startInclusive }
    assert { s.endInclusive }
    expect("") { s.name }
  }

  test("creation by factory method - inclusive start, non-inclusive end, with name") {
    val s = Session("abc", new LocalTime(9, 30), new LocalTime(16, 0))
    assert { s.startInclusive }
    assert { !s.endInclusive }
    expect("abc") { s.name }
  }

  test("creation by factory method - inclusive start, non-inclusive end, without name") {
    val s = Session(new LocalTime(9, 30), new LocalTime(16, 0))
    assert { s.startInclusive }
    assert { !s.endInclusive }
    expect("") { s.name }
  }

  test("isOpenAt - closing time is after opening time - inclusive") {
    val s = Session("name", new LocalTime(9, 30), true, new LocalTime(16, 0), true)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(!s.isOpenAt(new LocalTime(9, 29)))
    assert(!s.isOpenAt(new LocalTime(9, 0)))
    assert(!s.isOpenAt(new LocalTime(0, 1)))
    assert(!s.isOpenAt(new LocalTime(0, 0)))
    assert(!s.isOpenAt(LocalTime.MIDNIGHT))
    assert(!s.isOpenAt(new LocalTime(16, 1)))
    assert(!s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - closing time is after opening time - non-inclusive") {
    val s = Session("name", new LocalTime(9, 30), false, new LocalTime(16, 0), false)
    assert(!s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(!s.isOpenAt(new LocalTime(16, 0)))
    assert(!s.isOpenAt(new LocalTime(9, 29)))
    assert(!s.isOpenAt(new LocalTime(9, 0)))
    assert(!s.isOpenAt(new LocalTime(0, 1)))
    assert(!s.isOpenAt(new LocalTime(0, 0)))
    assert(!s.isOpenAt(LocalTime.MIDNIGHT))
    assert(!s.isOpenAt(new LocalTime(16, 1)))
    assert(!s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - closing time is before opening time - inclusive") {
    val s = Session("name", new LocalTime(16, 0), true, new LocalTime(9, 30), true)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(!s.isOpenAt(new LocalTime(9, 31)))
    assert(!s.isOpenAt(new LocalTime(12, 0)))
    assert(!s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - closing time is before opening time - non-inclusive") {
    val s = Session("name", new LocalTime(16, 0), false, new LocalTime(9, 30), false)
    assert(!s.isOpenAt(new LocalTime(9, 30)))
    assert(!s.isOpenAt(new LocalTime(9, 31)))
    assert(!s.isOpenAt(new LocalTime(12, 0)))
    assert(!s.isOpenAt(new LocalTime(15, 59)))
    assert(!s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - closing time is equal to opening time - inclusive, inclusive") {
    val s = Session("name", new LocalTime(16, 0), true, new LocalTime(16, 0), true)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - closing time is equal to opening time - inclusive, non-inclusive") {
    val s = Session("name", new LocalTime(16, 0), false, new LocalTime(16, 0), true)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - closing time is equal to opening time - non-inclusive, inclusive") {
    val s = Session("name", new LocalTime(16, 0), false, new LocalTime(16, 0), true)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - closing time is equal to opening time - non-inclusive, non-inclusive") {
    val s = Session("name", new LocalTime(16, 0), false, new LocalTime(16, 0), false)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(!s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - no closing time - inclusive") {
    val s = Session("name", new LocalTime(9, 30), true, LocalTime.MIDNIGHT, true)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(!s.isOpenAt(new LocalTime(9, 29)))
    assert(!s.isOpenAt(new LocalTime(9, 0)))
    assert(!s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - no closing time - non-inclusive") {
    val s = Session("name", new LocalTime(9, 30), false, LocalTime.MIDNIGHT, false)
    assert(!s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(!s.isOpenAt(new LocalTime(9, 29)))
    assert(!s.isOpenAt(new LocalTime(9, 0)))
    assert(!s.isOpenAt(new LocalTime(0, 1)))
    assert(!s.isOpenAt(new LocalTime(0, 0)))
    assert(!s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - no opening time - inclusive") {
    val s = Session("name", LocalTime.MIDNIGHT, true, new LocalTime(16, 0), true)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(!s.isOpenAt(new LocalTime(16, 1)))
    assert(!s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - no opening time - non-inclusive") {
    val s = Session("name", LocalTime.MIDNIGHT, false, new LocalTime(16, 0), false)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(!s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(!s.isOpenAt(new LocalTime(0, 0)))
    assert(!s.isOpenAt(LocalTime.MIDNIGHT))
    assert(!s.isOpenAt(new LocalTime(16, 1)))
    assert(!s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - no opening or closing time - inclusive, inclusive") {
    val s = Session("name", LocalTime.MIDNIGHT, true, LocalTime.MIDNIGHT, true)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - no opening or closing time - inclusive, non-inclusive") {
    val s = Session("name", LocalTime.MIDNIGHT, true, LocalTime.MIDNIGHT, false)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - no opening or closing time - non-inclusive, inclusive") {
    val s = Session("name", LocalTime.MIDNIGHT, false, LocalTime.MIDNIGHT, true)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(s.isOpenAt(new LocalTime(0, 0)))
    assert(s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }

  test("isOpenAt - no opening or closing time - non-inclusive, non-inclusive") {
    val s = Session("name", LocalTime.MIDNIGHT, false, LocalTime.MIDNIGHT, false)
    assert(s.isOpenAt(new LocalTime(9, 30)))
    assert(s.isOpenAt(new LocalTime(9, 31)))
    assert(s.isOpenAt(new LocalTime(12, 0)))
    assert(s.isOpenAt(new LocalTime(15, 59)))
    assert(s.isOpenAt(new LocalTime(16, 0)))
    assert(s.isOpenAt(new LocalTime(9, 29)))
    assert(s.isOpenAt(new LocalTime(9, 0)))
    assert(s.isOpenAt(new LocalTime(0, 1)))
    assert(!s.isOpenAt(new LocalTime(0, 0)))
    assert(!s.isOpenAt(LocalTime.MIDNIGHT))
    assert(s.isOpenAt(new LocalTime(16, 1)))
    assert(s.isOpenAt(new LocalTime(21, 0)))
  }
}
