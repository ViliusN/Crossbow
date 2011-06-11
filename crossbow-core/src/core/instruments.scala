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
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatter

/** Exchange information.
  * @param name      name of the exchange
  * @param timeZone  time zone at the exchange */
case class Exchange(name: String, timeZone: DateTimeZone)
object Exchange {
  def nasdaq = Exchange("NASDAQ", DateTimeZone.forID("America/New_York"))
  def nyse = Exchange("NYSE", DateTimeZone.forID("America/New_York"))
  def cboe = Exchange("CBOE", DateTimeZone.forID("America/Chicago"))
}

/** A base class for all financial instruments. Extend this class to create custom instrument
  * types. */
abstract class Instrument {
  /** Exchange where the instrument is traded. */
  def exchange: Exchange
  /** Base currency of the instrument. */
  def currency: String
}

/** Stock financial instrument. */
case class Stock(symbol: String, exchange: Exchange, currency: String) extends Instrument {
  /** Returns symbol of the stock. */
  override def toString = symbol
}

/** Stock option financial instrument. */
case class StockOption(underlying: Stock, right: OptionRight, strike: BigDecimal,
    expiration: DateMidnight, exchange: Exchange, currency: String) extends Instrument {
  /** Writes data of the stock option to a string. Uses the specified date formatter. */
  def toString(dateFormatter: DateTimeFormatter): String = {
    underlying.symbol+" "+right+" "+dateFormatter.print(expiration)+" @"+strike.toString()
  }

  /** Writes data of the stock option to a string. Uses default date formatter. */
  override def toString: String = toString(Settings.dateFormatter)

  /** Returns stock option with the same parameters, but inverted right of the option (`Call` is
    * changed to `Put` and vice versa). */
  def invertRight = StockOption(underlying, right.invert, strike, expiration, exchange, currency)
}

/** Option rights: call and put. */
sealed trait OptionRight {
  def invert: OptionRight
}
/** Option rights: Call and Put. */
object OptionRight {
  /** Call option right. */
  case object Call extends OptionRight {
    override def toString = "Call"
    def invert = Put
  }
  /** Put option right. */
  case object Put extends OptionRight {
    override def toString = "Put"
    def invert = Call
  }
}
