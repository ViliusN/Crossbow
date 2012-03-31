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
import lt.norma.crossbow.messages.Quote
import lt.norma.crossbow.messages._
import org.joda.time.DateTime
import org.scalatest.FunSuite

class CrossedMarketTest extends FunSuite {
  test("CrossedMarket test") {
    val s = new Stock("AA", Exchange.Nasdaq, "USD")
    val lq = new LastQuote(s)
    val ask = new Ask(lq)
    val bid = new Bid(lq)
    val i = new CrossedMarket(ask, bid)
    val list = new IndicatorList(i)
    expect("Crossed Market") {
      i.name
    }
    expect(Set(ask, bid)) {
      i.dependencies
    }
    expect(None) {
      i()
    }
    list.send(new Quote(s, 5, 0, 4.5, 0, new DateTime))
    expect(Some(false)) {
      i()
    }
    list.send(new Quote(s, 4.49, 0, 4.5, 0, new DateTime))
    expect(Some(true)) {
      i()
    }
    list.send(new Quote(s, 8, 0, 8, 0, new DateTime))
    expect(Some(false)) {
      i()
    }
    list.send(new Quote(s, 0, 0, 0.000001, 0, new DateTime))
    expect(Some(true)) {
      i()
    }
  }
}
