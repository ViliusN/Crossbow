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
  test("session") {
    pending
    /*println(new LocalTime(0, 0, 0))
    println(LocalTime.MIDNIGHT)
    println(new LocalTime(1, 2, 3).isBefore(new LocalTime(0, 0, 0)))
    println(new LocalTime(1, 2, 3).isAfter(new LocalTime(0, 0, 0)))
    println(new LocalTime(1, 2, 3).isBefore(LocalTime.MIDNIGHT))
    println(new LocalTime(1, 2, 3).isAfter(LocalTime.MIDNIGHT))
    println(new LocalTime(12,0).getChronology().hourOfDay().getMaximumValue())*/
  }
}
