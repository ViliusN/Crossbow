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

import lt.norma.crossbow.core.Direction._

/** Holds `Direction.Long` if the amount of long signals is greater than the amount of short
  * and flat signals combined. Holds `Direction.Short` if the amount of short signals is greater
  * than the amount of long and flat signals combined. Otherwise holds `None` value. */
class MostSignals(signals: Signal*) extends FunctionalSignal {
  def name = "MostSignals("+(signals map { _.name } mkString("; "))+")"
  def dependencies = signals.toSet
  def calculate = {
    val longCount = signals.count(_.isLong)
    val shortCount = signals.count(_.isShort)
    val total = signals.size
    if(longCount > total - longCount) {
      Some(Long)
    } else if(shortCount > total - shortCount) {
      Some(Short)
    } else {
      None
    }
  }
}
