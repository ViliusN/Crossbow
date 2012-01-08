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

/** Records last trade received for the specified instrument. */
class LastTrade(val instrument: InstrumentWrapper = new InstrumentWrapper())
    extends ListenerIndicator[Trade] {
  def this(_instrument: Instrument) = this(new InstrumentWrapper(_instrument))

  def name = "Last Trade"
  def dependencies = Set(instrument)

  def receive = {
    // Unset last trade if the instrument is empty.
    case _ if(!instrument.isSet) => set(None)
    // Capture the trade if instrument matches.
    case t @ Trade(i, _, _, _) if(i == instrument.value) => set(t)
  }
}
