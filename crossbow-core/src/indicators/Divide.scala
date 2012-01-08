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

/** Calculates division between two indicators of `Double` type. If the value of `indicator2` is 0,
  * `Divide` results in `None` value.
  * {{{Divide = I1 / I2}}} */
class Divide(indicator1: Indicator[Double], indicator2: Indicator[Double])
    extends FunctionalIndicator[Double] {
  def this(indicator: Indicator[Double], constant: Double) =
    this(indicator, new Variable(constant) { override def name = constant.toString })

  def name = indicator1.name+" / "+indicator2.name
  def dependencies = Set(indicator1, indicator2)
  def calculate = (indicator1(), indicator2()) match {
    case (Some(t1), Some(t2)) if(t2 != 0) => t1 / t2
    case _ => None
  }
}
