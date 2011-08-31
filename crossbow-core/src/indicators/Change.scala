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

/** Calculates change in indicator's value since `numberOfBars` ago. `numberOfBars` cannot be less
  * than 1.
  *
  * For example, if `numberOfBars` is set to 1, the value is calculated as follows:
  * {{{ Change = Indicator.value - Indicator.history[last] }}}
  * If `numberOfBars` is set to n:
  * {{{ Change = Indicator.value - Indicator.history[last-(n-1)] }}}
  * */
class Change(period: Int, indicator: Indicator[Double] with History) extends Indicator[Double] {
  def name = "Change("+period+"; "+indicator.name+")"

  if(period < 1)
    throw new IllegalArgumentException("Period of "+name+" indicator cannot be less than 1")

  def dependencies = Set(indicator)

  def calculate = {
    case _ => calculateValue(period)
  }

  private def calculateValue(n: Int): Option[Double] = {
    (indicator.history.take(n), indicator()) match {
      case (bars @ List(Some(previous), _*), Some(value)) if(bars.size == n) =>
        Some(value - previous)
      case _ =>
        None
    }
  }
}
// TODO capture previous value, don't use the history
