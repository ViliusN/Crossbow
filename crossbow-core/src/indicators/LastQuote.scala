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
import org.joda.time.DateTime

/** Records last quote received for the specified instrument. */
class LastQuote(optionalInstrument: Option[Instrument]) extends Indicator[Quote] {
  def this() = this(None)
  def this(_instrument: Instrument) = this(Some(_instrument))

  def name = "Last Quote"
  val instrument = new InstrumentWrapper(optionalInstrument)
  def dependencies = Set(instrument)

  def calculate = {
    // Unset last quote if the instrument is empty.
    case _ if(instrument.isEmpty) => None
    // Capture the quote if instrument matches.
    case q @ Quote(i, _, _, _, _, _) if(i == instrument.value) => q
  }
}
