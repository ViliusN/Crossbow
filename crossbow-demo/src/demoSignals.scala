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

import lt.norma.crossbow.core._
import lt.norma.crossbow.indicators._

/** Sets value to long when the difference between two indicators is greater than delta. Values is
  * short, if the difference is smaller than -delta. Otherwise the values is not set. */
class DifferenceSignal(indicator1: Indicator[Double], indicator2: Indicator[Double], delta: Double)
    extends Signal {
  def name = "Difference signal"

  val diff = new Difference(indicator1, indicator2)
  def dependencies = Set(diff)

  def calculate = {
    case _ if(diff.isSet && diff.value > delta) => Direction.Long
    case _ if(diff.isSet && diff.value < -delta) => Direction.Short
    case _ => None
  }
}
