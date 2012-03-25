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
import org.joda.time.DateTime
import org.scalatest.FunSuite
import lt.norma.crossbow.indicators.{LastPrice, LastTrade}

class LastPriceTest extends FunSuite {
  test("name") {
    val lastTrade = new LastTrade(new Stock("AA", Exchange.Nasdaq, "USD"))
    val lastPrice = new LastPrice(lastTrade)
    expect("Last Price") {
      lastPrice.name
    }
  }

  test("dependencies") {
    val lastTrade = new LastTrade(new Stock("AA", Exchange.Nasdaq, "USD"))
    val lastPrice = new LastPrice(lastTrade)
    expect(Set(lastTrade)) {
      lastPrice.dependencies
    }
  }

  test("initial value") {
    val lastTrade = new LastTrade(new Stock("AA", Exchange.Nasdaq, "USD"))
    val lastPrice = new LastPrice(lastTrade)
    expect(None) {
      lastPrice()
    }
  }

  test("calculation") {
    val stock = new Stock("AA", Exchange.Nasdaq, "USD")
    val lastTrade = new LastTrade(stock)
    val lastPrice = new LastPrice(lastTrade)
    lastTrade.set(Trade(stock, 5, 100, new DateTime))
    expect(Some(5)) {
      lastPrice()
    }
    lastTrade.set(Trade(stock, 0.5, 100, new DateTime))
    expect(Some(0.5)) {
      lastPrice()
    }
    lastTrade.unset()
    expect(None) {
      lastPrice()
    }
  }
}
