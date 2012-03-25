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

/**Requests options chain at the opening of trading session. */
class OptionsChain(underlying: Instrument, dataProvider: DataNode)
  extends ListenerIndicator[List[OptionInstrument]] {
  def name = "Options chain for " + underlying

  def dependencies = Empty

  def receive = {
    case SessionOpen(time) =>
      dataProvider.send(OptionsLookupRequest(underlying))
    case LookupResult(OptionsLookupRequest(`underlying`), chain) =>
      set(chain.map(_.asInstanceOf[OptionInstrument]))
  }

  // Check if options look-up requests are supported by the specified provider
  if (!dataProvider.supports(OptionsLookupRequest(underlying))) {
    throw Exception("Data provider doesn't support options look-up requests")
  }
}
