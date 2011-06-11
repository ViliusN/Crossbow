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

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter

/** Holds information about a trade.
  *
  * @param instrument  financial instrument
  * @param price       price of the trade
  * @param size        number of contracts traded (volume)
  * @param time        time of the trade */
case class Trade(instrument: Instrument, price: BigDecimal, size: Long, time: DateTime)
    extends Data {
  /** Uses the specified time format. Example output:
    * {{{Trade MSFT 800@30.2 [2009-12-19 09:45:52]}}}
    *  @return trade data as text */
  def toString(dateTimeFormatter: DateTimeFormatter): String = {
    "Trade "+instrument+" "+size+"@"+price+" ["+dateTimeFormatter.print(time)+"]";
  }
  /** Uses default date-time format.
    * @return trade data as text */
  override def toString: String = toString(Settings.dateTimeFormatter)
}
