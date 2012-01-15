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

/** Calculates change in indicator's value since the specified period ago. Period of 1 means that
  * the change is calculated between current target's value and the most recent historical value.
  *
  * `period` cannot be less than 1. */
class Change(period: Int, target: Indicator[Double] with History)
  extends Transformation2(target, new HistoryAt(period - 1, target))({
    case (Some(v), Some(hv)) => Some(v - hv)
    case _ => None
  }) {
  override def name = "Change("+period+"; "+target.name+")"
  // Check for invalid period values
  if(period < 1) throw Exception("Period of "+name+" indicator cannot be less than 1")
}
