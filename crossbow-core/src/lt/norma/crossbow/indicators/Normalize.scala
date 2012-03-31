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

/**Calculates normalized value of the indicator. If the first value of the target indicator is 0,
 * `Normalized` will always result to `NaN` or `Infinity` values.
 * {{{ Normalize = (Target[i] / Target[0] - 1) * 100 }}}*/
class Normalize(target: Indicator[Double]) extends FunctionalIndicator[Double] {
  def name = "Normalize(" + target.name + ")"

  private val target0 = new FirstValue(target)

  def dependencies = Set(target, target0)

  def calculate = (target(), target0()) match {
    case (Some(t), Some(t0)) => (t / t0 - 1) * 100
    case _ => None
  }
}
