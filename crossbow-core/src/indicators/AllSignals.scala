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

package lt.norma.crossbow.core

/** Holds `Direction.Long` value if at least one of the specified signals has long value, and no
  * signals are short. Holds `Direction.Short` value if at least one of the specified signals has
  * short value, and no signals are long. Otherwise the values is `None`. */
class AllSignals(signals: Signal*) extends Signal {
  def name = "AllSignals("+(signals map { _.name } mkString("; "))+")"
  def dependencies = signals.toSet
  def calculate = {
    case _ =>
      val setSignals = signals.filter(_.isSet)
      if(setSignals.size > 0 && setSignals.forall(_.isLong)) {
        Direction.Long
      } else if(setSignals.size > 0 && setSignals.forall(_.isShort)) {
        Direction.Short
      } else {
        None
      }
  }
}
