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

/**Calculates cumulative value of the specified target indicator. */
class CumulativeValue(target: Indicator[Double]) extends ListenerIndicator[Double] {
  def name = "Cumulative(" + target.name + ")"

  val targetWithDefault = new Default(target, 0.0)

  def dependencies = Set(targetWithDefault)

  override def default = 0

  def receive = {
    case BarClose(_) => set(value + targetWithDefault.value)
  }
}
