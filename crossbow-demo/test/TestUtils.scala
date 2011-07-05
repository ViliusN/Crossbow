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

package lt.norma.crossbow.demo

object TestUtils {
  import org.scalatest.TestFailedException

  def approx(expected: Double, e: Double)(value: Double) {
    if(value < expected - e || value > expected + e) throw new TestFailedException(
      "Expected "+expected+" ±"+e+", but got "+value, 0)
  }

  def assertNot(condition: Boolean) { assert { condition == false } }
}
