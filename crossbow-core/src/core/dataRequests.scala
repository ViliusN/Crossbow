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

/* This file contains data requests which should be supported by most data providers. */

/** Request for the provider to establish connection to it's data source. */
case object Connect extends Request
/** Request for the provider to drop connection from it's data source. */
case object Disconnect extends Request
/** Response to connection request. */
case object Connected extends Response { def request = Connect }
/** Response to disconnection request. */
case object Disconnected extends Response { def request = Disconnect }

/** Stock look-up request by the specified symbol. */
case class StockLookupRequest(symbol: String) extends Request
/** Options look-up request by the specified underlying instrument. */
case class OptionsLookupRequest(underlying: Instrument) extends Request
/** Futures look-up request by the specified underlying instrument. */
case class FuturesLookupRequest(underlying: Instrument) extends Request
/** Response to instrument look-up requests. */
case class LookupResult(request: Request, instruments: List[Instrument]) extends Response

/** Market data request. */
case class MarketDataRequest(instrument: Instrument) extends Request
