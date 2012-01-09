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
import ImplicitValueConverter._

/** Raises the specified target indicator to the specified power. */
class Power(target: Indicator[Double], power: Indicator[Double])
    extends FunctionalIndicator[Double] {
  def this(indicator: Indicator[Double], constant: Double) =
    this(indicator, new Variable(constant) { override def name = constant.toString })

  def name = "Power("+target.name+"; "+power.name+")"
  def dependencies = Set(target, power)
  def calculate = (target(), power()) match {
    case (Some(t), Some(p)) =>
      val v = math.pow(t, p)
      if(!v.isNaN && !v.isInfinity) {
        v
      } else {
        None
      }
    case _ => None
  }
}
