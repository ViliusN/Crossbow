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

package lt.norma.crossbow.core.indicators

import lt.norma.crossbow.core._
import ImplicitValueConverter._

/** Calculates division between two indicators of `Double` type. If the value of `indicator2` is 0,
  * `Divide` results in `None` value.
  * {{{Divide = I1 / I2}}} */
class Divide(target1: Indicator[Double], target2: Indicator[Double])
    extends Transformation2(target1, target2)({
  case (Some(t1), Some(t2)) if(t2 != 0) => t1 / t2
  case _ => None
}) {
  def this(target: Indicator[Double], constant: Double) =
    this(target, new Variable(constant) { override def name = constant.toString })
  override def name = target1.name+" / "+target2.name
}
