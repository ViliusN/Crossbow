/*
 * Copyright 2011 Vilius Normantas <code@norma.lt>
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

/** Calculates difference between two indicators of `Double` type.
  * {{{Difference = I1 - I2}}} */
class Difference(indicator1: Indicator[Double], indicator2: Indicator[Double])
    extends Indicator[Double] {
  def name = "Difference("+indicator1.name+"; "+indicator2.name+")"
  def dependencies = Set(indicator1, indicator2)
  def calculate = {
    case _ if(indicator1.isSet && indicator2.isSet) => indicator1.value - indicator2.value
    case _ => None
  }
}
