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

/** Calculates multiplication of the specified indicators of `Double` type. If at least one of the
  * target indicators is empty, the `Multiply` will result in `None` value.
  * {{{Multiply = I1 * I2 * ... * In}}} */
class Multiply(indicators: Indicator[Double]*) extends Indicator[Double] {
  def this(indicator: Indicator[Double], constant: Double) =
    this(indicator, new Variable(constant) { override def name = constant.toString })

  def name = "Multiply("+(indicators map { _.name } mkString("; "))+")"
  def dependencies = indicators.toSet
  def calculate = {
    case _ if(indicators.size > 0 && indicators.forall(_.isSet)) =>
      indicators map(_.value) reduceLeft(_ * _)
    case _ => None
  }
}
