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

/** Calculates cumulative value of the specified indicator.
  * {{{Cumulative = Cumulative[-1] + Indicator}}} */
class CumulativeValue(indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "Cumulative("+indicator.name+")"

  val math = new Indicator[Double] {
    val name = "CumulativeValueMath"
    def dependencies = Set(indicator)
    override def default = 0
    def calculate = {
      case BarClose(_) => value + indicator().getOrElse(0.0)
    }
  }

  def dependencies = Set(math)
  def calculate = {
    case BarClose(_) if(indicator.isSet) => math()
    case BarClose(_) => None
  }
}
