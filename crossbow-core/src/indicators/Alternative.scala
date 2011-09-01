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

/** Holds value of the first indicator if it is not empty. Otherwise the holds value of the second
  * indicator. */
class Alternative[Value : Manifest](indicator: Indicator[Value], val alternative: Indicator[Value])
    extends Indicator[Value] {
  def name = indicator.name+" or alternative "+alternative.name
  def dependencies = Set(indicator, alternative)

  def calculate = {
    case _ if(indicator.isEmpty) => alternative()
    case _ => indicator.value
  }

  initialize
}
