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

/** Calculates exponential moving average of the specified indicator. Indicator's value is not set
  * until there are at least `period` bars collected. */
class Ema(period: Int, indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "EMA("+period+"; "+indicator.name+")"

  if(period < 1)
    throw new IllegalArgumentException("Period of "+name+" indicator cannot be less than 1")

  private val counter = new CountHistory(indicator)
  private val calculator = new EmaContinuous(period, indicator)

  def dependencies = Set(calculator, counter)

  def calculate = {
    case BarClose(_) if(indicator.isSet && counter.value >= period) => calculator.value
    case BarClose(_) => None
  }
}

/** Calculates exponential moving average of the specified indicator. In case indicator's value is
  * unset, `EmaContinuous` retains the last value. */
class EmaContinuous(period: Int, indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "EMA_C("+period+"; "+indicator.name+")"

  if(period < 1)
    throw new IllegalArgumentException("Period of "+name+" indicator cannot be less than 1")

  def dependencies = Set(indicator)

  val e = 2.0 / (period + 1)
  def calculate = {
    case BarClose(_) if(indicator.isSet && isSet) => e * indicator.value + (1 - e) * value
    case BarClose(_) if(indicator.isSet) => indicator.value
  }
}
// TODO Move EmaContinuous to separate file
