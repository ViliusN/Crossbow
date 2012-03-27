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
  test("isOpenAt - closing time is after opening time") {
    val s = Session("name", new LocalTime(9, 30), new LocalTime(16, 0))
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

  test("isOpenAt - closing time is before opening time") {
    val s = Session("name", new LocalTime(16, 0), new LocalTime(9, 30))
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

  test("isOpenAt - closing time is equal to opening time") {
    val s = Session("name", new LocalTime(16, 0), new LocalTime(16, 0))
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

  test("isOpenAt - no closing time") {
    val s = Session("name", new LocalTime(9, 30), LocalTime.MIDNIGHT)
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

  test("isOpenAt - no opening time") {
    val s = Session("name", LocalTime.MIDNIGHT, new LocalTime(16, 0))
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

  test("isOpenAt - no opening or closing time") {
    val s = Session("name", LocalTime.MIDNIGHT, LocalTime.MIDNIGHT)
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
}
