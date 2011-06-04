/*
 * Copyright 2011 Vilius Normantas <code@norma.lt>
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
import org.joda.time.DateTime

/** Records last quote received for the specified instrument. */
class LastQuote(_instrument: Instrument) extends Indicator[Quote] {
  def name = "Last Quote"
  private val wrapped = new InstrumentWrapper(_instrument)
  def dependencies = Set(wrapped)

  def calculate = {
    case q @ Quote(i, _, _, _, _, _) if(i == instrument) => q
  }

  def setInstrument(_instrument: Instrument) { wrapped.set(_instrument) }

  def instrument = wrapped.value
}
