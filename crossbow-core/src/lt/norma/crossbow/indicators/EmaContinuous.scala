package lt.norma.crossbow.indicators

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

import lt.norma.crossbow.core._
import lt.norma.crossbow.messages._

/**Calculates exponential moving average of the specified indicator. In case indicator's value is
 * unset, `EmaContinuous` retains the last value. */
class EmaContinuous(period: Int, indicator: Indicator[Double]) extends ListenerIndicator[Double] {
  def name = "EMA_C(" + period + "; " + indicator.name + ")"

  if (period < 1)
    throw new IllegalArgumentException("Period of " + name + " indicator cannot be less than 1")

  def dependencies = Set(indicator)

  val e = 2.0 / (period + 1)

  def receive = {
    case BarClose(_) => (optionalValue, indicator()) match {
      case (Some(v), Some(t)) => set(e * t + (1.0 - e) * v)
      case (None, Some(t)) => set(t)
      case _ =>
    }
  }
}
