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
  test("Stock") {
    val s = Stock("MSFT", Exchange.nasdaq, "USD")
    expect("MSFT") { s.toString }
    assert { s == Stock("MSFT", Exchange.nasdaq, "USD") }
    assert { s != Stock("MSF", Exchange.nasdaq, "USD") }
    assert { s != Stock("MSFT", Exchange("NASDAQ", DateTimeZone.forID("America/Chicago")), "USD") }
    assert { s != Stock("MSFT", Exchange.nasdaq, "Usd") }
  }
  test("StockOption") {
    expect("MSFT Call 2011-03-05 @30") {
      val s = Stock("MSFT", Exchange.nasdaq, "USD")
      val expiration = new DateMidnight(2011, 3, 5, Settings.timeZone)
      val so = StockOption(s, OptionRight.Call, 30, expiration, Exchange.cboe, "USD")
      so.toString
    }
    expect("XOM Put Mar, 11 @30.01") {
      val s = Stock("XOM", Exchange.nyse, "USD")
      val expiration = new DateMidnight(2011, 3, 5, Settings.timeZone)
      val so = StockOption(s, OptionRight.Put, 30.01, expiration, Exchange.cboe, "USD")
      val dateFormatter = DateTimeFormat.forPattern("MMM, YY").withZone(Settings.timeZone)
      so.toString(dateFormatter)
    }
    val s = Stock("MSFT", Exchange.nasdaq, "USD")
    val sWrong = Stock("MSFT-", Exchange.nasdaq, "USD")
    val expiration = new DateMidnight(2011, 3, 5, Settings.timeZone)
    val expirationWrong = new DateMidnight(2011, 3, 4, Settings.timeZone)
    val so = StockOption(s, OptionRight.Call, 30, expiration, Exchange.cboe, "USD")
    assert { so == StockOption(s, OptionRight.Call, 30, expiration, Exchange.cboe, "USD") }
    assert { so != StockOption(sWrong, OptionRight.Call, 30, expiration, Exchange.cboe, "USD") }
    assert { so != StockOption(s, OptionRight.Put, 30, expiration, Exchange.cboe, "USD") }
    assert { so != StockOption(s, OptionRight.Call, 30.000001, expiration, Exchange.cboe, "USD") }
    assert { so != StockOption(s, OptionRight.Call, 30, expirationWrong, Exchange.cboe, "USD") }
    assert { so != StockOption(s, OptionRight.Call, 30, expiration, Exchange.nasdaq, "USD") }
    assert { so != StockOption(s, OptionRight.Call, 30, expiration, Exchange.cboe, "EUR") }
  }
}
