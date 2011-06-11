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

import org.joda.time.DateMidnight

/** Stock dividend corporate event. */
case class Dividend(instrument: Instrument, amount: BigDecimal, exDividendDate: DateMidnight,
    paymentDate: DateMidnight) extends Data

/** Stock split corporate event.
  *
  * Example:
  * {{{
  * StockSplit(stock, 2, 1, date)  // Represents a 2 to 1 split
  * }}} */
case class StockSplit(instrument: Instrument, ratioAfter: Int, ratioBefore: Int, date: DateMidnight)
    extends Data
