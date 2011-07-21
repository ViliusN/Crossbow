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
import org.scalatest.FunSuite
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTimeZone

class InstrumentTest extends FunSuite {
  case class MockOption(underlying: Instrument, right: OptionRight, strike: BigDecimal,
      expiration: DateMidnight, exchange: Exchange, currency: String) extends OptionInstrument {
    def invert = MockOption(underlying, right.invert, strike, expiration, exchange, currency)
  }
  test("toString - default date formatter") {
    expect("[MSFT] Call 2011-03-05 @30") {
      val s = Stock("MSFT", Exchange.nasdaq, "USD")
      val expiration = new DateMidnight(2011, 3, 5, Settings.timeZone)
      val so = StockOption(s, OptionRight.Call, 30, expiration, Exchange.cboe, "USD")
      so.toString
    }
    expect("[[MSFT] Call 2011-03-05 @30] Put 2012-04-06 @31") {
      val s = Stock("MSFT", Exchange.nasdaq, "USD")
      val expiration = new DateMidnight(2011, 3, 5, Settings.timeZone)
      val u = MockOption(s, OptionRight.Call, 30, expiration, Exchange.cboe, "USD")
      val so = new MockOption(u, OptionRight.Put, 31,
        new DateMidnight(2012, 4, 6, Settings.timeZone), Exchange.cboe, "USD")
      so.toString
    }
  }
}
