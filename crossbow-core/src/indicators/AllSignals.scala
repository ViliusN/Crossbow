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

/** Holds `Direction.Long` value if all of the target signals are long. Holds `Direction.Short`
  * value if all of the target signals are short. Otherwise the values is `None`. */
class AllSignals(signals: Signal*) extends Signal {
  import Direction._
  def name = "AllSignals("+(signals map { _.name } mkString("; "))+")"
  def dependencies = signals.toSet
  def calculate = if(signals.size > 0) {
    case _ if(signals.forall(_.isLong)) => Long
    case _ if(signals.forall(_.isShort)) => Short
    case _ => None
  } else {
    case _ => None
  }
}
